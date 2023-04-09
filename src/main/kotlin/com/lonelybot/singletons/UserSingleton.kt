package com.lonelybot.singletons

import com.lonelybot.adapters.SlackUserAdapter
import com.lonelybot.services.notion.NotionSlackUserService

object UserSingleton: IStatefulSingleton<SlackUserAdapter> {
    private val objectById: MutableMap<String, SlackUserAdapter> = mutableMapOf()
    
    override fun getByKey(key: String) = objectById[key]

    override fun remove(obj: SlackUserAdapter): Boolean {
        val removedItem = objectById.remove(obj.slackId)
        return removedItem != null
    }

    override fun add(obj: SlackUserAdapter) {
        objectById[obj.slackId] = obj
    }
    
    override fun isPresent(key: String) = objectById.containsKey(key)
    
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