package com.lonelybot.notion

enum class NotionLogicalFilter(val jsonValue: String) {
    OR("or"),
    AND("and")
}

enum class NotionTypes{
    TITLE,
    RICHT_TEXT,
    TEXT,
    URL,
    EMAIL,
    PHONE,
    NUMBER,
    CHECKBOX,
    SELECT,
    MULTI_SELECT,
    DATE,
    CREATED_TIME,
    LAST_EDITED_TIME,
    PEOPLE,
    CREATED_BY,
    LAST_EDITED_BY,
    FILES,
    RELATION,
    FORMULA,
    EQUATION
}

enum class NotionFields{
    PROPERTY,
    FILTER
}

enum class NotionAnnotationColors(val value: String) {
    BLUE("blue"),
    BLUE_BACKGROUND("blue_background"),
    BROWN("brown"),
    BROWN_BACKGROUND("brown_background"),
    DEFAULT("default"),
    GRAY("gray"),
    GRAY_BACKGROUND("gray_background"),
    GREEN("green"),
    GREEN_BACKGROUND("green_background"),
    ORANGE("orange"),
    ORANGE_BACKGROUND("orange_background"),
    PINK("pink"),
    PINK_BACKGROUND("pink_background"),
    PURPLE("purple"),
    PURPLE_BACKGROUND("purple_background"),
    RED("red"),
    RED_BACKGROUND("red_background"),
    YELLOW("yellow"),
    YELLOW_BACKGROUND("yellow_background")
}