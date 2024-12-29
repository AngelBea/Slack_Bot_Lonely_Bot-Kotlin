package com.lonelybot.slack

import com.lonelybot.slack.builders.SlackBlockBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SlackModule(module: SlackBlockBuilder.() -> Unit) {
    var blocks : List<SlackBlock>
    
    init {
        blocks = SlackBlockBuilder{}.apply(module).blocks
    }
}