package com.lonelybot

import com.google.gson.GsonBuilder
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
    val json = GsonBuilder().setLenient().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter()).create()
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
            }
        }
    }
}