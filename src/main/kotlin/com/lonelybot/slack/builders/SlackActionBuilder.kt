package com.lonelybot.slack.builders

import com.lonelybot.slack.*

class SlackActionBuilder(builder: SlackActionBuilder.() -> Unit) {
    private val elements: MutableList<Element> = mutableListOf() 
    
    init {
        this.apply(builder)
    }
    
    fun button(name: String, actionId: String, emoji: SlackEmoji?, style: SlackStyleButton? = null){
        val text = if (emoji != null) SlackText("""${emoji.value} $name""") else SlackText(name)
        val button = SlackButton(text, name, actionId, style?.value)
        
        elements.add(button)
    }
    
    fun retrieve() = elements
}