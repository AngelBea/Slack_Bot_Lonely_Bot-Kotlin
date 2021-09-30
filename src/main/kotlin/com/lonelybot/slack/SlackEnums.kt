package com.lonelybot.slack

enum class BlockType(val typeName: String){
    ACTIONS("action"),
    CONTEXT("context"),
    DIVIDER("divider"),
    FILE("file"),
    HEADER("header"),
    IMAGE("image"),
    INPUT("input"),
    SECTION("section")
}

enum class SlackCommands(val shortcut: String){
    CARD("/tarjeta")
}

enum class ElementType(val typeName: String){
    MARKDOWN("mrkdwn"),
    IMAGE("image"),
    TEXT("plain_text")
}