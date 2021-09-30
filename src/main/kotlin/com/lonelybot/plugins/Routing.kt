package com.lonelybot.plugins

import com.lonelybot.slack.commandLecture
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        commandLecture()
    }
}
