package com.lonelybot.not

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lonelybot.kObjectToJsonObject

class NotionFilter(builder: NotionFilter.() -> Unit) {
    private lateinit var filter: MutableMap<String, List<MutableMap<String, Any>>>
    
    init {
        this.apply(builder)
    }
    
    fun and(builder: NotionFilterBuilder.() -> Unit){
        if (!this::filter.isInitialized){
            filter = mutableMapOf()
            filter[NotionLogicalFilter.AND.jsonValue] = NotionFilterBuilder().apply(builder).filters    
        }        
    }
    
    fun or(builder: NotionFilterBuilder.() -> Unit){
        if (!this::filter.isInitialized){
            filter = mutableMapOf()            
            filter[NotionLogicalFilter.OR.jsonValue] = NotionFilterBuilder().apply(builder).filters
        }
    }
    
    fun retrieve(): Map<String, List<MutableMap<String, Any>>>{
        return filter.toMap()
    }
}