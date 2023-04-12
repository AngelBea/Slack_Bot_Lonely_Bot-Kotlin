package com.lonelybot.singletons

import com.google.gson.Gson
import com.lonelybot.NotSerializable
import com.lonelybot.imgur.ImgurApp
import com.lonelybot.imgur.ImgurToken
import com.lonelybot.services.imgur.TokenService
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import org.slf4j.Logger
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ImgurTokenSingleton {
    private lateinit var imgurToken: ImgurToken    
    private lateinit var refreshedOn: LocalDateTime
    private lateinit var expiresOnDate: LocalDateTime
    
    fun isPresent(): Boolean = this::imgurToken.isInitialized
    
    fun isExpired(): Boolean = expiresOnDate.minusDays(1) < LocalDateTime.now()
    
    fun retrieve(): ImgurToken? = if (isPresent()) imgurToken else null        
    
    suspend fun refresh(): ImgurToken{
        "Refreshing Token".let(::println)
        imgurToken = Gson().fromJson(ImgurApp.request.post.getToken().bodyAsChannel().readUTF8Line().toString(), ImgurToken::class.java)
        recalculateExpiration()
        return imgurToken
    }
    
    private fun recalculateExpiration(){
        refreshedOn = LocalDateTime.now()
        expiresOnDate = refreshedOn.plusNanos(imgurToken.expiresIn)
    }
}