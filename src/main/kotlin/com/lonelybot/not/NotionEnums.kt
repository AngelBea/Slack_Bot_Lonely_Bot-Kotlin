package com.lonelybot.not

enum class NotionLogicalFilter(val jsonValue: String) {
    OR("or"),
    AND("and")
}

enum class NotionTypes{
    TITLE,
    RICHT_TEXT,
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
    FORMULA
}

enum class NotionFields{
    PROPERTY,
    FILTER
}

