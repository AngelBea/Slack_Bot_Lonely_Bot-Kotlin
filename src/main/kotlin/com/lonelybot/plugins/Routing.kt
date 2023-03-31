package com.lonelybot.plugins

import com.lonelybot.slack.dispatchers.commandReader
import com.lonelybot.slack.dispatchers.actionReader
import com.lonelybot.slack.dispatchers.eventReader
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        commandReader()
        actionReader()
        eventReader()
    }
}
