package com.lonelybot.adapters

import com.lonelybot.Permissions
import com.lonelybot.notion.*
import com.lonelybot.slack.SlackUserInfo

class SlackUserAdapter(private val notionSlackUser: SlackUser, private val userInfo: SlackUserInfo) {
    val redCardsShown: Int by lazy{
        notionSlackUser.redCardsShown.number
    }
    val redCardsReceived: Int by lazy { 
        notionSlackUser.redCardsReceived.number
    }
    
    val yellowCardsShown: Int by lazy{
        notionSlackUser.yellowCardsShown.number
    }
    val yellowCardsReceived: Int by lazy { 
        notionSlackUser.yellowCardsReceived.number
    }
    
    val permissions: List<Permissions> by lazy { 
        Permissions.values().filter { permission -> permission.name in notionSlackUser.permissions.multiSelect.map { it.name } }
    }
    val leavingOnFriday: String? by lazy { 
        notionSlackUser.leavingOnFriday?.richText?.firstOrNull()?.plainText
    }
    val leavingRestOfWeek: String? by lazy{
        notionSlackUser.leavingRestOfWeek.richText.firstOrNull()?.plainText
    }
    val slackImChannel: String? by lazy { 
        notionSlackUser.slackImChannel.richText.firstOrNull()?.plainText
    }
    
    val slackId: String by lazy{
        userInfo.id
    }
    
    val slackTeam: String by lazy{
        userInfo.teamId
    }
    
    val slackUserName by lazy{
        userInfo.name
    }
    val profileImgFull by lazy { 
        userInfo.profile.image512
    }
    
    val profileImgMedium by lazy { 
        userInfo.profile.image192
    }

    val profileImageSmall by lazy {
        userInfo.profile.image72
    }
    
    val notionId by lazy{
        notionSlackUser.id
    }
    
    fun divide() = notionSlackUser to userInfo
}