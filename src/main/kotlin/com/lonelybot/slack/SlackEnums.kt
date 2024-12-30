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
    CARD("/lonelycard"),
    TIME("/lonelyrun"),
    ME("/lonelyme")
}

enum class ElementType(val typeName: String){
    MARKDOWN("mrkdwn"),
    IMAGE("image"),
    TEXT("plain_text"),
    USERS_SELECT("users_select"),
    STATIC_SELECT("static_select"),
    MULTI_CONVERSATIONS_SELECT("multi_conversations_select"),
    CONVERSATIONS_SELECT("conversations_select"),
    BUTTON("button"),    
    OVERFLOW("overflow"),
    DATE_PICKER("datepicker"),
    CHECKBOXES("checkboxes"),
    RADIO_BUTTONS("radio_buttons"),
    TIMEPICKER("timepicker"),
    PLAIN_TEXT_INPUT("plain_text_input")
}

enum class ViewTypes(val typeName: String){
    APP_HOME("home"),
    MODAL("modal"),
    MESSAGE("message")
}

enum class SlackEmoji(val value: String){
    RED_SQUARE(":large_red_square:"),
    YELLOW_SQUARE(":large_yellow_square:"),
    RUNNER(":runner:"),
    KING(":person_with_crown:"),
    CURLED_PAPER(":page_with_curl:"),
    MONOCLE(":face_with_monocle:"),
    BLUE_CIRCLE(":large_blue_circle:"),
    RED_CIRCLE(":red_circle:"),
    BLACK_CIRCLE(":black_circle:"),
    ORANGE_CIRCLE(":large_orange_circle:"),
    ROBOT(":robot_face:"),
    ARROW_FORWARD(":arrow_forward:"),
    ARROW_BACKWARD(":arrow_backward:"),
    CALENDAR(":calendar:"),
    DISAPPOINTED(":disappointed:"),
    SMILE(":smile:"),
    FILE_CABINET(":file_cabinet:"),

}

enum class SlackStyleButton(val value: String){
    PRIMARY("primary"),
    DANGER("danger"),
    DEFAULT("default")    
}