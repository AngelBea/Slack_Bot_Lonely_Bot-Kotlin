package com.lonelybot.slack.dispatchers

import com.lonelybot.services.notion.getCurrentUser
import com.lonelybot.slack.SlackEvent
import com.lonelybot.slack.factories.ViewFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch

fun Route.eventReader(){
    post("/slack/events") {
        val evt = call.receive<SlackEvent>()
        evt.let(::println)
        
        if (evt.type == "url_verification") call.respond(HttpStatusCode.OK, evt.challenge)
        launch {
            when(evt.event.type){
                "app_home_opened" -> evt.returnHomeForUser()
            }   
        }
        
        call.respond(HttpStatusCode.OK)
    }
}

suspend fun SlackEvent.returnHomeForUser(){
    val slackUser = getCurrentUser(this)
    
    val home = ViewFactory.buildHomeForUser(slackUser)
    home.deploy()
}