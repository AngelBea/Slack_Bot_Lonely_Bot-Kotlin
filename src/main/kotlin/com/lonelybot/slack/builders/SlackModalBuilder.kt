package com.lonelybot.slack.builders

import com.lonelybot.slack.SlackBlock
import com.lonelybot.slack.SlackModalView
import com.lonelybot.slack.SlackText

class SlackModalBuilder(builder: SlackModalBuilder.() -> Unit) {
    private lateinit var modalTitle: SlackText
    private var modalSubmit: SlackText? = null
    private lateinit var modalClose: SlackText
    private lateinit var modalBlocks: MutableList<SlackBlock>
    private lateinit var modalView: SlackModalView
    
    init {
        this.apply(builder)
    }
    fun title(text: String): SlackModalBuilder{
        modalTitle = SlackText(text)
        return this
    }
    
    fun submit(text: String): SlackModalBuilder{
        modalSubmit = SlackText(text)  
        return this
    }
    
    fun close(text: String): SlackModalBuilder{
        modalClose = SlackText(text)
        return this
    }    
    
    fun blocks(blocks: SlackBlockBuilder.() -> Unit): SlackModalBuilder{
        modalBlocks = SlackBlockBuilder{}.apply(blocks).blocks
        return this
    }
    
    fun build(): SlackModalView{
        modalTitle = if (this::modalTitle.isInitialized) modalTitle else SlackText("My App")
        modalClose = if (this::modalClose.isInitialized) modalClose else SlackText("Cancel") 
        modalBlocks = if (this::modalBlocks.isInitialized) modalBlocks else SlackBlockBuilder{ addTextSection("I'm a modal, look at me! :eyes:") }.blocks
        return SlackModalView(modalTitle, modalSubmit, modalClose, modalBlocks)
    }
}