package com.lonelybot.slack

import com.google.gson.Gson
import com.lonelybot.*
import com.lonelybot.slack.builders.SlackBlockBuilder
import com.lonelybot.slack.builders.SlackViewBuilder
import io.ktor.client.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*


object SlackApp{
    private lateinit var response: HttpResponse

    object request{
        private val client: HttpClient = HttpClient {
            install(ResponseObserver) {
                onResponse {
                    it.bodyAsChannel().readUTF8Line().let(::println)
                    response = it
                }
            }
        }

        object post {
            suspend fun sendTextMessage(channel: String, text: String): HttpResponse{
                return client.request{
                    headers {
                        append(HEADER_AUTH_NAME, "Bearer $BOT_TOKEN")
                        append(HEADER_CONTENT_TYPE_NAME, "application/json")
                    }

                    url(POST_MESSAGE_URL)
                    method = HttpMethod.Post

                    val message = Message(channel, text)

                    setBody(Gson().toJson(message))
                }
            }
            
            suspend fun sendHiddenMessage(channel: String, text: String, toUser: String): HttpResponse{
                return client.request{
                    headers {
                        append(HEADER_AUTH_NAME, "Bearer $BOT_TOKEN")
                        append(HEADER_CONTENT_TYPE_NAME, "application/json")
                    }

                    url(HIDDEN_MESSAGE_URL)
                    method = HttpMethod.Post

                    val message = Message(channel, text, user = toUser)

                    setBody(Gson().toJson(message))
                }
            }

            suspend fun sendBlockedMessage(channel: String, builder: SlackBlockBuilder): HttpResponse {
                return client.request{
                    headers {
                        append(HEADER_AUTH_NAME, "Bearer $BOT_TOKEN")
                        append(HEADER_CONTENT_TYPE_NAME, "application/json")
                    }

                    url(POST_MESSAGE_URL)
                    method = HttpMethod.Post

                    val message = Message(channel, "", blocks = builder.blocks)

                    setBody(Gson().toJson(message))
                }
            }
            
            suspend fun publishView(viewBuilder: SlackViewBuilder): HttpResponse {
                return client.request{                    
                    headers {
                        append(HEADER_AUTH_NAME, "Bearer $BOT_TOKEN")
                        append(HEADER_CONTENT_TYPE_NAME, "application/json")
                    }

                    url(PUBLISH_HOME_URL)
                    method = HttpMethod.Post

                    setBody(viewBuilder.toJson())
                }
            }
            
            suspend fun openModal(viewBuilder: SlackViewBuilder): HttpResponse {
                return client.request{                    
                    headers {
                        append(HEADER_AUTH_NAME, "Bearer $BOT_TOKEN")
                        append(HEADER_CONTENT_TYPE_NAME, "application/json")
                    }

                    url(OPEN_MODAL_URL)
                    method = HttpMethod.Post

                    setBody(viewBuilder.toJson())
                }
            }

            suspend fun updateModal(viewBuilder: SlackViewBuilder): HttpResponse {
                return client.request{
                    headers {
                        append(HEADER_AUTH_NAME, "Bearer $BOT_TOKEN")
                        append(HEADER_CONTENT_TYPE_NAME, "application/json")
                    }

                    url(UPDATE_VIEW_URL)
                    method = HttpMethod.Post

                    setBody(viewBuilder.toJson())
                }
            }

            suspend fun openUserConversation(userId: String? = null, returnIm: Boolean?): HttpResponse{
                return client.request{
                    headers {
                        append(HEADER_AUTH_NAME, "Bearer $BOT_TOKEN")
                        append(HEADER_CONTENT_TYPE_NAME, "application/json")
                    }

                    url(OPEN_CONVERSATION_URL)
                    method = HttpMethod.Post
                    val map = mapOf("return_im" to returnIm, "users" to userId)                    
                    setBody(Gson().toJson(map))
                }
            }
            suspend fun kickUserFromChannel(userId: String, channel: String): HttpResponse{
                return client.request{
                    headers {
                        append(HEADER_AUTH_NAME, "Bearer $BOT_TOKEN")
                        append(HEADER_CONTENT_TYPE_NAME, "application/json")
                    }

                    url(KICK_USER_URL)
                    method = HttpMethod.Post
                    val map = mapOf("channel" to channel, "user" to userId)                    
                    setBody(Gson().toJson(map))
                }
            }
        }
        
        object get{
            suspend fun getUserConversations(userId: String? = null, teamId: String? = null, vararg conversationTypes: String): HttpResponse{
                return client.request{
                    headers {
                        append(HEADER_AUTH_NAME, "Bearer $BOT_TOKEN")
                        append(HEADER_CONTENT_TYPE_NAME, "application/json")
                    }

                    url(USER_CONVERSATION_URL)
                    method = HttpMethod.Get
                    val map = mapOf("user" to userId, "team_id" to teamId, "types" to conversationTypes.joinToString(","))
                    Gson().toJson(map).let(::println)
                    setBody(Gson().toJson(map))
                }
            }
            suspend fun getUserInfo(userId: String): HttpResponse{
                return client.request{
                    headers {
                        append(HEADER_AUTH_NAME, "Bearer $BOT_TOKEN")
                        append(HEADER_CONTENT_TYPE_NAME, "application/x-www-form-urlencoded")
                    }
                    
                    url(USER_INFO_URL)
                    method = HttpMethod.Post
                    setBody(FormDataContent(
                        Parameters.build { 
                            append("user", userId)
                        }
                    ))
                }
            }
            
            suspend fun getBotInfo(botId: String): HttpResponse{
                return client.request{
                    headers {
                        append(HEADER_AUTH_NAME, "Bearer $BOT_TOKEN")
                        append(HEADER_CONTENT_TYPE_NAME, "application/x-www-form-urlencoded")
                    }
                    
                    url(BOT_INFO_URL)
                    method = HttpMethod.Post
                    setBody(FormDataContent(
                        Parameters.build { 
                            append("bot", botId)
                        }
                    ))
                }
            }

           
        }
    }

}