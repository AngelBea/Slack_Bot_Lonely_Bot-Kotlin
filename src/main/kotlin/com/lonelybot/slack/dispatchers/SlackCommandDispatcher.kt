package com.lonelybot.slack.dispatchers

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lonelybot.*
import com.lonelybot.adapters.SlackUserAdapter
import com.lonelybot.notion.*
import com.lonelybot.notion.Meme
import com.lonelybot.services.notion.BadUserException
import com.lonelybot.services.global.getCurrentUser
import com.lonelybot.services.global.isPermitted
import com.lonelybot.services.global.updateCardStatsForUsers
import com.lonelybot.services.slack.SlackChannelService
import com.lonelybot.slack.*
import com.lonelybot.slack.builders.SlackBlockBuilder
import com.lonelybot.slack.factories.Modules.Companion.MOD_LONELYME
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.coroutines.launch
import me.angelbea.application.notion.NotionObjectParser
import me.angelbea.application.notion.builders.NotionFilter
import java.time.*
import kotlin.random.Random

fun Route.commandReader() {
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
                        SlackApp.request.post.sendTextMessage(
                            parameters.channel_id,
                            "Recuerda el comando es /tarjeta [color] a @user por [motivo] | ¡El motivo es opcional!"
                        )
                    }
                }                
                SlackCommands.TIME -> {
                    processGetTimeRemaining(parameters) 
                }
                SlackCommands.ME -> processLonelyMe(parameters)
                else -> {
                    SlackApp.request.post.sendTextMessage(
                        parameters.channel_id,
                        "Ese comando no esta disponible para tu Team o esta en desarrollo"
                    )
                }
            }
        }
        
        call.respond(HttpStatusCode.OK)
    }
}

private fun checkCardCommand(parameters: Params, withReason: Boolean = false): Card {
    val text = parameters.text

    val color = REGEX_GET_COLOUR.find(text)?.value ?: ""
    val notionColorTag = if (color in ACCEPTED_CARD_RED_VALUES) NotionTags.RED_COLOUR
                            else if(color in ACCEPTED_CARD_YELLOW_VALUES) NotionTags.YELLOW_COLOUR
                            else NotionTags.NONE

    val user = REGEX_GET_USER.find(text)?.value ?: ""
    val reason = REGEX_GET_REASON.find(text)?.value ?: ""

    return if (withReason) Card(parameters.user_id, notionColorTag, user, parameters.channel_id, reason, parameters.team_id)
                else Card(parameters.user_id, notionColorTag, user, parameters.channel_id, teamId = parameters.team_id)
}

suspend fun processCardService(card: Card) {
    val fromUser = getCurrentUser(card.fromUser, card.teamId)
    if (!fromUser.isPermitted(Permissions.CARDS)){
        SlackApp.request.post.sendHiddenMessage(card.onChannel, COMMAND_OR_ACTION_BLOCKED, fromUser.slackId)
        return
    }
    var toUser: SlackUserAdapter
    
    try {
        toUser = getCurrentUser(card.toUser, card.teamId, false)        
    }catch (exc: BadUserException){
        SlackApp.request.post.sendHiddenMessage(card.onChannel, exc.message, fromUser.slackId)
        return
    }
    
    val notionFilter = NotionFilter{
        and {
            contains("Tags", NotionTypes.MULTI_SELECT, "Tarjeta")
            contains("Tags", NotionTypes.MULTI_SELECT, card.color.tagName)
        }
    }

    val response = NotionApi.Databases.queryDatabase(
        notionFilter,
        MEME_TABLE_ID
    ).bodyAsChannel().readUTF8Line()

    val cardMemes = NotionObjectParser()
        .parseQuery<Meme>(response ?: "")
        .shuffled(Random(System.currentTimeMillis()))

    val builder = SlackBlockBuilder{
        addContext { 
            addMarkdownText("<@${card.fromUser}> ha sacado ${card.color.emote} a <@${card.toUser}> por ${card.reason ?: "los loles"}")
        }
        addImage(
            cardMemes.first().url.richText.first().plainText ?: ""
            , cardMemes.first().name.title.first().plainText ?: ""
            , "${card.color.tagName} de <@${fromUser.slackUserName}>"
        )
    }
    
    
    SlackApp.request.post.sendBlockedMessage(card.onChannel, builder)
    updateCardStatsForUsers(fromUser, card.color.tagName, toUser)
    if (card.color.tagName == NotionTags.RED_COLOUR.tagName) SlackChannelService.kickUser(fromUser, toUser, card.onChannel, true)
}

