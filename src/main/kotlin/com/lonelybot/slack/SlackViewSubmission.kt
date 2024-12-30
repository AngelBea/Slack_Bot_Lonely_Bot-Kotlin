package com.lonelybot.slack

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.jayway.jsonpath.JsonPath
import com.lonelybot.API
import com.lonelybot.deserializers.LocalDateTimeTypeAdapter
import com.lonelybot.deserializers.TextElementDeserializer
import java.time.LocalDateTime

class SlackViewSubmission(val view: JsonObject): SlackAction(null, null, null, null, null, null, null) {


    inline fun <reified T> getStateOf(vararg ids: String): T{
        val json: Gson = API().json
        val viewJson = json.toJson(view)
        return JsonPath.read(viewJson, "$.state.values.".plus(ids.joinToString(".")))
    }
    
    fun getExternalId(): String{
        val json: Gson = API().json
        val viewJson = json.toJson(view)
        return JsonPath.read(viewJson, "$.external_id")
    }

    inline fun <reified T: SlackBlock> getBlockIdAs(id: String): T{
        val json: Gson = API().json
        val blocks = view.get("blocks").asJsonArray
        val blockJsonObj = blocks.first { it.asJsonObject.get("block_id").asString == id }.asJsonObject
        val blockJsonString = json.toJson(blockJsonObj)
        val block = json.fromJson(blockJsonString, T::class.java)

        return block
    }
}