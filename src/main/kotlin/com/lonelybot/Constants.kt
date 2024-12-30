package com.lonelybot

import com.lonelybot.adapters.SlackUserAdapter
import java.sql.Time
import java.time.LocalDateTime
import java.time.ZoneOffset

/*
* Endpoints
* */
const val POST_MESSAGE_URL = "https://slack.com/api/chat.postMessage"
const val HIDDEN_MESSAGE_URL = "https://slack.com/api/chat.postEphemeral"
const val PUBLISH_HOME_URL = "https://slack.com/api/views.publish"
const val USER_CONVERSATION_URL = "https://slack.com/api/users.conversations"
const val OPEN_CONVERSATION_URL = "https://slack.com/api/conversations.open"
const val USER_INFO_URL = "https://slack.com/api/users.info"
const val BOT_INFO_URL = "https://slack.com/api/bots.info"
const val OPEN_MODAL_URL = "https://slack.com/api/views.open"
const val UPDATE_VIEW_URL = "https://slack.com/api/views.update"
const val KICK_USER_URL = "https://slack.com/api/conversations.kick"
const val OAUTH_CODE = "https://slack.com/api/oauth.v2.access"
const val IMGUR_CREATE_IMG_URL = "https://api.imgur.com/3/image"
const val IMGUR_TOKEN_URL = "https://api.imgur.com/oauth2/token"

/*
* Headers
* */
const val HEADER_AUTH_NAME = "Authorization"
const val HEADER_CONTENT_TYPE_NAME = "Content-Type"
const val HEADER_NOTION_VERSION_NAME = "Notion-Version"
const val HEADER_NOTION_VERSION_VALUE = "2022-06-28"

/*
* Tokens
* */
val TEST_MODE: Boolean = System.getenv("TEST_MODE").toBoolean()
val BOT_TOKEN_PROD: String = System.getenv("SLACK_BOT_TOKEN")
val BOT_TOKEN_TEST: String = System.getenv("SLACK_BOT_TOKEN_TEST")
val BOT_TOKEN: String = if (TEST_MODE) BOT_TOKEN_TEST else BOT_TOKEN_PROD
val IMGUR_REFRESH_TOKEN: String = System.getenv("IMGUR_REFRESH_TOKEN")
val IMGUR_CLIENT_ID: String = System.getenv("IMGUR_ID")
val IMGUR_CLIENT_SECRET: String = System.getenv("IMGUR_SECRET")

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

val ZONE_OFFSET: ZoneOffset = ZoneOffset.UTC.rules.getOffset(LocalDateTime.now())

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

/*
* Messages
 */

const val YOU_WERE_NOT_REGISTERED = "No estabas registrado en el sistema, pero Mr. Lonely lo ha hecho automaticamente :face_with_monocle:. Prueba a usar /lonelyme"
const val COMMAND_OR_ACTION_BLOCKED = "¡Uh, oh, sin permisos! :x:. Parece que alguien ha enfadado a Mr. Lonely :anger::face_with_monocle:"
const val REMAINING_TIME_MSG = "En mi mansión (La Home App) puedes decirme cuando sales, entonces te diré cuanto te queda."
const val YOU_SHOULD_NOT_BE_HERE = "¿Tu no te habías ido ya o quieres seguir trabajando?"

/*
* View Texts
 */

val VIEW_HOME_MESSAGE_MR_LONELY = {user: SlackUserAdapter -> """Hola <@${user.slackId}> bienvenido a mi humilde mansión. Aquí podrás acceder a todo lo que necesitas, si te dejo..."""}
const val VIEW_HOME_IMAGE_MR_LONELY = "https://i.imgur.com/qAXUaZI.png"
const val VIEW_HOME_ALT_MR_LONELY = "Mr. Lonely"

const val VIEW_HOME_DATEPICKER_FRIDAY = "¿A que hora te piensas ir los Viernes?"
const val VIEW_HOME_DATEPICKER_WEEK = "¿A que hora empiezas a hacer el vago entre semana?"
const val VIEW_HOME_ACTIONID_FRIDAY = "time-remaining-pick-friday"
const val VIEW_HOME_ACTIONID_WEEK = "time-remaining-pick-week"
val VIEW_HOME_TIME_FRIDAY = Time.valueOf("15:00:00")
val VIEW_HOME_TIME_WEEK = Time.valueOf("17:30:00")
const val VIEW_HOME_PLACEHOLDER_TIMEPICKER = "Pon la hora."
const val VIEW_HOME_SECTION_FRIDAY_ID = "time-remaining-section-friday"
const val VIEW_HOME_SECTION_WEEK_ID = "time-remaining-section-week"
const val VIEW_HOME_SAVE_BUTTON_ID = "save-time-remaining"
const val VIEW_HOME_SAVE_CARD_BUTTON_ID = "save-card"
const val VIEW_INPUT_CARD_URL_ID = "input-card-url"
const val VIEW_INPUT_URL_BLOCK_ID = "card-url"
const val VIEW_INPUT_OPTION_CARD_URL_ID = "card-url-option"
const val VIEW_INPUT_OPTION_CARD_SELECTED_URL_ID = "card-url-option-selected"
const val VIEW_INPUT_OPTION_MESSAGE = "Pon aquí la URL y selecciona si quieres que sea una tarjeta roja o una amarilla."

