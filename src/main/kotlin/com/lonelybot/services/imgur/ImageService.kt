package com.lonelybot.services.imgur

import com.google.gson.annotations.SerializedName
import com.lonelybot.imgur.ImageType
import com.lonelybot.imgur.ImgurApp
import com.lonelybot.imgur.builders.ImageCreationBuilder
import com.lonelybot.services.getBodyAs

class ImageService {
    data class ImgurResponse(val data: ImgurImageData, val success: Boolean, val status: Int)
    data class ImgurImageData(val id: String, val title: String?, val description: String?, val datetime: Long,
                              val type: String, val animated: Boolean, val width: Int,
                              val height: Int, val size: Int, val views: Int, val bandwidth: Int,
                              val vote: String?, val favorite: Boolean, val nsfw: Boolean?,
                              val section: String?, @SerializedName("account_url") val accountUrl: String?, @SerializedName("account_id") val accountId: Int,
                              @SerializedName("is_ad") val isAd: Boolean, @SerializedName("in_most_viral") val inMostViral: Boolean, @SerializedName("has_sound") val hasSound: Boolean,
                              val tags: List<String>, @SerializedName("ad_type") val adType: Int, @SerializedName("ad_url") val adUrl: String,
                              val edited: String, @SerializedName("in_gallery") val inGallery: Boolean, val deletehash: String,
                              val name: String, val link: String, val mp4: String, val gifv: String, val hls: String,
                              @SerializedName("mp4_size") val mp4Size: Int, val looping: Boolean)
    companion object{
        suspend fun createImage(url: String, album: String?, type: ImageType): ImgurImageData {
            val builder = ImageCreationBuilder{
                image(url)
                if (album != null) album(album)
                type(type)
            }
            
            val imgurResponse = ImgurApp.request.post.createImage(builder).getBodyAs<ImgurResponse>()
            
            return imgurResponse.data
        }
    }
}