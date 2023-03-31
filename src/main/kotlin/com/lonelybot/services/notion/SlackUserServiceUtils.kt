package com.lonelybot.services.notion

import com.lonelybot.Permissions
import com.lonelybot.YOU_WERE_NOT_REGISTERED
import com.lonelybot.not.SlackUser
import com.lonelybot.services.slack.SlackChannelService
import com.lonelybot.slack.Params
import com.lonelybot.slack.SlackAction
import com.lonelybot.slack.SlackApp
import com.lonelybot.slack.SlackEvent

suspend fun getCurrentUser(parameters: Params): SlackUser {
    var slackUsers = SlackUserService.getUserBySlackIdAndTeamId(parameters.user_id, parameters.team_id)
    if(slackUsers.isEmpty()) {
        val channel = SlackChannelService.openConversationByUser(parameters.user_id, true)
        SlackUserService.createUser(parameters.user_id, parameters.team_id, parameters.user_name, channel.id)
        SlackApp.request.post.sendHiddenMessage(channel.id, YOU_WERE_NOT_REGISTERED, parameters.user_id)
        slackUsers = SlackUserService.getUserBySlackIdAndTeamId(parameters.user_id, parameters.team_id)
    }
    
    return slackUsers[0]
}

suspend fun getCurrentUser(event: SlackEvent): SlackUser {
    var slackUsers = SlackUserService.getUserBySlackIdAndTeamId(event.event.user, event.teamId)
    if(slackUsers.isEmpty()) {
        val channel = SlackChannelService.openConversationByUser(event.event.user, true)
        SlackUserService.createUser(event.event.user, event.teamId, "" , channel.id)
        SlackApp.request.post.sendHiddenMessage(channel.id, YOU_WERE_NOT_REGISTERED, event.event.user)
        slackUsers = SlackUserService.getUserBySlackIdAndTeamId(event.event.user, event.teamId)
    }
    
    return slackUsers[0]
}

suspend fun getCurrentUser(action: SlackAction): SlackUser {
    var slackUsers = SlackUserService.getUserBySlackIdAndTeamId(action.user!!.id, action.team!!.id)
    if(slackUsers.isEmpty()) {
        val channel = SlackChannelService.openConversationByUser(action.user!!.id, true)
        channel.id.let(::println)
        SlackUserService.createUser(action.user.id, action.team.id, action.user.username , channel.id)
        SlackApp.request.post.sendHiddenMessage(channel.id, YOU_WERE_NOT_REGISTERED, action.user.id)
        slackUsers = SlackUserService.getUserBySlackIdAndTeamId(action.user.id, action.team.id)
    }

    return slackUsers[0]
}

fun SlackUser.isPermitted(permission: Permissions): Boolean = this.permissions.multiSelect.none { it.name == permission.name }.not()
