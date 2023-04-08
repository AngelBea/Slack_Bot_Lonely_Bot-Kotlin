package com.lonelybot.services.slack

import com.google.gson.Gson
import com.lonelybot.adapters.SlackUserAdapter
import com.lonelybot.slack.Channel
import com.lonelybot.slack.SlackApp
import io.ktor.client.statement.*
import io.ktor.utils.io.*

class SlackChannelService {
    private data class UserConversationsResponse(val channels: List<Channel>): GenericResponse(false, null)
    private data class UserConversationResponse(val channel: Channel): GenericResponse(false, null)
    
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
        
        suspend fun kickUser(fromUser: SlackUserAdapter, toUser: SlackUserAdapter, channelId: String, talkative: Boolean = false){
            val response = SlackApp.request.post.kickUserFromChannel(toUser.slackId, channelId)
            val genericResponse = Gson().fromJson(response.bodyAsChannel().readUTF8Line(), GenericResponse::class.java)
            if (talkative){
                when(genericResponse.error){
                    "cant_kick_from_general" -> SlackApp.request.post.sendHiddenMessage(channelId, "No puedes echar a <@$toUser> de #general", fromUser.slackId)
                    null -> SlackApp.request.post.sendHiddenMessage(channelId, "<@${fromUser.slackId}> te ha echado de <@$channelId>.", toUser.slackId)
                }
            }
        }
    }
}