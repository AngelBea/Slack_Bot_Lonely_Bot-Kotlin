package com.lonelybot.not

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lonelybot.kObjectToJsonObject

class NotionFilter(val logicalFilter: NotionLogicalFilter?, val notionFilterBuilder:  NotionFilterBuilder) {
    val filterObject: JsonObject

    init{
        filterObject = when(logicalFilter){
            NotionLogicalFilter.OR -> constructJsonObject(logicalFilter, notionFilterBuilder)
            NotionLogicalFilter.AND -> constructJsonObject(logicalFilter, notionFilterBuilder)
            null -> throw NotAFilterException()
        }
    }

    private fun constructJsonObject(logicalFilter: NotionLogicalFilter, notionFilterBuilder: NotionFilterBuilder): JsonObject{
        val rootObj = JsonObject()
        val filterObj = JsonObject()
        val orArray = JsonArray()
        println("Filters ${notionFilterBuilder.filters}")

        notionFilterBuilder.filters.forEach { map ->
            val propertyObject = JsonObject()
            propertyObject.addProperty(NotionFields.PROPERTY.name.lowercase(), map[NotionFields.PROPERTY.name.lowercase()] as String)
            map.remove(NotionFields.PROPERTY.name.lowercase())

            var condition = map.firstNotNullOf { entry -> entry }
            propertyObject.add(condition.key, kObjectToJsonObject(condition.value))
            orArray.add(propertyObject)
        }

        filterObj.add(logicalFilter.jsonValue, orArray)
        rootObj.add(NotionFields.FILTER.name.lowercase(), filterObj)

        return rootObj
    }
}