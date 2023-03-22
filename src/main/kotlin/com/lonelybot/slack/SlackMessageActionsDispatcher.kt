package com.lonelybot.slack

import com.google.gson.Gson
import com.lonelybot.NotionTags
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun Route.messageActionLecture(){
    post ("/actions"){        
        val parameters = call.receiveParameters()["payload"]
        parameters.let(::println)
        val messageAction = Gson().fromJson(parameters.toString(), SlackMessageAction::class.java)
        
        launch {
            when(messageAction.callbackId){
                "yellow_card" -> messageAction.yellowCard()
                "red_card" -> messageAction.redCard()
            }    
        }
        
        call.respond(HttpStatusCode.OK)
    }
}

suspend fun SlackMessageAction.yellowCard(){
    val card = Card(user.username, NotionTags.YELLOW_COLOUR, message.user!!, channel.id)
    processCardService(card)
}

suspend fun SlackMessageAction.redCard(){
    val card = Card(user.username, NotionTags.RED_COLOUR, message.user!!, channel.id)
    processCardService(card)
}