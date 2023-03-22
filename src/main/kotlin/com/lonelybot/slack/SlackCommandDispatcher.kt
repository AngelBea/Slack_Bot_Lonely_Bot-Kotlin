package com.lonelybot.slack

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lonelybot.*
import com.lonelybot.not.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.*
import kotlin.random.Random

fun Route.commandLecture() {
    post("/command") {
        val parameters = processParameters(call.receiveParameters())
        println("Parameters: $parameters")
    
        val command = SlackCommands.values().firstOrNull { it.shortcut == parameters.command }
        command.let(::println)
        launch {
            when (command) {
                SlackCommands.CARD -> {
               
                    val text = parameters.text

                    if (text.matches(REGEX_PHRASE_WITH_REASON)){
                        processCardService(checkCardCommand(parameters, true))
                    }else if (text.matches(REGEX_PHRASE_NO_REASON)){
                        processCardService(checkCardCommand(parameters))
                    }else{
                        SlackApp.request.post.sendTextMessage(parameters.channel_id,"Recuerda el comando es /tarjeta [color] a @user por [motivo] | ¡El motivo es opcional!")
                    }
                }                
                SlackCommands.TIME -> {
                    processGetTimeRemaining(parameters) 
                }
                SlackCommands.SUGGEST -> {

                }
                else -> {
                    SlackApp.request.post.sendTextMessage(parameters.channel_id, "Ese comando no esta disponible para tu Team o esta en desarrollo")
                }
            }
        }
        
        call.respond(HttpStatusCode.OK)
    }
}

private fun checkCardCommand(parameters: Params, withReason: Boolean = false): Card{
    val text = parameters.text

    val color = REGEX_GET_COLOUR.find(text)?.value ?: ""
    val notionColorTag = if (color in ACCEPTED_CARD_RED_VALUES) NotionTags.RED_COLOUR
                            else if(color in ACCEPTED_CARD_YELLOW_VALUES) NotionTags.YELLOW_COLOUR
                            else NotionTags.NONE

    val user = REGEX_GET_USER.find(text)?.value ?: ""
    val reason = REGEX_GET_REASON.find(text)?.value ?: ""

    return if (withReason) Card(parameters.user_id, notionColorTag, user, parameters.channel_id, reason)
                else Card(parameters.user_name, notionColorTag, user, parameters.channel_id)
}

suspend fun processCardService(card: Card) {
    card.let(::println)
    val blocks = mutableListOf<Block>()
    val username = card.toUser

    val notionFilter = NotionFilterBuilder.build<String> {
        this.contains("Tags", NotionTypes.MULTI_SELECT, "Tarjeta")
        this.contains("Tags", NotionTypes.MULTI_SELECT, card.color.tagName)
    }

    val response = NotionApp.request.post.queryDatabase(
        notionFilter,
        NotionLogicalFilter.AND,
        MEME_TABLE_ID
    ).bodyAsChannel().readUTF8Line()

    val cardMemes = NotionObjectParser(Gson().fromJson(response, JsonObject::class.java), Meme::class)
        .parseQueryObject<Meme>()
        .shuffled(Random(System.currentTimeMillis()))

    blocks.add(
        ContextBlock(
            mutableListOf(
                Text(
                    ElementType.MARKDOWN.typeName, "<@${card.fromUser}> ha sacado ${card.color.emote} a <@${username}> por ${card.reason ?: "los loles"}"
                )
            )
        )
    )
    blocks.add(
        ImageBlock(
            cardMemes.first().url.richText.first().href ?: "",
            cardMemes.first().name.title.first().value ?: "",
            Text(ElementType.TEXT.typeName, "${card.color.tagName} de <@${card.fromUser}>")
        )
    )

    SlackApp.request.post.sendBlockedMessage(card.onChannel, blocks)
}

private suspend fun processGetTimeRemaining(parameters: Params){
    val localTime = LocalDateTime.now().atZone(PARIS_ZONE_OFFSET)

    when(localTime.dayOfWeek){
        DayOfWeek.SATURDAY -> SlackApp.request.post.sendTextMessage(parameters.channel_id, "Que pasa <@${parameters.user_name}> ¿Quieres trabajar en Sabado?")
        DayOfWeek.SUNDAY -> SlackApp.request.post.sendTextMessage(parameters.channel_id, "Que pasa <@${parameters.user_name}> ¿Quieres trabajar en Domingo?")
        else -> SlackApp.request.post.sendTextMessage(parameters.channel_id, "<@${parameters.user_name}> quedan: ${calculateHours(localTime)}")
    }
}

private fun calculateHours(now: ZonedDateTime): String {
    var hourGap = 0
    
    val fridayAtThree = when (now.dayOfWeek) {
        DayOfWeek.MONDAY -> with(now.plusDays(4)) {
            hourGap = 4
            LocalDateTime.of(this.year, this.month, this.dayOfMonth, 15, 0, 0).atZone(PARIS_ZONE_OFFSET)
        }
        DayOfWeek.TUESDAY -> with(now.plusDays(3)) {
            hourGap = 3
            LocalDateTime.of(this.year, this.month, this.dayOfMonth, 15, 0, 0).atZone(PARIS_ZONE_OFFSET)
        }
        DayOfWeek.WEDNESDAY -> with(now.plusDays(2)) {
            hourGap = 2
            LocalDateTime.of(this.year, this.month, this.dayOfMonth, 15, 0, 0).atZone(PARIS_ZONE_OFFSET)
        }
        DayOfWeek.THURSDAY -> with(now.plusDays(1)) {
            hourGap = 1
            LocalDateTime.of(this.year, this.month, this.dayOfMonth, 15, 0, 0).atZone(PARIS_ZONE_OFFSET)
        }
        DayOfWeek.FRIDAY -> with(now.plusDays(0)) {
            hourGap = 0
            LocalDateTime.of(this.year, this.month, this.dayOfMonth, 15, 0, 0).atZone(PARIS_ZONE_OFFSET)
        }
        DayOfWeek.SATURDAY -> with(now.plusDays(6)) {
            hourGap = 6
            LocalDateTime.of(this.year, this.month, this.dayOfMonth, 15, 0, 0).atZone(PARIS_ZONE_OFFSET)
        }
        DayOfWeek.SUNDAY -> with(now.plusDays(5)) {
            hourGap = 5
            LocalDateTime.of(this.year, this.month, this.dayOfMonth, 15, 0, 0).atZone(PARIS_ZONE_OFFSET)
        }
    }

    fridayAtThree.let(::println)

    val secondsBetween = (fridayAtThree.toEpochSecond() - now.toEpochSecond())
    val hours = secondsBetween / 3600 to secondsBetween % 3600
    val minutes = hours.second / 60 to hours.second % 60

    return "${hours.first - ((24 - 8) * hourGap)} horas, ${minutes.first} minutos y ${minutes.second} segundos de agonia"
}

fun processCommandSuggest(){

}
