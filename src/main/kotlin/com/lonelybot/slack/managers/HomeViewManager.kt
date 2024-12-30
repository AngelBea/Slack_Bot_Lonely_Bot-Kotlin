package com.lonelybot.slack.managers

import com.lonelybot.*
import com.lonelybot.services.global.getCurrentUser
import com.lonelybot.slack.SlackActionBlock
import com.lonelybot.slack.SlackBlockAction
import com.lonelybot.slack.factories.ViewFactory

class HomeViewManager(private val action: SlackBlockAction) {    
    suspend fun showView(){
        val currentUser = getCurrentUser(action)
        val view = when(action.getActionId()){
            VIEW_HOME_ACTION_ID_LONELYCARD -> ViewFactory.buildLonelyCardMenu(currentUser)
            VIEW_HOME_ACTION_ID_LONELYRUN -> ViewFactory.buildLonelyRunMenu(currentUser)
            VIEW_HOME_ACTION_ID_LONELYME -> ViewFactory.buildLonelyMeMenu(currentUser)
            VIEW_HOME_ACTION_ID_AUTH -> ViewFactory.buildOAuthMenu(currentUser)
            VIEW_HOME_ACTION_ID_CHANGELOG -> ViewFactory.buildHomeForUser(currentUser)
            else -> null
        }
        view?.deploy()
    }
    
}