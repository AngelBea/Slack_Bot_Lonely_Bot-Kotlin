package com.lonelybot.slack.managers

import com.lonelybot.VIEW_HOME_ACTION_ID_CHANGELOG
import com.lonelybot.VIEW_HOME_ACTION_ID_LONELYCARD
import com.lonelybot.VIEW_HOME_ACTION_ID_LONELYME
import com.lonelybot.VIEW_HOME_ACTION_ID_LONELYRUN
import com.lonelybot.services.global.getCurrentUser
import com.lonelybot.slack.SlackActionBlock
import com.lonelybot.slack.SlackBlockAction
import com.lonelybot.slack.factories.ViewFactory

class HomeViewManager(private val action: SlackBlockAction) {    
    suspend fun showView(){
        val currentUser = getCurrentUser(action)
        when(action.getActionId()){            
            VIEW_HOME_ACTION_ID_LONELYCARD -> ViewFactory.buildLonelyCardMenu(currentUser)
            VIEW_HOME_ACTION_ID_LONELYRUN -> ViewFactory.buildLonelyRunMenu(currentUser)
            VIEW_HOME_ACTION_ID_LONELYME -> ViewFactory.buildLonelyMeMenu(currentUser)
            else -> ViewFactory.buildHomeForUser(currentUser)
        }.deploy()
    }
    
}