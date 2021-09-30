package com.lonelybot

import com.google.gson.Gson
import com.google.gson.JsonObject

fun kObjectToJsonObject(obj: Any): JsonObject{
    return Gson().fromJson(Gson().toJson(obj), JsonObject::class.java)
}