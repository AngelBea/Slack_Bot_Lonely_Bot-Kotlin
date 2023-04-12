package com.lonelybot.slack.builders

import com.google.gson.Gson
import com.lonelybot.slack.*
import java.sql.Time
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SlackBlockBuilder(builder: SlackBlockBuilder.() -> Unit) {
    val blocks: MutableList<SlackBlock> = mutableListOf()

    init {
        this.apply(builder)
    }

    fun addImage(url: String, altText: String = "", title: String? = null): SlackBlockBuilder {
        val titleOrNull = if (title != null) SlackText(title) else null
        val image = SlackImageBlock(url, altText, titleOrNull)
        blocks.add(image)
        return this
    }
    
    fun addTextSection(text: String, isTextMarkdown: Boolean = true): SlackBlockBuilder {
        val textElement = checkMarkdownText(text, isTextMarkdown)
        val sectionBlock = SlackTextSectionBlock(textElement)
        blocks.add(sectionBlock)
        return this
    }
    
    fun addMultiTextSection(texts: SlackBlockBuilder.() -> SlackBlockBuilder): SlackBlockBuilder {        
        val textElements = texts().blocks.filterIsInstance<SlackTextSectionBlock>()
            .map { it.text }
        blocks.add(SlackMultiSection(textElements))
        return this
    }
    
    fun addUserSelectionSection(text: String, placeholder: String, actionId: String, initialUser: String? = null, isTextMarkdown: Boolean = true): SlackBlockBuilder {
        val textElement = checkMarkdownText(text, isTextMarkdown)
        
        val selectUser = SlackUserSelect(SlackText(placeholder), actionId, initialUser)
        
        blocks.add(SlackAccessorySection(textElement, selectUser))
        
        return this
    }
    
    fun addConversationsSelectionSection(text: String, placeholder: String, actionId: String, isTextMarkdown: Boolean = true): SlackBlockBuilder {
        val textElement = checkMarkdownText(text, isTextMarkdown)
        
        val selectConversations = SlackMultiConversationSelect(SlackText(placeholder), actionId)
        
        blocks.add(SlackAccessorySection(textElement, selectConversations))
        
        return this
    }

    fun addConversationSelectionSection(text: String, placeholder: String, actionId: String, initialChannel: String? = null, isTextMarkdown: Boolean = true): SlackBlockBuilder {
        val textElement = checkMarkdownText(text, isTextMarkdown)

        val selectConversation = SlackConversationSelect(SlackText(placeholder), actionId, initialChannel)

        blocks.add(SlackAccessorySection(textElement, selectConversation))

        return this
    }
    
    fun addButtonSection(text: String, value: String, actionId: String, isTextMarkdown: Boolean = true): SlackBlockBuilder {
        val textElement = checkMarkdownText(text, isTextMarkdown)
        
        val button = SlackButton(SlackText(value), value, actionId)
        
        blocks.add(SlackAccessorySection(textElement, button))
        
        return this
    }
    
    fun addImageSection(text: String, imageUrl: String, altText: String, isTextMarkdown: Boolean = true): SlackBlockBuilder {
        val textElement = checkMarkdownText(text, isTextMarkdown)
        
        val image = SlackImage(imageUrl, altText)
        
        blocks.add(SlackAccessorySection(textElement, image))
        
        return this
    }    
    
    fun addStaticSelectSection(text: String, placeholder: String, actionId: String, vararg options: String , isTextMarkdown: Boolean = true): SlackBlockBuilder {
        val optionList = options.map {            
            SlackOption(SlackText(it), it)
        }.toMutableList()
        val staticSelect = SlackStaticSelect(SlackText(placeholder), optionList, actionId)
        
        val accessorySection = SlackAccessorySection(checkMarkdownText(text, isTextMarkdown), staticSelect)
        blocks.add(accessorySection)
        
        return this
    }

    fun addStaticSelectSection(text: String, placeholder: String, actionId: String, vararg optionsWithValue: Pair<String, String>, isTextMarkdown: Boolean = true): SlackBlockBuilder {
        val optionList = optionsWithValue.map {
            SlackOption(SlackText(it.first), it.second)
        }.toMutableList()
        val staticSelect = SlackStaticSelect(SlackText(placeholder), optionList, actionId)

        val accessorySection = SlackAccessorySection(checkMarkdownText(text, isTextMarkdown), staticSelect)
        blocks.add(accessorySection)
        
        return this
    }
    
    fun addOverflowSection(text: String, actionId: String, isTextMarkdown: Boolean = true, vararg options: String): SlackBlockBuilder {
        val optionList = options.map {            
            SlackOption(SlackText(it), it)
        }.toMutableList()
        val overflow = SlackOverflow(actionId, optionList)
        
        val accessorySection = SlackAccessorySection(checkMarkdownText(text, isTextMarkdown), overflow)
        blocks.add(accessorySection)
        
        return this
    }

    fun addOverflowSection(text: String, actionId: String, isTextMarkdown: Boolean = true, vararg optionsWithValue: Pair<String, String>): SlackBlockBuilder {
        val optionList = optionsWithValue.map {
            SlackOption(SlackText(it.first), it.second)
        }.toMutableList()
        val overflow = SlackOverflow(actionId, optionList)

        val accessorySection = SlackAccessorySection(checkMarkdownText(text, isTextMarkdown), overflow)
        blocks.add(accessorySection)
        
        return this
    }
    
    fun addRadioButtonsSection(text: String, actionId: String, isTextMarkdown: Boolean = true, vararg options: String): SlackBlockBuilder {
        val optionList = options.map {            
            SlackOption(SlackText(it), it)
        }.toMutableList()
        val radioButtons = SlackRadioButtons(optionList, actionId)
        
        val accessorySection = SlackAccessorySection(checkMarkdownText(text, isTextMarkdown), radioButtons)
        blocks.add(accessorySection)
        
        return this
    }

    fun addRadioButtonsSection(text: String, actionId: String, isTextMarkdown: Boolean = true, vararg optionsWithValue: Pair<String, String>): SlackBlockBuilder {
        val optionList = optionsWithValue.map {
            SlackOption(SlackText(it.first), it.second)
        }.toMutableList()
        val radioButtons = SlackRadioButtons(optionList, actionId)

        val accessorySection = SlackAccessorySection(checkMarkdownText(text, isTextMarkdown), radioButtons)
        blocks.add(accessorySection)
        
        return this
    }
    
    fun addDatePickerSection(text: String, actionId: String, placeholder: String, initialDate: LocalDateTime, isTextMarkdown: Boolean = true): SlackBlockBuilder {
        val datePicker = SlackDatePicker(SlackText(placeholder), actionId, initialDate.format(DateTimeFormatter.ISO_DATE))
        val accessorySection = SlackAccessorySection(checkMarkdownText(text, isTextMarkdown), datePicker)
        blocks.add(accessorySection)

        return this
    }
    
    fun addTimePickerSection(text: String, actionId: String, placeholder: String, initialTime: Time, isTextMarkdown: Boolean = true): SlackBlockBuilder {
        val timePicker = SlackTimePicker(
            SlackText(placeholder), actionId, initialTime.toLocalTime().format(
            DateTimeFormatter.ISO_LOCAL_TIME).take(5))
        val accessorySection = SlackAccessorySection(checkMarkdownText(text, isTextMarkdown), timePicker)
        blocks.add(accessorySection)
        
        return this
    }

    fun addCheckboxesSection(blockId: String? = null, text: String, actionId: String, isTextMarkdown: Boolean = true, vararg textAndDescriptions: Pair<String, String>): SlackBlockBuilder {
        val optionList = textAndDescriptions.map {
            SlackOption(SlackMarkdown(it.first), it.first, SlackText(it.second))
        }.toMutableList()
        val checkboxes = SlackCheckboxes(optionList, actionId)

        val accessorySection = SlackAccessorySection(checkMarkdownText(text, isTextMarkdown), checkboxes)
        
        blocks.add(accessorySection)

        return this
    }
    
    fun addDivider(): SlackBlockBuilder {
        blocks.add(SlackDivider())
        
        return this
    }
    
    fun addHeader(text: String): SlackBlockBuilder {
        blocks.add(SlackHeader(SlackText(text)))
        
        return this
    }
    
    fun addContext(contextBuilder: SlackContextBuilder.() -> Unit): SlackBlockBuilder {
        val context = SlackContextBuilder {}.apply(contextBuilder)
        
        blocks.add(context)
        
        return this
    }
    
    fun addPlainTextInput(multiline: Boolean, actionId: String, label: String): SlackBlockBuilder{
        val plainTextInput = PlainTextInputElement(multiline, actionId)
        val textLabel = SlackText(label)
        
        val input = SlackInput(plainTextInput, textLabel) 
        
        blocks.add(input)
        
        return this
    }
    
    fun toJson() = Gson().toJson(this)
    
    infix fun byId(blockId: String){
        blocks.last().block_id = blockId
    }
    
    private fun checkMarkdownText(text: String, isTextMarkdown: Boolean): SlackTextElement {
        return if (isTextMarkdown){
            SlackMarkdown(text)
        }else{
            SlackText(text)
        }
    }
}