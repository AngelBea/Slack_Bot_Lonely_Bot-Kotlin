package com.lonelybot

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jayway.jsonpath.JsonPath
import com.lonelybot.slack.Params
import io.ktor.http.*

fun kObjectToJsonObject(obj: Any): JsonObject{
    return Gson().fromJson(Gson().toJson(obj), JsonObject::class.java)
}

fun processParameters(parameters: Parameters): Params {
    val mapParams = mutableMapOf<String, String>()

    parameters.entries().forEach { mapParams[it.key] = it.value.first() }

    val paramsJson = Gson().toJson(mapParams)

    return Gson().fromJson(paramsJson, Params::class.java)
}



