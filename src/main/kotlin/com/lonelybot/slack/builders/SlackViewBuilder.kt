package com.lonelybot.slack.builders

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.lonelybot.slack.SlackApp
import com.lonelybot.slack.SlackHomeView
import com.lonelybot.slack.SlackModalView
import com.lonelybot.slack.SlackView

class SlackViewBuilder(builder: SlackViewBuilder.() -> Unit) {
    @SerializedName("user_id") private lateinit var userId: String
    private lateinit var view: SlackView
    @SerializedName("trigger_id") private lateinit var triggerId: String
    @SerializedName("external_id") private lateinit var externalId: String
    
    
    init{
        this.apply(builder)
    }
    
    fun toUser(userId: String){
        this.userId = userId
    }
    
    fun home(blocks: SlackBlockBuilder.() -> Unit){
        if (!this::view.isInitialized){
            view = SlackHomeView(SlackBlockBuilder{}.apply(blocks).blocks)    
        }
    }
    
    fun modal(triggerId: String, builder: SlackModalBuilder.() -> Unit){
        if (!this::view.isInitialized){ 
            this.triggerId = triggerId
            view = SlackModalBuilder{}.apply(builder).build()
        }
    }
    
    infix fun byId(text: String): SlackViewBuilder{
        view.external_id = text
        return this
    }
    
    fun toJson() = Gson().toJson(this)
    
    suspend fun deploy(){
        when(this.view){
            is SlackHomeView -> {                
                SlackApp.request.post.publishView(this)
            }
            is SlackModalView -> {
                this.toJson().let(::println)
                SlackApp.request.post.openModal(this)
            }
        }
    }
    
    suspend fun update(externalId: String){
        when(this.view){
            is SlackHomeView -> {
                SlackApp.request.post.publishView(this)
            }
            is SlackModalView -> {
                this.externalId = externalId
                SlackApp.request.post.updateModal(this)
            }
        }
    }
}