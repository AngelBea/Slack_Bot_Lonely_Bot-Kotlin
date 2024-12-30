package com.lonelybot

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.lonelybot.deserializers.LocalDateTimeTypeAdapter
import com.lonelybot.deserializers.TextElementDeserializer
import com.lonelybot.slack.SlackTextElement
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.statement.*
import io.ktor.serialization.gson.*
import io.ktor.utils.io.*
import java.time.LocalDateTime

open class API {
    val json = GsonBuilder().setLenient()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
        .registerTypeAdapter(SlackTextElement::class.java, TextElementDeserializer())
        .setExclusionStrategies(object: ExclusionStrategy{
            override fun shouldSkipField(f: FieldAttributes?): Boolean {
                return f!!.declaredClass::class.java == ThreadLocal::class.java
            }

            override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                return clazz!!::class.java == ThreadLocal::class.java
            }
        })
        .create()
    val client = HttpClient(OkHttp) {
        engine {
            config {
                followRedirects(true)
            }
        }

        install(ResponseObserver) {
            onResponse {
                it.bodyAsChannel().readUTF8Line().let(::println)
                it.call.request.url.let(::println)
            }
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            gson{
                registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
                registerTypeAdapter(SlackTextElement::class.java, TextElementDeserializer())
            }
        }
    }
}