package com.lonelybot.plugins

import com.lonelybot.slack.commandLecture
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        commandLecture()
    }
}
