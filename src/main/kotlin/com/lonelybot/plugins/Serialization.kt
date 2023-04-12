package com.lonelybot.plugins

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.lonelybot.NotSerializable
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        gson { setExclusionStrategies(object : ExclusionStrategy{
            override fun shouldSkipField(f: FieldAttributes?): Boolean {
                return f?.getAnnotation(NotSerializable::class.java) != null
            }

            override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                return false
            }

        }) }
    }

    routing {
        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}
