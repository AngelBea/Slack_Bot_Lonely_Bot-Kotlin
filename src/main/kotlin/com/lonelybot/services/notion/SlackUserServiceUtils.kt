package com.lonelybot.services.notion

import com.lonelybot.Permissions
import com.lonelybot.YOU_WERE_NOT_REGISTERED
import com.lonelybot.adapters.SlackUserAdapter
import com.lonelybot.not.SlackUser
import com.lonelybot.services.slack.SlackChannelService
import com.lonelybot.services.slack.SlackUserService
import com.lonelybot.slack.Params
import com.lonelybot.slack.SlackAction
import com.lonelybot.slack.SlackApp
import com.lonelybot.slack.SlackEvent

suspend fun getCurrentUser(parameters: Params): SlackUserAdapter {
    var slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(parameters.user_id, parameters.team_id)
    if(slackUsers.isEmpty()) {
        val channel = SlackChannelService.openConversationByUser(parameters.user_id, true)
        NotionSlackUserService.createUser(parameters.user_id, parameters.team_id, parameters.user_name, channel.id)
        SlackApp.request.post.sendHiddenMessage(channel.id, YOU_WERE_NOT_REGISTERED, parameters.user_id)
        slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(parameters.user_id, parameters.team_id)
    }
    
    val slackUserInfo = SlackUserService.getUserById(parameters.user_id)
    
    return SlackUserAdapter(slackUsers[0], slackUserInfo)
}

suspend fun getCurrentUser(event: SlackEvent): SlackUserAdapter {
    var slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(event.event.user, event.teamId)
    val slackUserInfo = SlackUserService.getUserById(event.event.user)
    
    if(slackUsers.isEmpty()) {
        val channel = SlackChannelService.openConversationByUser(event.event.user, true)
        NotionSlackUserService.createUser(event.event.user, event.teamId, slackUserInfo.name , channel.id)
        SlackApp.request.post.sendHiddenMessage(channel.id, YOU_WERE_NOT_REGISTERED, event.event.user)
        slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(event.event.user, event.teamId)
    }
    
    return SlackUserAdapter(slackUsers[0], slackUserInfo)
}

suspend fun getCurrentUser(action: SlackAction): SlackUserAdapter {
    var slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(action.user!!.id, action.team!!.id)
    if(slackUsers.isEmpty()) {
        val channel = SlackChannelService.openConversationByUser(action.user!!.id, true)
        channel.id.let(::println)
        NotionSlackUserService.createUser(action.user.id, action.team.id, action.user.username , channel.id)
        SlackApp.request.post.sendHiddenMessage(channel.id, YOU_WERE_NOT_REGISTERED, action.user.id)
        slackUsers = NotionSlackUserService.getUserBySlackIdAndTeamId(action.user.id, action.team.id)
    }
    
    val slackUserInfo = SlackUserService.getUserById(action.user.id)

    return SlackUserAdapter(slackUsers[0], slackUserInfo)
}

fun SlackUser.isPermitted(permission: Permissions): Boolean = this.permissions.multiSelect.none { it.name == permission.name }.not()
fun SlackUserAdapter.isPermitted(permission: Permissions): Boolean = this.permissions.none { it == permission }.not()
