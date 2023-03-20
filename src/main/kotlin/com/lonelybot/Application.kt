package com.lonelybot


import com.google.gson.Gson
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.lonelybot.plugins.*
import com.lonelybot.slack.SlackApp

fun main(args: Array<String>) {
    args.forEach { it.let(::println) }

    embeddedServer(Netty, System.getenv("PORT").toInt(), host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
    }.start(wait = false)
    SlackApp
}
