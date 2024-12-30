package com.lonelybot.slack

import com.lonelybot.services.notion.NotionSlackUserService
import com.lonelybot.services.slack.SlackOauthService
import com.lonelybot.singletons.UserSingleton
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.angelbea.application.notion.builders.NotionPageBuilder

fun Routing.oauthRoutes(){
    slackOauth()
}

fun Route.slackOauth(){
    get("/oauth") {
        if (call.request.queryParameters.contains("code")){
            val code = call.request.queryParameters["code"]!!
            val oauthResponse = SlackOauthService.getOauthResponse(code)
            val user = UserSingleton.getByKeyOrFetch(oauthResponse.authed_user.id)
            NotionSlackUserService.updateUser(user?.notionId!!, NotionPageBuilder.build {
                addRichText("UserTokenScope", oauthResponse.authed_user.access_token)
            })
            UserSingleton.refresh(user.slackId)
            call.respondText("Se ha actualizado con éxito.", status = HttpStatusCode.OK)
        }
    }
}