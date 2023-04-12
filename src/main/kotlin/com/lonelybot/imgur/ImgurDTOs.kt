package com.lonelybot.imgur

import com.google.gson.annotations.SerializedName
import com.lonelybot.NotSerializable
import java.time.LocalDateTime

data class ImgurToken(@SerializedName("access_token") val accessToken: String, @SerializedName("expires_in") val expiresIn: Long, @SerializedName("token_type") val tokenType: String,
                      val scope: String?, @SerializedName("refresh_token") val refreshToken: String, @SerializedName("account_id") val accountId: Long,
                      @SerializedName("account_username") val accountUsername: String )
