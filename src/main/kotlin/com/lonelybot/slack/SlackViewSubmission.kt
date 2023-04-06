package com.lonelybot.slack

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.jayway.jsonpath.JsonPath

class SlackViewSubmission(val view: JsonObject): SlackAction(null, null, null, null, null, null, null) {
    inline fun <reified T> getStateOf(vararg ids: String): T{
        val viewJson = Gson().toJson(view)
        return JsonPath.read(viewJson, "$.state.values.".plus(ids.joinToString(".")))
    }
    
    fun getExternalId(): String{
        val viewJson = Gson().toJson(view)
        return JsonPath.read(viewJson, "$.external_id")
    }
}