package com.lonelybot.services.notion

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lonelybot.Permissions
import com.lonelybot.not.*
import com.lonelybot.not.SlackUser
import io.ktor.client.statement.*
import io.ktor.utils.io.*

class NotionSlackUserService{
    companion object: CompanionService {
        override val databaseId: String = "97ca326680654d728548700af7418a72"

        suspend fun getUserBySlackIdAndTeamId(slackId: String, teamId: String): List<SlackUser> {
            val filter = NotionFilterBuilder.build {
                equals("SlackId", NotionTypes.TITLE, slackId)
                equals("SlackTeam", NotionTypes.TITLE, teamId)
            }

            val responseBody =
                NotionApp.request.post.queryDatabase(filter, NotionLogicalFilter.AND, databaseId).bodyAsChannel()
                    .readUTF8Line()
            responseBody.let(::println)
            return NotionObjectParser(Gson().fromJson(responseBody!!, JsonObject::class.java), SlackUser::class)
                .parseQueryObject()
        }
        
        suspend fun createUser(slackId: String, slackTeam: String, name: String, imChannel: String){            
            val slackUser = NotionPageBuilder.build(databaseId){
                addRichText("SlackId", slackId)
                addRichText("SlackTeam", slackTeam)
                addTitle("Name", name)
                addMultiSelect("Permissions", Permissions.CARDS.name, Permissions.TIMEREMAINING.name, Permissions.LONELYME.name)
                addNumber("RedCardsShown", 0)
                addNumber("YellowCardsShown", 0)
                addNumber("RedCardsReceived", 0)
                addNumber("YellowCardsReceived", 0)
                addRichText("SlackImChannel", imChannel)
            }
            
            val response = NotionApp.request.post.insertPage(slackUser)
        }

        suspend fun updateUser(id: String, builder: NotionPageBuilder){
            val response = NotionApp.request.post.updatePage(id, builder)
        }
    }
}