package com.lonelybot.slack

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lonelybot.ACCEPTED_CARD_RED_VALUES
import com.lonelybot.ACCEPTED_CARD_YELLOW_VALUES
import com.lonelybot.MEME_TABLE_ID
import com.lonelybot.NotionTags
import com.lonelybot.not.*

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Time
import java.util.*
import kotlin.random.Random
import kotlin.reflect.KClass

fun Route.commandLecture() {
    post("/command") {
        val parameters = processParameters(call.receiveParameters())
        println("Parameters: $parameters")
        withContext(Dispatchers.Unconfined) {
            val command = SlackCommands.values().filter { it.shortcut == parameters.command }.firstOrNull()

            when (command) {
                SlackCommands.CARD -> {
                    val splittedMessage = parameters.text.split(" ")
                    val color = splittedMessage.last()
                    when (color.lowercase()) {
                        in ACCEPTED_CARD_YELLOW_VALUES -> processCardService(parameters, NotionTags.YELLOW_COLOUR)
                        in ACCEPTED_CARD_RED_VALUES -> processCardService(parameters, NotionTags.RED_COLOUR)
                        else -> SlackApp.request.post.sendTextMessage(
                            parameters.channel_id,
                            "¿Estas seguro que sabes los colores?"
                        )
                    }
                }
                else -> {
                    SlackApp.request.post.sendTextMessage(parameters.channel_id, "¡Ese comando no existe!")
                }
            }

        }

        call.respond(HttpStatusCode.OK)
    }
}

private fun processParameters(parameters: Parameters): Params {
    val mapParams = mutableMapOf<String, String>()

    parameters.entries().forEach { mapParams[it.key] = it.value.first() }

    val paramsJson = Gson().toJson(mapParams)

    return Gson().fromJson(paramsJson, Params::class.java)
}

private suspend fun processCardService(parameters: Params, tag: NotionTags) {
    val blocks = mutableListOf<Block>()
    val username = parameters.text.split(" ").first()

    val notionFilter = NotionFilterBuilder.build<String> {
        this.contains("Tags", NotionTypes.MULTI_SELECT, "Tarjeta")
        this.contains("Tags", NotionTypes.MULTI_SELECT, tag.tagName)
    }

    val response = NotionApp.request.post.queryDatabase(
        notionFilter,
        NotionLogicalFilter.AND,
        MEME_TABLE_ID
    ).content.readUTF8Line()

    val cardMemes = NotionObjectParser(Gson().fromJson(response, JsonObject::class.java), Memes::class)
        .parseQueryObject<Memes>()
        .shuffled(Random(System.currentTimeMillis()))

    blocks.add(
        ContextBlock(
            mutableListOf(
                Text(
                    ElementType.MARKDOWN.typeName, "<@${parameters.user_id}> ha sacado ${tag.emote} a $username"
                )
            )
        )
    )
    blocks.add(
        ImageBlock(
            cardMemes.first().url.richText.first().href,
            cardMemes.first().name.title.first().value,
            Text(ElementType.TEXT.typeName, "${tag.tagName} de <@${parameters.user_name}>")
        )
    )

    SlackApp.request.post.sendBlockedMessage(parameters.channel_id, blocks)
}
