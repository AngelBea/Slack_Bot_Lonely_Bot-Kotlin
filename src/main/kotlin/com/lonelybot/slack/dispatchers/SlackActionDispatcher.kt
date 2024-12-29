package com.lonelybot.slack.dispatchers

import com.google.gson.Gson
import com.jayway.jsonpath.PathNotFoundException
import com.lonelybot.*
import com.lonelybot.notion.NotionApi
import com.lonelybot.services.global.createRedCard
import com.lonelybot.services.global.createYellowCard
import com.lonelybot.services.notion.BadUserException
import com.lonelybot.services.global.getCurrentUser
import com.lonelybot.services.global.isPermitted
import com.lonelybot.services.notion.MemeService
import com.lonelybot.slack.*
import com.lonelybot.slack.factories.ViewFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.coroutines.launch
import me.angelbea.application.notion.builders.NotionPageBuilder

fun Route.actionReader(){
    post ("/actions"){        
        val parametersReceived = call.receiveParameters()
        val parameters = parametersReceived["payload"] ?: call.receiveChannel().readUTF8Line()
        "PARAMETERS -> $parameters".let(::println)
        val action = Gson().fromJson(parameters.toString(), SlackAction::class.java)
        
        launch {
            action.callbackId.let(::println)
            when(action.callbackId){
                "yellow_card" -> getActionAs<SlackMessageAction>(parameters).yellowCard()
                "red_card" -> getActionAs<SlackMessageAction>(parameters).redCard()
                null -> {
                    if (action.type == "view_submission"){
                        val viewSubmission = getActionAs<SlackViewSubmission>(parameters)
                        
                        when(viewSubmission.getExternalId()){
                            VIEW_MODAL_YELLOW_CARD_ID -> viewSubmission.yellowCard()
                            VIEW_MODAL_RED_CARD_ID -> viewSubmission.redCard()
                        }
                    }else{
                        val actionBlock = getActionAs<SlackBlockAction>(parameters)
    
                        when(actionBlock.getActionId()){
                            "save-time-remaining" -> actionBlock.saveLeavingTime()
                            VIEW_HOME_SAVE_CARD_BUTTON_ID -> actionBlock.uploadCard()
                        }                        
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

    NotionApi.Pages.updatePage(currentUser.notionId!!, builder)
    SlackApp.request.post.sendTextMessage(currentUser.slackImChannel!!, "Pstt! Solo te informo de que tus horarios se han guardado.")
}

suspend fun SlackMessageAction.yellowCard(){
    ViewFactory.buildLoadingModal(VIEW_MODAL_YELLOW_CARD_ID, triggerId!!).deploy()
    var toUser: String
    try{
        if (message.user == null){
            throw BadUserException()  
        }else{
            toUser = message.user 
        }     
    }catch(exc: BadUserException){
        SlackApp.request.post.sendHiddenMessage(channel!!.id, exc.message, user!!.id)
        return
    }
    
    val fromUser = getCurrentUser(this)
    if (!fromUser.isPermitted(Permissions.CARDS)){
        SlackApp.request.post.sendHiddenMessage(channel!!.id, COMMAND_OR_ACTION_BLOCKED, fromUser.slackId)
        return
    }
    
    val toUserAd = getCurrentUser(message.user, team!!.id)
    ViewFactory.buildModalYellowCard(fromUser, toUserAd, triggerId!!, channel!!.id).update(VIEW_MODAL_YELLOW_CARD_ID)
}

suspend fun SlackViewSubmission.yellowCard(){
    val toUser = getStateOf<String>(VIEW_MODAL_YELLOW_CARD_USER_BLOCK_ID, VIEW_MODAL_YELLOW_USER_CARD_ACTION_ID, "selected_user")
    val reason = getStateOf<String>(VIEW_MODAL_YELLOW_CARD_TEXT_BLOCK_ID, VIEW_MODAL_YELLOW_CARD_TEXT_ACTION_ID, "value")
    val channel = getStateOf<String>(VIEW_MODAL_YELLOW_CARD_CHANNEL_BLOCK_ID, VIEW_MODAL_YELLOW_CARD_CHANNEL_ACTION_ID, "selected_conversation")
    
    this.let(::println)
    val card = Card(user!!.id, NotionTags.YELLOW_COLOUR, toUser, channel, reason, team!!.id)
    processCardService(card)
}
suspend fun SlackMessageAction.redCard(){
    ViewFactory.buildLoadingModal(VIEW_MODAL_RED_CARD_ID, triggerId!!).deploy()
    var toUser: String
    try{
        if (message.user == null){
            throw BadUserException()
        }else{
            toUser = message.user
        }
    }catch(exc: BadUserException){
        SlackApp.request.post.sendHiddenMessage(channel!!.id, exc.message, user!!.id)
        return
    }

    val fromUser = getCurrentUser(this)
    if (!fromUser.isPermitted(Permissions.CARDS)){
        SlackApp.request.post.sendHiddenMessage(channel!!.id, COMMAND_OR_ACTION_BLOCKED, fromUser.slackId)
        return
    }

    val toUserAd = getCurrentUser(message.user, team!!.id)
    ViewFactory.buildModalRedCard(fromUser, toUserAd, triggerId!!, channel!!.id).update(VIEW_MODAL_RED_CARD_ID)
}

suspend fun SlackViewSubmission.redCard(){
    val toUser = getStateOf<String>(VIEW_MODAL_RED_CARD_USER_BLOCK_ID, VIEW_MODAL_RED_USER_CARD_ACTION_ID, "selected_user")
    val reason = getStateOf<String>(VIEW_MODAL_RED_CARD_TEXT_BLOCK_ID, VIEW_MODAL_RED_CARD_TEXT_ACTION_ID, "value")
    val channel = getStateOf<String>(VIEW_MODAL_RED_CARD_CHANNEL_BLOCK_ID, VIEW_MODAL_RED_CARD_CHANNEL_ACTION_ID, "selected_conversation")
    
    val card = Card(user!!.id, NotionTags.RED_COLOUR, toUser, channel, reason, team!!.id)

    processCardService(card)
}

suspend fun SlackBlockAction.uploadCard(){
    ViewFactory.buildLoadingModal(UPLOAD_MODAL_ID, triggerId!!).deploy()
    val currentUser = getCurrentUser(this)
    var url : String? = null 
    var cardType: String? = null
    try{
        url = getStateOf<String>(VIEW_INPUT_URL_BLOCK_ID, VIEW_INPUT_CARD_URL_ID, "value")
        cardType = getStateOf<String>(VIEW_INPUT_OPTION_CARD_URL_ID, VIEW_INPUT_OPTION_CARD_SELECTED_URL_ID, "selected_option", "value")        
    }catch (exc: PathNotFoundException){
        
    }
    
    url ?: ViewFactory
        .buildLoadingModalError(UPLOAD_MODAL_ID, triggerId!!, "Debes rellenar la URL para subir.")
        .update(UPLOAD_MODAL_ID)
    url ?: return
    
    val meme = when(cardType){
        "Amarilla" -> createYellowCard(url!!, currentUser)
        "Roja" -> createRedCard(url!!, currentUser)
        else -> {
            ViewFactory
                .buildLoadingModalError(UPLOAD_MODAL_ID, triggerId!!, "Debes seleccionar un valor valido Roja o Amarilla")
                .update(UPLOAD_MODAL_ID)
            null   
        }
    } ?: return

    ViewFactory.buildModalUploadConfirmation(triggerId, meme).update(UPLOAD_MODAL_ID)
}

inline fun <reified T> getActionAs(json: String?) : T{
    return Gson().fromJson(json, T::class.java)
}