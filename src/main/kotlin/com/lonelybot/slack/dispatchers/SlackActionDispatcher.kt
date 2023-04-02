package com.lonelybot.slack.dispatchers

import com.google.gson.Gson
import com.lonelybot.*
import com.lonelybot.not.NotionApp
import com.lonelybot.not.NotionPageBuilder
import com.lonelybot.services.notion.getCurrentUser
import com.lonelybot.slack.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch

fun Route.actionReader(){
    post ("/actions"){        
        val parameters = call.receiveParameters()["payload"]
        parameters.let(::println)
        val action = Gson().fromJson(parameters.toString(), SlackAction::class.java)
        
        launch {
            action.callbackId.let(::println)
            when(action.callbackId){
                "yellow_card" -> getActionAs<SlackMessageAction>(parameters).yellowCard()
                "red_card" -> getActionAs<SlackMessageAction>(parameters).redCard()
                null -> {
                    val actionBlock = getActionAs<SlackBlockAction>(parameters)

                    when(val actionId = actionBlock.getActionId()){
                        "save-time-remaining" -> actionBlock.saveLeavingTime()
                    }
                }
            }
        }
        
        call.respond(HttpStatusCode.OK)
    }
}
suspend fun SlackBlockAction.saveLeavingTime() {
    val stateFriday = getStateOf<String>(VIEW_HOME_SECTION_FRIDAY_ID, VIEW_HOME_ACTIONID_FRIDAY, "selected_time")
    val stateWeek = getStateOf<String>(VIEW_HOME_SECTION_WEEK_ID, VIEW_HOME_ACTIONID_WEEK, "selected_time")

    val currentUser = getCurrentUser(this)

    val builder = NotionPageBuilder.build(null) {
        addRichText("LeavingOnFriday", stateFriday)
        addRichText("LeavingRestOfWeek", stateWeek)
    }

    NotionApp.request.post.updatePage(currentUser.notionId!!, builder)
    SlackApp.request.post.sendTextMessage(currentUser.slackImChannel!!, "Pstt! Solo te informo de que tus horarios se han guardado.")
}

suspend fun SlackMessageAction.yellowCard(){
    val card = Card(user!!.username, NotionTags.YELLOW_COLOUR, message.user!!, channel!!.id)
    processCardService(card)
}

suspend fun SlackMessageAction.redCard(){
    val card = Card(user!!.username, NotionTags.RED_COLOUR, message.user!!, channel!!.id)
    processCardService(card)
}

inline fun <reified T> getActionAs(json: String?) : T{
    return Gson().fromJson(json, T::class.java)
}