const val VIEW_HOME_ACTION_ID_CHANGELOG = "load-changelog-menu"
const val VIEW_HOME_ACTION_ID_LONELYRUN = "load-lonely-run-menu"
const val VIEW_HOME_ACTION_ID_LONELYCARD = "load-lonely-card-menu"
const val VIEW_HOME_ACTION_ID_LONELYME = "load-lonely-me-menu"
const val VIEW_HOME_ACTION_ID_AUTH = "load-auth"
const val VIEW_HOME_SEL_USER_BLOCK_ID = "selected-user-block"
const val VIEW_HOME_SEL_USER = "selected-user"

/* Yellow Card Modal */
const val VIEW_MODAL_YELLOW_CARD_TITLE = "¡Tarjeta amarilla!"
const val VIEW_MODAL_YELLOW_CARD_TEXT = "Vas a a sacar :large_yellow_square: a esta pobre alma dile el porqué:"
const val VIEW_MODAL_YELLOW_CARD_PH = "User"
const val VIEW_MODAL_YELLOW_USER_CARD_ACTION_ID = "to-user"
const val VIEW_MODAL_YELLOW_CARD_USER_BLOCK_ID = "user-selected"
const val VIEW_MODAL_YELLOW_CARD_TEXT_ACTION_ID = "reason-text-input-yellow-card"
const val VIEW_MODAL_YELLOW_CARD_TEXT_BLOCK_ID = "modal-yellow-card-text"
const val VIEW_MODAL_YELLOW_CARD_CLOSE = "Perdonarle"
const val VIEW_MODAL_YELLOW_CARD_SUBMIT = "Castigarle"
const val VIEW_MODAL_YELLOW_CARD_ID = "yellow-card-view"
const val VIEW_MODAL_CARD_CHANNEL_TEXT = "Puedes hacerlo en el canal en el que estabas o hacerlo más personal :knife:. Tu eliges:"
const val VIEW_MODAL_CARD_CHANNEL_PH = "Channel"
const val VIEW_MODAL_YELLOW_CARD_CHANNEL_ACTION_ID = "to-channel"
const val VIEW_MODAL_YELLOW_CARD_CHANNEL_BLOCK_ID = "yellow-card-to-channel"

/* Red Card Modal */
const val VIEW_MODAL_RED_CARD_TITLE = "¡Tarjeta ROJA!"
const val VIEW_MODAL_RED_CARD_TEXT = "Piensa bien porque vas a sacar :large_red_square: ¿crees que se lo merece de verdad?"
const val VIEW_MODAL_RED_CARD_PH = "User"
const val VIEW_MODAL_RED_USER_CARD_ACTION_ID = "to-user"
const val VIEW_MODAL_RED_CARD_USER_BLOCK_ID = "user-selected"
const val VIEW_MODAL_RED_CARD_TEXT_ACTION_ID = "reason-text-input-red-card"
const val VIEW_MODAL_RED_CARD_TEXT_BLOCK_ID = "modal-red-card-text"
const val VIEW_MODAL_RED_CARD_CLOSE = "No, me lo pienso."
const val VIEW_MODAL_RED_CARD_SUBMIT = "¡Si, expulsale!"
const val VIEW_MODAL_RED_CARD_ID = "red-card-view"
const val VIEW_MODAL_RED_CARD_CHANNEL_ACTION_ID = "to-channel"
const val VIEW_MODAL_RED_CARD_CHANNEL_BLOCK_ID = "red-card-to-channel"


/* Upload Image Confirmation Modal */

const val UPLOAD_MODAL_ID = "upload_card_confirmation"
const val UPLOAD_MODAL_TITLE = "¡Tarjeta subida!"
const val UPLOAD_MODAL_MSG_IMG = "Esta es tu imagen:"
const val UPLOAD_MODAL_MSG_URL = "Aqui la puedes ver en Imgur: "

/* Response Modal */
const val RESPONSE_MODAL_ID = "response_modal"
const val RESPONSE_MODAL_TITLE = "Responde al mensaje."
const val RESPONSE_MODAL_INPUT_MESSAGE_ID = "response_modal_message"
const val RESPONSE_MODAL_INPUT_MESSAGE_ID_ACTION = "response_modal_message_action"
const val RESPONSE_MODAL_CHANNEL_ID = "change_channel_response"
const val RESPONSE_MODAL_CHANNEL_ID_ACTION = "change_channel_response_action"
const val RESPONSE_MODAL_MESSAGE_SECTION = "message_to_response"

/* Text decorators */
const val INDENTATION = "\t\t"