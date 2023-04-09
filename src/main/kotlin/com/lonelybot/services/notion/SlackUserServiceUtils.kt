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
import com.lonelybot.singletons.UserSingleton
import com.lonelybot.slack.*

suspend fun getCurrentUser(parameters: Params, message: Boolean = true): SlackUserAdapter {
    val userId = parameters.user_id
    if(UserSingleton.isPresent(userId)) return UserSingleton.getByKey(userId)!!
    
    var slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(userId, parameters.team_id)
    if(slackUsers.isEmpty()) {
        val channel = SlackChannelService.openConversationByUser(userId, true)
        NotionSlackUserService.createUser(userId, parameters.team_id, parameters.user_name, channel.id)
        if (message){
            SlackApp.request.post.sendHiddenMessage(channel.id, YOU_WERE_NOT_REGISTERED, userId)            
        }
        slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(userId, parameters.team_id)
    }
    
    val slackUserInfo = SlackUserService.getUserById(userId)
    val userAdapter = SlackUserAdapter(slackUsers[0], slackUserInfo)
    UserSingleton.add(userAdapter)
    
    return userAdapter
}

suspend fun getCurrentUser(event: SlackEvent, message: Boolean = true): SlackUserAdapter {
    val userId = event.event.user
    if(UserSingleton.isPresent(userId)) return UserSingleton.getByKey(userId)!!
    
    var slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(userId, event.teamId)
    val slackUserInfo = SlackUserService.getUserById(userId)
    
    if(slackUsers.isEmpty()) {
        val channel = SlackChannelService.openConversationByUser(userId, true)
        NotionSlackUserService.createUser(userId, event.teamId, slackUserInfo.name , channel.id)
        if (message){
            SlackApp.request.post.sendHiddenMessage(channel.id, YOU_WERE_NOT_REGISTERED, userId)            
        }
        slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(userId, event.teamId)
    }
    
    val userAdapter = SlackUserAdapter(slackUsers[0], slackUserInfo)
    UserSingleton.add(userAdapter)
    
    return userAdapter
}

suspend fun getCurrentUser(action: SlackAction, message: Boolean = true): SlackUserAdapter {
    val userId = action.user!!.id 
    if(UserSingleton.isPresent(userId)) return UserSingleton.getByKey(userId)!!
    
    var slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(userId, action.team!!.id)
    if(slackUsers.isEmpty()) {
        val channel = SlackChannelService.openConversationByUser(userId, true)
        NotionSlackUserService.createUser(userId, action.team.id, action.user.username , channel.id)
        if (message){
            SlackApp.request.post.sendHiddenMessage(channel.id, YOU_WERE_NOT_REGISTERED, action.user.id)            
        }
        slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(userId, action.team.id)
    }
    
    val slackUserInfo = SlackUserService.getUserById(userId)
    
    val userAdapter = SlackUserAdapter(slackUsers[0], slackUserInfo)    
    UserSingleton.add(userAdapter)
    
    return userAdapter 
}

suspend fun getCurrentUser(slackId: String, slackTeam: String, message: Boolean = true): SlackUserAdapter{
    if(UserSingleton.isPresent(slackId)) return UserSingleton.getByKey(slackId)!!
        
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

    val userAdapter = SlackUserAdapter(slackUsers[0], slackUserInfo)
    UserSingleton.add(userAdapter)
    
    return userAdapter
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
    
    UserSingleton.refresh(fromUser.slackId, toUser.slackId)
}

fun SlackUserAdapter.isPermitted(permission: Permissions): Boolean = this.permissions.none { it == permission }.not()
