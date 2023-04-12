package com.lonelybot.notion

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlin.reflect.KClass
import kotlin.reflect.full.*

class NotionObjectParser<T: NotionDatatable>(val notionClass: KClass<T>, val obj: JsonObject? = null) {
    val membersProps = notionClass.memberProperties.map {
        it.name to it.findAnnotation<NotionField>()?.fieldValue}
    
    inline fun <reified T> parseQuery(): List<T>{
        val listObjects: MutableList<T> = mutableListOf()
        
        obj!!.get("results").asJsonArray.forEach { page ->
            val parsedObj = parseObject<T>(page.asJsonObject)
            listObjects.add(parsedObj)
        }

        return listObjects
    }
    
    inline fun <reified T> parseObject(page: JsonObject): T{
        val mapProperties = mutableMapOf<String, Any>()
        
        mapProperties["id"] = page.get("id")

        val property = page.get("properties").asJsonObject

        membersProps.forEach { classParameter ->
            try{
                if (classParameter.second == null){
                    mapProperties[classParameter.first] = property.get(classParameter.first)
                }else{
                    mapProperties[classParameter.first] = property.get(classParameter.second)
                }
            }catch(exc: Exception){
                "Ignored parameter because: ${exc.message}".let(::println)
            }
        }
        
        return Gson().fromJson(Gson().toJson(mapProperties), T::class.java)        
    }
}