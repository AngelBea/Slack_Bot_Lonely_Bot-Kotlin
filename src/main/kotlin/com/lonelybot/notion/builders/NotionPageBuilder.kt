package com.lonelybot.notion.builders

import com.google.gson.Gson
import com.lonelybot.notion.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotionPageBuilder(databaseId: String? = null) {
    private val parent = if (databaseId != null) NotionParent(databaseId) else null
    val properties: MutableMap<String, NotionParameter> = mutableMapOf()
    private val id: String? = null
    val children: MutableList<Any> = mutableListOf()
    
    companion object{
        inline fun build (databaseId: String? = null, builder: NotionPageBuilder.() -> Unit): NotionPageBuilder {
            return NotionPageBuilder(databaseId).apply(builder)
        }
    }
    fun addTitle(field: String, value: String, annotation: NotionAnnotationsObject? = null, href: String? = null){
        val contentText = NotionContentObject(value, null)
        val notionTextObj = NotionTextObject(contentText, annotation, null, href)

        if (properties.containsKey(field)){
            val existingField = properties[field] as NotionTitle
            existingField.title.add(notionTextObj)
        }else{
            val textObjectList = mutableListOf(notionTextObj)
            properties[field] = NotionTitle(textObjectList)
        }
    }
    
    fun addRichText(field: String, value: String, annotation: NotionAnnotationsObject? = null, href: String? = null){
        val contentText = NotionContentObject(value, null)
        val notionTextObj = NotionTextObject(contentText, annotation, null, href)
        
        if (properties.containsKey(field)){
            val existingField = properties[field] as NotionRichText
            existingField.richText.add(notionTextObj)
        }else{
            val textObjectList = mutableListOf<RichTextType>(notionTextObj)
            properties[field] = NotionRichText(textObjectList)            
        }
    }
    
    fun addRichEquation(field: String, value: String, annotation: NotionAnnotationsObject? = null, href: String? = null){
        val expressionObject = NotionExpressionObject(value)
        val notionExpObj = NotionEquationObject(expressionObject, annotation, null, href)
        
        if (properties.containsKey(field)){
            val existingField = properties[field] as NotionRichText
            existingField.richText.add(notionExpObj)
        }else{
            val textObjectList = mutableListOf<RichTextType>(notionExpObj)
            properties[field] = NotionRichText(textObjectList)            
        }
    }
    
    fun addNumber(field: String, value: Int) {
        properties[field] = NotionNumber(value)  
    } 
    
    
    fun addSelect(field: String, value: String){
        val selectObj = NotionSelectObject(value)
        properties[field] = NotionSelect(selectObj)
    }

    fun addMultiSelect(field: String, vararg values: String){
        val selectionsList = values.map { MultiSelectObject(it) }
        if (properties.containsKey(field)){
            (properties[field] as NotionMultiSelect).multiSelect.addAll(selectionsList)
        }else{            
            properties[field] = NotionMultiSelect(selectionsList.toMutableList())
        }
    }
    
    fun addDate(field: String, start: LocalDateTime, end: LocalDateTime? = null, timeZone: String? = null){
        val dateObj = NotionDateObject(start.format(DateTimeFormatter.ISO_DATE_TIME), end?.format(DateTimeFormatter.ISO_DATE_TIME), timeZone)
        properties[field] = NotionDate(dateObj)
    }
    
    fun addRelation(field: String, pageId: String){
        val relationObject = NotionRelationObject(pageId)
        val relation = NotionRelation(listOf(relationObject))
        
        properties[field] = relation
    }
    
    fun addProperty(field: String, property: NotionParameter){
        properties[field] = property
    }
    
    
    fun toJson(): String{
        return Gson().toJson(this)
    }
}
