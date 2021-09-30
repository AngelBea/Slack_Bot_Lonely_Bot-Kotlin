package com.lonelybot.not

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import io.ktor.util.reflect.*
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.memberProperties

class NotionObjectParser<T: NotionDatatable>(val obj: JsonObject, val notionClass: KClass<T>) {
    inline fun <reified T> parseQueryObject(): List<T>{
        val listObjects: MutableList<MutableMap<String, Any>> = mutableListOf()

        val membersProps = notionClass.memberProperties.map { it.name }

        println("TRACE | parseQueryObject | classParameters $membersProps")

        obj.get("results").asJsonArray.forEach { page ->
            val property = page.asJsonObject.get("properties").asJsonObject

            val mapProperties = mutableMapOf<String, Any>()
            membersProps.forEach { classParameter ->
                mapProperties[classParameter] = property.get(classParameter.replaceFirstChar(Char::uppercase))
            }
            listObjects.add(mapProperties)
        }

        "List Objs $listObjects".let(::println)
        val type = object: TypeToken<List<T>>() {}.type

        return Gson().fromJson(Gson().toJson(listObjects), type)
    }
}