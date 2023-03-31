package com.lonelybot.services.slack

import com.google.gson.Gson
import com.lonelybot.slack.Channel
import com.lonelybot.slack.SlackApp
import io.ktor.client.statement.*
import io.ktor.utils.io.*

class SlackChannelService {
    private data class UserConversationsResponse(val ok: Boolean, val channels: List<Channel>)
    private data class UserConversationResponse(val ok: Boolean, val channel: Channel)
    
    companion object{
        suspend fun getImChannelByUserIdAndTeam(userId: String, teamId: String): Channel{
            val response = SlackApp.request.get.getUserConversations(userId, teamId, "im", "mpim")
            val channelResponse = Gson().fromJson(response.bodyAsChannel().readUTF8Line(), UserConversationsResponse::class.java)
            return channelResponse.channels[0]
        }      
        
        suspend fun openConversationByUser(userId: String, returnIm: Boolean? = null): Channel {
            val response = SlackApp.request.post.openUserConversation(userId, returnIm)
            val channelResponse = Gson().fromJson(response.bodyAsChannel().readUTF8Line(), UserConversationResponse::class.java)
            return channelResponse.channel
        }
    }
}