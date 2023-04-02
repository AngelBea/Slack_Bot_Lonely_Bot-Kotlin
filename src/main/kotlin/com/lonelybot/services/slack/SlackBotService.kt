package com.lonelybot.services.slack

import com.google.gson.Gson
import com.lonelybot.slack.SlackApp
import com.lonelybot.slack.SlackBot
import io.ktor.client.statement.*
import io.ktor.utils.io.*

class SlackBotService {
    private data class BotResponse(val ok: Boolean, val bot: SlackBot)
    
    companion object{
        suspend fun getBotById(botId: String): SlackBot {
            val response = SlackApp.request.get.getBotInfo(botId)
            return Gson().fromJson(response.bodyAsChannel().readUTF8Line(), BotResponse::class.java).bot
        }
    }
}