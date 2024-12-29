package me.angelbea.application.notion

import com.google.gson.JsonObject
import com.lonelybot.notion.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class NotionObjectParser {
    inline fun <reified T: NotionPage> parseQuery(queryString: String): List<T>{
        val listObjects: MutableList<T> = mutableListOf()
        val obj = NotionApi.json.fromJson(queryString, JsonObject::class.java)

        obj!!.get("results").asJsonArray.forEach { page ->
            val parsedObj = parseObject<T>(page.asJsonObject)
            listObjects.add(parsedObj)
        }

        return listObjects
    }
    
    inline fun <reified T: NotionPage> parseObject(page: JsonObject): T{
        val membersProps = T::class.memberProperties.map {
            it.name to it.findAnnotation<NotionField>()?.fieldValue}

        val mapProperties = mutableMapOf<String, Any>()
        
        mapProperties["id"] = page.get("id")
        mapProperties["created_time"] = page.getAsJsonPrimitive("created_time")
        mapProperties["last_edited_time"] = page.getAsJsonPrimitive("last_edited_time")
        mapProperties["cover"] = page.get("cover")
        mapProperties["icon"] = page.get("icon")

        val property = page.get("properties").asJsonObject

        membersProps.forEach { classParameter ->
            try{
                if (classParameter.second == null){
                    mapProperties[classParameter.first] = property.get(classParameter.first)
                }else{
                    mapProperties[classParameter.first] = property.get(classParameter.second)
                }
            }catch(exc: Exception){
                "Ignored parameter ${classParameter.first} because: ${exc.message}".let(::println)
            }
        }
        
        return NotionApi.json.fromJson(NotionApi.json.toJson(mapProperties), T::class.java)
    }

    companion object{
        fun parseBlocks(blockString: String): List<PageBlock>{
            val typeClassMap = mapOf(
                "paragraph" to NotionParagraphBlock::class.java,
                "heading_3" to NotionThirdHeadingBlock::class.java,
                "heading_2" to NotionSecondHeadingBlock::class.java,
                "heading_1" to NotionFirstHeadingBlock::class.java,
                "image" to NotionImageBlock::class.java,
                "divider" to NotionDivider::class.java
            )

            val blockList = mutableListOf<PageBlock>()
            val obj = NotionApi.json.fromJson(blockString, JsonObject::class.java)
            obj.get("results").asJsonArray.forEach {block ->
                val blockObj = block.asJsonObject
                val type = blockObj.get("type").asJsonPrimitive.asString
                try{
                    val mapByType = mapOf(type to blockObj.get(type))
                    val blockJson = NotionApi.json.toJson(mapByType)
                    val notionBlock = NotionApi.json.fromJson(blockJson, typeClassMap[type])
                    blockList.add(notionBlock)
                }catch (exc: Exception){
                    exc.let(::println)
                    "The block with type $type couldn't be converted maybe isn't implemented yet.".let(::println)
                }
            }

            return blockList
        }
    }
}