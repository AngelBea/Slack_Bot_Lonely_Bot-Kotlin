package com.lonelybot.slack

data class Params(val token: String, val team_id: String, val team_domain: String, val channel_id: String,
                  val channel_name: String, val user_id: String, val user_name: String, val command: String, val text: String,
                  val api_app_id: String, val is_enterprise_install: String, val response_url: String, val trigger_id: String)
data class Message(val channel: String, val text: String, val username: String = "Señor Feudal Mister Lonely", val blocks: MutableList<Block> = mutableListOf())

open class Block(val type: String, val block_id: String)
data class ImageBlock(val image_url: String, val alt_text: String, val title: Text): Block(type = BlockType.IMAGE.typeName, block_id = "id_$alt_text")
data class ContextBlock(val elements: MutableList<Element>): Block(type = BlockType.CONTEXT.typeName, block_id = "")

interface Element
data class Text(val type: String, val text: String) : Element
data class Image(val type: String, val image_url: String, val alt_text: String): Element