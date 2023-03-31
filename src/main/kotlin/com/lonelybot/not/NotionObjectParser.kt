package com.lonelybot.not

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlin.reflect.KClass
import kotlin.reflect.full.*

class NotionObjectParser<T: NotionDatatable>(val obj: JsonObject? = null, val notionClass: KClass<T>) {
    inline fun <reified T> parseQueryObject(): List<T>{
        val listObjects: MutableList<MutableMap<String, Any>> = mutableListOf()
        
        val membersProps = notionClass.memberProperties.map { 
            it.name to it.findAnnotation<NotionField>()?.fieldValue}
        
        println("TRACE | parseQueryObject | classParameters $membersProps")
        
        obj!!.get("results").asJsonArray.forEach { page ->
            val mapProperties = mutableMapOf<String, Any>()
            
            val pageJsonObj = page.asJsonObject
            mapProperties["id"] = pageJsonObj.get("id")
            
            val property = pageJsonObj.get("properties").asJsonObject
            
            membersProps.forEach { classParameter ->
                "Processing parameter: ${classParameter.first}-${classParameter.second ?: ""}".let(::println)
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
            listObjects.add(mapProperties)
        }
        val type = object: TypeToken<List<T>>() {}.type

        return Gson().fromJson(Gson().toJson(listObjects), type)
    }
}