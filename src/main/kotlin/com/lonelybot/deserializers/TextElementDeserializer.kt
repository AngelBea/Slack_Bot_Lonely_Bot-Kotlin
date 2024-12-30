package com.lonelybot.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.lonelybot.slack.ElementType
import com.lonelybot.slack.SlackMarkdown
import com.lonelybot.slack.SlackText
import com.lonelybot.slack.SlackTextElement
import java.lang.reflect.Type

class TextElementDeserializer : JsonDeserializer<SlackTextElement> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): SlackTextElement {
        val jsonObj = json?.asJsonObject
        var element: SlackTextElement? = null
        if (jsonObj != null) {
            val type = jsonObj.get("type").asString
            when (type) {
                ElementType.TEXT.typeName -> {
                    element = context!!.deserialize<SlackText>(json, SlackText::class.java)
                }
                ElementType.MARKDOWN.typeName -> {
                    element = context!!.deserialize<SlackMarkdown>(json, SlackMarkdown::class.java)
                }
            }
        }

        return element!!
    }

}