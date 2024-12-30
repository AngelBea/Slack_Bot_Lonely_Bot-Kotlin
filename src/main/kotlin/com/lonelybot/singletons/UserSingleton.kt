package com.lonelybot.singletons

import com.lonelybot.adapters.SlackUserAdapter
import com.lonelybot.notion.SlackUser
import com.lonelybot.services.global.getCurrentUser
import com.lonelybot.services.notion.NotionSlackUserService
import com.lonelybot.services.slack.SlackUserService

object UserSingleton {
    private val objectById: MutableMap<String, SlackUserAdapter> = mutableMapOf()
    
    fun getByKey(key: String) = objectById[key]

    suspend fun getByKeyOrFetch(key: String): SlackUserAdapter?{
        if (!objectById.containsKey(key)){
            val slackUser = SlackUserService.getUserById(key)
            val slackNotionAdapter = getCurrentUser(slackUser.id, slackUser.teamId, false)
            add(slackNotionAdapter)
        }

        return objectById[key]
    }

    fun remove(obj: SlackUserAdapter): Boolean {
        val removedItem = objectById.remove(obj.slackId)
        return removedItem != null
    }

    fun add(obj: SlackUserAdapter) {
        objectById[obj.slackId] = obj
    }
    
    fun isPresent(key: String) = objectById.containsKey(key)
    
    suspend fun refresh(vararg keys: String){
        val entriesToRefresh = this.objectById.filter { it.key in keys }
        if (entriesToRefresh.isNotEmpty()){
            val users = NotionSlackUserService.getUsersById(entriesToRefresh.values.first().slackTeam, entriesToRefresh.keys).sortedBy { it.slackId.richText.first().plainText }
            
            entriesToRefresh
                .map { it.value.divide().second }
                .sortedBy { it.id }
                .zip(users)
                .map { SlackUserAdapter(it.second, it.first) }
                .forEach { objectById[it.slackId] = it }
        }
    }
}