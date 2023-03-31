package com.lonelybot.slack

enum class BlockType(val typeName: String){
    ACTIONS("actions"),
    CONTEXT("context"),
    DIVIDER("divider"),
    FILE("file"),
    HEADER("header"),
    IMAGE("image"),
    INPUT("input"),
    SECTION("section")
}

enum class SlackCommands(val shortcut: String){
    CARD("/tarjeta"),
    TIME("/cuantoparahuir"),
    SUGGEST("/sugerirTarjeta")
}

enum class ElementType(val typeName: String){
    MARKDOWN("mrkdwn"),
    IMAGE("image"),
    TEXT("plain_text"),
    USERS_SELECT("users_select"),
    STATIC_SELECT("static_select"),
    MULTI_CONVERSATIONS_SELECT("multi_conversations_select"),
    BUTTON("button"),    
    OVERFLOW("overflow"),
    DATE_PICKER("datepicker"),
    CHECKBOXES("checkboxes"),
    RADIO_BUTTONS("radio_buttons"),
    TIMEPICKER("timepicker")
}

enum class ViewTypes(val typeName: String){
    APP_HOME("home"),
    MODAL("modal")
}