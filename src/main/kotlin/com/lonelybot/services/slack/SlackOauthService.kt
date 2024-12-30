package com.lonelybot.services.slack

import com.lonelybot.services.getBodyAs
import com.lonelybot.slack.SlackApp
import io.ktor.client.statement.*
import io.ktor.utils.io.*

class SlackOauthService {
    data class SlackAuthResponse(
        val ok: Boolean,
        val access_token: String,
        val token_type: String,
        val scope: String,
        val bot_user_id: String,
        val app_id: String,
        val team: Team,
        val enterprise: Enterprise,
        val authed_user: AuthedUser
    )

    data class Team(
        val name: String,
        val id: String
    )

    data class Enterprise(
        val name: String,
        val id: String
    )

    data class AuthedUser(
        val id: String,
        val scope: String,
        val access_token: String,
        val token_type: String
    )

    companion object{
        suspend fun getOauthResponse(code: String): SlackAuthResponse{
            val response = SlackApp.request.post.getOauthUserCode(code)
            return response.getBodyAs()
        }
    }
}