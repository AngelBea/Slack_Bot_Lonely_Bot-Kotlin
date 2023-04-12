package com.lonelybot.services

import com.google.gson.Gson
import io.ktor.client.statement.*
import io.ktor.utils.io.*

suspend inline fun <reified T> HttpResponse.getBodyAs(): T{
    return Gson().fromJson(bodyAsChannel().readUTF8Line().toString(), T::class.java)
}