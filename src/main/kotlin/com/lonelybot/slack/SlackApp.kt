package com.lonelybot.slack

import com.google.gson.Gson
import com.lonelybot.*
import com.slack.api.bolt.App
import com.slack.api.bolt.jetty.SlackAppServer
import io.ktor.client.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*


object SlackApp{
    val app: App by lazy {
        App()
    }


    private lateinit var response: HttpResponse

    init {
        SlackAppServer(this.app).start()
    }

    object request{
        private val client: HttpClient = HttpClient {
            install(ResponseObserver) {
                onResponse {
                    println(it.content.readUTF8Line())
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

                    body = Gson().toJson(message)
                }
            }

            suspend fun sendBlockedMessage(channel: String, blocks: MutableList<Block>){
                return client.request{
                    headers {
                        append(HEADER_AUTH_NAME, "Bearer $BOT_TOKEN")
                        append(HEADER_CONTENT_TYPE_NAME, "application/json")
                    }

                    url(POST_MESSAGE_URL)
                    method = HttpMethod.Post

                    val message = Message(channel, "", blocks = blocks)

                    body = Gson().toJson(message)
                }
            }
        }
    }

}