private suspend fun processGetTimeRemaining(parameters: Params){
    val currentUser = getCurrentUser(parameters)
    if (!currentUser.isPermitted(Permissions.TIMEREMAINING)){
        SlackApp.request.post.sendHiddenMessage(parameters.channel_id, COMMAND_OR_ACTION_BLOCKED, parameters.user_id)
        return
    }
    
    val leavingHourFriday = currentUser.leavingOnFriday
    val leavingHourWeek = currentUser.leavingRestOfWeek
    if (leavingHourFriday == null || leavingHourWeek == null){
        val userIm = currentUser.slackImChannel
        SlackApp.request.post.sendTextMessage(userIm!!, REMAINING_TIME_MSG)
        return
    }
        
    val localTime = LocalDateTime.now().atZone(ZONE_OFFSET)
    val leavingFridayLocalTime = leavingHourFriday
        .split(":")
        .map { it.toInt() }
        .let { LocalDateTime.of(localTime.year, localTime.month, localTime.dayOfMonth, it.first(), it.last(), 0) }
    
    val leavingWeekLocalTime = leavingHourWeek
        .split(":")
        .map { it.toInt() }
        .let { LocalDateTime.of(localTime.year, localTime.month, localTime.dayOfMonth, it.first(), it.last(), 0) }
    
    val isWeekLessThanNow = leavingWeekLocalTime.toEpochSecond(ZONE_OFFSET) < localTime.toEpochSecond()
    val isFridayLessThanNow = localTime.dayOfWeek == DayOfWeek.FRIDAY && leavingFridayLocalTime.toEpochSecond(
        ZONE_OFFSET) < localTime.toEpochSecond()
    
    if (isFridayLessThanNow || isWeekLessThanNow){
        SlackApp.request.post.sendTextMessage(parameters.channel_id, "<@${parameters.user_id}> $YOU_SHOULD_NOT_BE_HERE")
        return
    }
    
    when(localTime.dayOfWeek){
        DayOfWeek.SATURDAY -> SlackApp.request.post.sendTextMessage(
            parameters.channel_id,
            "Que pasa <@${parameters.user_name}> ¿Quieres trabajar en Sabado?"
        )
        DayOfWeek.SUNDAY -> SlackApp.request.post.sendTextMessage(
            parameters.channel_id,
            "Que pasa <@${parameters.user_name}> ¿Quieres trabajar en Domingo?"
        )
        else -> SlackApp.request.post.sendTextMessage(
            parameters.channel_id,
            "<@${parameters.user_name}> quedan: ${calculateHours(localTime, leavingFridayLocalTime, leavingWeekLocalTime)}"
        )
    }
}

private fun calculateHours(now: ZonedDateTime, fridayDate: LocalDateTime, weekDay: LocalDateTime): String {
    var hourGap = 0
    
    val fridayAtThree = when (now.dayOfWeek) {
        DayOfWeek.MONDAY -> with(now.plusDays(4)) {
            hourGap = 4
            fridayDate.plusDays(hourGap.toLong()).atZone(ZONE_OFFSET)
        }
        DayOfWeek.TUESDAY -> with(now.plusDays(3)) {
            hourGap = 3
            fridayDate.plusDays(hourGap.toLong()).atZone(ZONE_OFFSET)
        }
        DayOfWeek.WEDNESDAY -> with(now.plusDays(2)) {
            hourGap = 2
            fridayDate.plusDays(hourGap.toLong()).atZone(ZONE_OFFSET)
        }
        DayOfWeek.THURSDAY -> with(now.plusDays(1)) {
            hourGap = 1
            fridayDate.plusDays(hourGap.toLong()).atZone(ZONE_OFFSET)
        }
        DayOfWeek.FRIDAY -> with(now.plusDays(0)) {
            hourGap = 0
            fridayDate.plusDays(hourGap.toLong()).atZone(ZONE_OFFSET)
        }
        DayOfWeek.SATURDAY -> with(now.plusDays(6)) {
            hourGap = 6
            fridayDate.plusDays(hourGap.toLong()).atZone(ZONE_OFFSET)
        }
        DayOfWeek.SUNDAY -> with(now.plusDays(5)) {
            hourGap = 5
            fridayDate.plusDays(hourGap.toLong()).atZone(ZONE_OFFSET)
        }
    }

    fridayAtThree.let(::println)

    val secondsBetween = (fridayAtThree.toEpochSecond() - now.toEpochSecond())
    val hours = secondsBetween / 3600 to secondsBetween % 3600
    val minutes = hours.second / 60 to hours.second % 60

    return "${hours.first - ((24 - 8) * hourGap)} horas, ${minutes.first} minutos y ${minutes.second} segundos de agonia"
}

suspend fun processLonelyMe(parameters: Params){
    val currentUser = getCurrentUser(parameters)
    if (!currentUser.isPermitted(Permissions.LONELYME)){
        SlackApp.request.post.sendHiddenMessage(parameters.channel_id, COMMAND_OR_ACTION_BLOCKED, parameters.user_id)
        return
    }
    
    val messageBlock = SlackBlockBuilder{
        this append MOD_LONELYME.invoke(currentUser)        
    }
    
    SlackApp.request.post.sendBlockedMessage(parameters.channel_id, messageBlock)    
}
