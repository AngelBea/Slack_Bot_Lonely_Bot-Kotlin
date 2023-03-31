package com.lonelybot.slack.builders

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.lonelybot.slack.SlackApp
import com.lonelybot.slack.SlackHomeView
import com.lonelybot.slack.SlackView

class SlackViewBuilder(builder: SlackViewBuilder.() -> Unit) {
    @SerializedName("user_id") private lateinit var userId: String
    private lateinit var view: SlackView
    
    init{
        this.apply(builder)
    }
    
    fun toUser(userId: String){
        this.userId = userId
    }
    
    fun home(blocks: SlackBlockBuilder.() -> Unit){
        if (this::view.isInitialized.not()){
            view = SlackHomeView(SlackBlockBuilder{}.apply(blocks).blocks)    
        }
    }
    
    fun toJson() = Gson().toJson(this)
    
    suspend fun deploy(){
        when(this.view){
            is SlackHomeView -> {
                this.toJson().let(::println)
                SlackApp.request.post.publishView(this)
            }
        }
    }
}