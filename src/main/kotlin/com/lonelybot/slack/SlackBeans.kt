package com.lonelybot.slack

import com.google.gson.annotations.SerializedName
import com.lonelybot.NotionTags

data class Params(val token: String, val team_id: String, val team_domain: String, val channel_id: String,
                  val channel_name: String, val user_id: String, val user_name: String, val command: String, val text: String,
                  val api_app_id: String, val is_enterprise_install: String, val response_url: String, val trigger_id: String)
data class Message(val channel: String, val text: String, val user: String? = null, val username: String = "Señor Feudal Mister Lonely", val blocks: MutableList<Block> = mutableListOf())

open class Block(val type: String, val block_id: String)
data class ImageBlock(val image_url: String, val alt_text: String, val title: Text): Block(type = BlockType.IMAGE.typeName, block_id = "id_$alt_text")
data class ContextBlock(val elements: MutableList<Element>): Block(type = BlockType.CONTEXT.typeName, block_id = "")

interface Element
data class Text(val type: String, val text: String) : Element
data class Image(val type: String, val image_url: String, val alt_text: String): Element

data class Card(val fromUser: String, val color: NotionTags, val toUser: String, val onChannel: String, val reason: String? = null)

data class SlackMessageAction(val type: String, val token: String, @SerializedName("action_ts") val actionTs: String, val team: Team, val user: User, 
                              val channel: Channel, @SerializedName("is_enterprise_install") val isEnterpriseInstall: Boolean, val enterprise: Any?, 
                              @SerializedName("callback_id") val callbackId: String, @SerializedName("trigger_id") val triggerId: String, 
                              @SerializedName("response_url") val responseUrl: String, @SerializedName("message_ts") val messageTs: String, val message: Message)

data class Team(val id: String, val domain: String)

data class User(val id: String, val username: String, @SerializedName("team_id") val teamId: String, val name: String)

data class Channel(val id: String, val name: String)