package com.lonelybot.slack

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.jayway.jsonpath.JsonPath

class SlackBlockAction(val view: JsonObject, val actions: JsonArray): SlackAction(null, null, null, null, null, null) {
    inline fun <reified T> getStateOf(vararg ids: String): T{
        val viewJson = Gson().toJson(view)
        return JsonPath.read(viewJson, "$.state.values.".plus(ids.joinToString("."))) 
    }
    
    fun getActionId(): String{
        val viewJson = Gson().toJson(actions)
        return JsonPath.read(viewJson, "$.[0].action_id")
    }
}