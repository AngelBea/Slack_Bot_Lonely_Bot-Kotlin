package com.lonelybot

enum class NotionTags(val tagName: String, val emote: String){
    RED_COLOUR("Roja", ":large_red_square:"),
    YELLOW_COLOUR("Amarilla", ":large_yellow_square:"),
    NONE("","")
}

enum class Permissions{
    TIMEREMAINING,
    CARDS,
    LONELYME
}