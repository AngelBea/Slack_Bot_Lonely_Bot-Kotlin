package com.lonelybot.slack.managers

import com.lonelybot.services.global.getCurrentUser
import com.lonelybot.slack.SlackEvent
import com.lonelybot.slack.factories.ViewFactory

class HomeAppEventManager(private val event: SlackEvent) {
    suspend fun dispatch(){
        when(event.event.tab){
            "home" -> event.returnHomeForUser()
            "messages" -> {}
        }
    }

    private suspend fun SlackEvent.returnHomeForUser(){
        val slackUser = getCurrentUser(this)

        val home = ViewFactory.buildHomeForUser(slackUser)
        home.deploy()
    }
}