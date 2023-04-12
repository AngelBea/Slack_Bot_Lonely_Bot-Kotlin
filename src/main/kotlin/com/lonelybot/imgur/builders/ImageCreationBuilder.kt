package com.lonelybot.imgur.builders

import com.lonelybot.imgur.ImageType
import com.lonelybot.imgur.MixedTypesOnImageCreation
import io.ktor.client.request.forms.*
import io.ktor.http.*

class ImageCreationBuilder(builder: ImageCreationBuilder.() -> Unit) {
    private val properties: MutableMap<String, String> = mutableMapOf()
    
    init {
        this.apply(builder)
    }
    
    fun image(url: String){
        if (properties.containsKey("video")) throw MixedTypesOnImageCreation()
        properties["image"] = url
    }
    
    fun type(imageType: ImageType){
        properties["type"] = imageType.value
    }
    
    fun album(albumHash: String){
        properties["album"] = albumHash
    }
    
    fun name(name: String){
        properties["name"] = name 
    }
    
    fun video(url: String){
        if (properties.containsKey("image")) throw MixedTypesOnImageCreation()
        properties["video"] = url
    }
    
    fun toKtorParams(): FormDataContent{
        return FormDataContent(Parameters.build { 
            properties.forEach { append(it.key, it.value) }
        })
    }
}