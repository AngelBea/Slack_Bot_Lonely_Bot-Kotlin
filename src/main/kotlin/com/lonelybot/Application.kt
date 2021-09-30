package com.lonelybot


import com.google.gson.Gson
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.lonelybot.plugins.*
import com.lonelybot.slack.SlackApp

fun main() {


    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
    }.start(wait = false)
    SlackApp
}
