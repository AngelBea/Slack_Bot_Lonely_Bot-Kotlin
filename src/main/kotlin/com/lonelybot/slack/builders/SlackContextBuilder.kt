package com.lonelybot.slack.builders

import com.lonelybot.slack.*

class SlackContextBuilder(builder: SlackContextBuilder.() -> Unit): SlackBlock(BlockType.CONTEXT.typeName, null) {
    private val elements: MutableList<Element> = mutableListOf()
    
    init {
        this.apply(builder)
    }
    
    fun addImage(url: String, altText: String): SlackContextBuilder {
        elements.add(SlackImage(url, altText))
        
        return this
    }
    
    fun addText(text: String): SlackContextBuilder {
        elements.add(SlackText(text))
        
        return this
    }
    
    fun addMarkdownText(text: String): SlackContextBuilder {
        elements.add(SlackMarkdown(text))
        
        return this
    }
}