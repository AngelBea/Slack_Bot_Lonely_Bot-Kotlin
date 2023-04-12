package com.lonelybot.services.imgur

import com.lonelybot.imgur.ImgurApp
import com.lonelybot.imgur.ImgurToken
import com.lonelybot.singletons.ImgurTokenSingleton

class TokenService {
    companion object{
        suspend fun getToken(): ImgurToken{
            with(ImgurTokenSingleton){
                return if (!isPresent() || isExpired()) {
                    refresh()
                } else {
                    retrieve()!!
                }
            }
        }
    }
}