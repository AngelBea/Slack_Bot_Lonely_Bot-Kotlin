package com.lonelybot.imgur

import com.lonelybot.*
import com.lonelybot.imgur.builders.ImageCreationBuilder
import com.lonelybot.services.imgur.TokenService
import com.lonelybot.singletons.ImgurTokenSingleton
import io.ktor.client.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*

object ImgurApp {
    private lateinit var response: HttpResponse

    object request {
        private val client: HttpClient = HttpClient {
            install(ResponseObserver) {
                onResponse {
                    it.bodyAsChannel().readUTF8Line().let(::println)
                    response = it
                }
            }
        }
        
        object post{
            suspend fun getToken(): HttpResponse {
                return client.request{
                    headers { 
                        append(HEADER_CONTENT_TYPE_NAME, "multipart/form-data")
                    }
                    url(IMGUR_TOKEN_URL)
                    method = HttpMethod.Post
                    
                    setBody(FormDataContent(
                        Parameters.build { 
                            append("refresh_token", IMGUR_REFRESH_TOKEN)
                            append("client_id", IMGUR_CLIENT_ID)
                            append("client_secret", IMGUR_CLIENT_SECRET)
                            append("grant_type", "refresh_token")
                        }
                    ))
                }
            }
            suspend fun createImage(builder: ImageCreationBuilder): HttpResponse {
                return client.request{
                    val token = TokenService.getToken()
                    "Token -> $token".let(::println)
                    headers { 
                        append(HEADER_CONTENT_TYPE_NAME, "multipart/form-data")
                        append(HEADER_AUTH_NAME, "Bearer ${token.accessToken}")
                    }
                    url(IMGUR_CREATE_IMG_URL)
                    method = HttpMethod.Post
                    
                    setBody(builder.toKtorParams())
                }
            }
        }
    }
}