package com.lonelybot.services.global

import com.lonelybot.adapters.SlackUserAdapter
import com.lonelybot.imgur.ImageType
import com.lonelybot.notion.decorators.MemeDecorator
import com.lonelybot.services.imgur.ImageService
import com.lonelybot.services.notion.MemeService

suspend fun createYellowCard(url: String, createdUser: SlackUserAdapter): MemeDecorator{
    val album = "aKxnpW5"
    val imageCreated = ImageService.createImage(url, album, ImageType.URL)
    val notionMemeCreated = MemeService.createMeme(createdUser.notionId!!, "yellow_${imageCreated.id}", "Slack", imageCreated.link, "Tarjeta", "Amarilla", "Yellow", "Card")
    
    return notionMemeCreated
}