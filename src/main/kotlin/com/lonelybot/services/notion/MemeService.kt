package com.lonelybot.services.notion

import com.lonelybot.notion.NotionApp
import com.lonelybot.notion.builders.NotionPageBuilder
import com.lonelybot.notion.decorators.MemeDecorator
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import java.time.LocalDateTime

class MemeService {
    companion object: ICompanionService{
        override val databaseId: String = "8cfaddc8b0b94550aa203acf26ec8740"
            
        suspend fun createMeme(addedById: String, name: String, source: String, url: String, vararg tags: String): MemeDecorator{
            val builder = NotionPageBuilder.build(databaseId){
                addTitle("Name", name)
                addRelation("Added By", addedById)
                addSelect("Source", source)
                addRichText("Url", url)
                addMultiSelect("Tags", *tags)                
            }

            val response = NotionApp.request.post.insertPage(builder)
            return MemeDecorator.fromJson(response.bodyAsText())
        }
    }
}