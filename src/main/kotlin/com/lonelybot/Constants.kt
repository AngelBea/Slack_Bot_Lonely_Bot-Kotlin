package com.lonelybot
/*
* Endpoints
* */
const val POST_MESSAGE_URL = "https://slack.com/api/chat.postMessage"

/*
* Headers
* */
const val HEADER_AUTH_NAME = "Authorization"
const val HEADER_CONTENT_TYPE_NAME = "Content-Type"
const val HEADER_NOTION_VERSION_NAME = "Notion-Version"
const val HEADER_NOTION_VERSION_VALUE = "2021-08-16"

/*
* Tokens
* */
val BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN")
val NOTION_TOKEN = System.getenv("NOTION_TOKEN")

/*
* Lists
* */
val ACCEPTED_CARD_YELLOW_VALUES = listOf("amarilla", "yellow", "amarillo")
val ACCEPTED_CARD_RED_VALUES = listOf("red", "roja", "rojo")

/*
* Notion Endpoints Values
* */

val MEME_TABLE_ID = "8cfaddc8b0b94550aa203acf26ec8740"