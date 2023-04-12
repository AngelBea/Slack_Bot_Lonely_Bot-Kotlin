package com.lonelybot.notion.builders

import com.lonelybot.notion.NotionLogicalFilter

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