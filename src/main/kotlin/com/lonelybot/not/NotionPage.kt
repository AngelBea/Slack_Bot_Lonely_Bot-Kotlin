package com.lonelybot.not

import com.google.gson.Gson
import com.lonelybot.SOURCE_TEST_VALUE

class NotionPage(val databaseId: String?) {
    private val parent = if (databaseId != null) NotionParent(databaseId) else null
    private val properties: MutableMap<String, NotionParameter> = mutableMapOf()

    companion object{
        inline fun build (databaseId: String?, builder: NotionPage.() -> Unit): NotionPage{
            return NotionPage(databaseId).apply(builder)
        }
    }

    fun addRichText(field: String, value: String){
        val contentText = NotionContentObject(value, NotionLinkObject(""))
        val textObjectList = listOf(NotionTextObject(NotionTypes.TEXT.name.lowercase(), contentText, null, null, null))
        properties[field] = NotionRichText(textObjectList)
    }

    fun addSelect(field: String, value: String){
        val selectObj = NotionSelectObject(value)
        properties[field] = NotionSelect(selectObj)
    }

    fun addMultiSelect(field: String, value: String){
        val multiSelectObject = MultiSelectObject(value)

    }

    fun toJson(): String{
        return Gson().toJson(this)
    }

}
