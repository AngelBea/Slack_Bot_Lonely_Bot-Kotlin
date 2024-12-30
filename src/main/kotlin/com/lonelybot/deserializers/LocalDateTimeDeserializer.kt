package com.lonelybot.deserializers

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeTypeAdapter : JsonDeserializer<LocalDateTime>,
    JsonSerializer<LocalDateTime> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime {
        if (json !is JsonPrimitive) {
            throw JsonParseException("Not a JSON primitive")
        }
        val dateStr = json.asString
        return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME)
    }

    override fun serialize(
        value: LocalDateTime?,
        typeOfT: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(value?.toString() ?: "")
    }
}