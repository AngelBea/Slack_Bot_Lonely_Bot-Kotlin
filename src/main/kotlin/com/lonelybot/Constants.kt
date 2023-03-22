package com.lonelybot

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

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
val TEST_MODE: Boolean = System.getenv("TEST_MODE").toBoolean()
val BOT_TOKEN_PROD: String = System.getenv("SLACK_BOT_TOKEN")
val BOT_TOKEN_TEST: String = System.getenv("SLACK_BOT_TOKEN_TEST")
val BOT_TOKEN: String = if (TEST_MODE) BOT_TOKEN_TEST else BOT_TOKEN_PROD
val NOTION_TOKEN: String = System.getenv("NOTION_TOKEN")

/*
* Lists
* */
val ACCEPTED_CARD_YELLOW_VALUES = listOf("amarilla", "yellow", "amarillo")
val ACCEPTED_CARD_RED_VALUES = listOf("red", "roja", "rojo")

/*
* Notion Endpoints Values
* */

const val MEME_TABLE_ID = "8cfaddc8b0b94550aa203acf26ec8740"

/*
* Zone Offset
* */

val PARIS_ZONE_OFFSET: ZoneOffset = ZoneId.of("Europe/Berlin").rules.getOffset(LocalDateTime.now())

/*
* Regex /tarjeta
* */

val REGEX_PHRASE_WITH_REASON = "^(${ACCEPTED_CARD_YELLOW_VALUES.joinToString("|")}|${ACCEPTED_CARD_RED_VALUES.joinToString("|")}) a <@(\\w+\\|[\\w\\W]+)> por [\\w\\W]+".toRegex()
val REGEX_PHRASE_NO_REASON = "^(${ACCEPTED_CARD_YELLOW_VALUES.joinToString("|")}|${ACCEPTED_CARD_RED_VALUES.joinToString("|")}) a <@(\\w+\\|[\\w\\W]+)>".toRegex()

val REGEX_GET_COLOUR = "${ACCEPTED_CARD_YELLOW_VALUES.joinToString("|")}|${ACCEPTED_CARD_RED_VALUES.joinToString("|")}".toRegex()
val REGEX_GET_USER = "(?<=<@)\\w+".toRegex()
val REGEX_GET_REASON = "(?<=por )[\\w\\W]+".toRegex()

/*
* Source select values
* */

const val SOURCE_TEST_VALUE = "Test"