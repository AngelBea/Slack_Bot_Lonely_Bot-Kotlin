package com.lonelybot.services.slack

import com.google.gson.Gson
import com.lonelybot.slack.SlackApp
import com.lonelybot.slack.SlackUserInfo
import io.ktor.client.statement.*
import io.ktor.utils.io.*

class SlackUserService {
    data class UserInfoResponse(val ok: Boolean, val user: SlackUserInfo)
    companion object{
        suspend fun getUserById(userId: String): SlackUserInfo {
            val response = SlackApp.request.get.getUserInfo(userId)
            
            return Gson().fromJson(response.bodyAsChannel().readUTF8Line(), UserInfoResponse::class.java).user
        }
    }
}