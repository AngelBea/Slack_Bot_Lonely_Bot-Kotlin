package com.lonelybot.services.notion

import com.lonelybot.NotionTags
import com.lonelybot.Permissions
import com.lonelybot.YOU_WERE_NOT_REGISTERED
import com.lonelybot.adapters.SlackUserAdapter
import com.lonelybot.not.NotionApp
import com.lonelybot.not.NotionPageBuilder
import com.lonelybot.not.SlackUser
import com.lonelybot.services.slack.SlackChannelService
import com.lonelybot.services.slack.SlackUserService
import com.lonelybot.slack.Params
import com.lonelybot.slack.SlackAction
import com.lonelybot.slack.SlackApp
import com.lonelybot.slack.SlackEvent

suspend fun getCurrentUser(parameters: Params, message: Boolean = true): SlackUserAdapter {
    var slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(parameters.user_id, parameters.team_id)
    if(slackUsers.isEmpty()) {
        val channel = SlackChannelService.openConversationByUser(parameters.user_id, true)
        NotionSlackUserService.createUser(parameters.user_id, parameters.team_id, parameters.user_name, channel.id)
        if (message){
            SlackApp.request.post.sendHiddenMessage(channel.id, YOU_WERE_NOT_REGISTERED, parameters.user_id)            
        }
        slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(parameters.user_id, parameters.team_id)
    }
    
    val slackUserInfo = SlackUserService.getUserById(parameters.user_id)
    
    return SlackUserAdapter(slackUsers[0], slackUserInfo)
}

suspend fun getCurrentUser(event: SlackEvent, message: Boolean = true): SlackUserAdapter {
    var slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(event.event.user, event.teamId)
    val slackUserInfo = SlackUserService.getUserById(event.event.user)
    
    if(slackUsers.isEmpty()) {
        val channel = SlackChannelService.openConversationByUser(event.event.user, true)
        NotionSlackUserService.createUser(event.event.user, event.teamId, slackUserInfo.name , channel.id)
        if (message){
            SlackApp.request.post.sendHiddenMessage(channel.id, YOU_WERE_NOT_REGISTERED, event.event.user)            
        }
        slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(event.event.user, event.teamId)
    }
    
    return SlackUserAdapter(slackUsers[0], slackUserInfo)
}

suspend fun getCurrentUser(action: SlackAction, message: Boolean = true): SlackUserAdapter {
    var slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(action.user!!.id, action.team!!.id)
    if(slackUsers.isEmpty()) {
        val channel = SlackChannelService.openConversationByUser(action.user.id, true)
        NotionSlackUserService.createUser(action.user.id, action.team.id, action.user.username , channel.id)
        if (message){
            SlackApp.request.post.sendHiddenMessage(channel.id, YOU_WERE_NOT_REGISTERED, action.user.id)            
        }
        slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(action.user.id, action.team.id)
    }
    
    val slackUserInfo = SlackUserService.getUserById(action.user.id)

    return SlackUserAdapter(slackUsers[0], slackUserInfo)
}

suspend fun getCurrentUser(slackId: String, slackTeam: String, message: Boolean = true): SlackUserAdapter{
    var slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(slackId, slackTeam)
    val slackUserInfo = SlackUserService.getUserById(slackId)
    
    if (slackUserInfo.isBot) throw BadUserException()
    
    if(slackUsers.isEmpty()) {
        val channel = SlackChannelService.openConversationByUser(slackId, true)
        NotionSlackUserService.createUser(slackId, slackTeam, slackUserInfo.name, channel.id)
        if (message){
            SlackApp.request.post.sendHiddenMessage(channel.id, YOU_WERE_NOT_REGISTERED, slackId)            
        }
        slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(slackId, slackTeam)
    }

    return SlackUserAdapter(slackUsers[0], slackUserInfo)
}

suspend fun updateCardStatsForUsers(fromUser: SlackUserAdapter, cardName: String, toUser: SlackUserAdapter){
    when(cardName){
        NotionTags.YELLOW_COLOUR.tagName -> {
            val builderFromUser = NotionPageBuilder.build { 
                addNumber("YellowCardsShown", fromUser.yellowCardsShown.plus(1))
            }
            NotionApp.request.post.updatePage(fromUser.notionId!!, builderFromUser)
            
            val builderToUser = NotionPageBuilder.build { 
                addNumber("YellowCardsReceived", toUser.yellowCardsReceived.plus(1))
            }
            
            NotionApp.request.post.updatePage(toUser.notionId!!, builderToUser)
        }
        NotionTags.RED_COLOUR.tagName -> {
            val builderFromUser = NotionPageBuilder.build {
                addNumber("RedCardsShown", fromUser.redCardsShown.plus(1))
            }
            NotionApp.request.post.updatePage(fromUser.notionId!!, builderFromUser)

            val builderToUser = NotionPageBuilder.build {
                addNumber("RedCardsReceived", toUser.redCardsReceived.plus(1))
            }

            NotionApp.request.post.updatePage(toUser.notionId!!, builderToUser)
        }
    }
}

fun SlackUserAdapter.isPermitted(permission: Permissions): Boolean = this.permissions.none { it == permission }.not()
