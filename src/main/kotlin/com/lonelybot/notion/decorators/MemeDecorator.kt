package com.lonelybot.notion.decorators

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lonelybot.notion.Meme
import me.angelbea.application.notion.NotionObjectParser

class MemeDecorator(meme: Meme) {
    val name by lazy { 
        meme.name.title.firstOrNull()?.plainText
    }
    
    val source by lazy { 
        meme.source.select.name
    }
    
    val url by lazy { 
        meme.url.richText.firstOrNull()?.plainText
    }
    
    val addedBy by lazy { 
        meme.addedBy?.relation?.map { it.id }
    }
    
    companion object{
        fun fromJson(responseJson: String): MemeDecorator{        
            val meme = NotionObjectParser()
                .parseObject<Meme>(Gson().fromJson(responseJson, JsonObject::class.java))
            
            return MemeDecorator(meme)        
        }        
    }
}