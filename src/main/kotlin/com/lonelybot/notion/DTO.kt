package com.lonelybot.notion

import com.lonelybot.NotionTags

open class NotionDatatable(var id: String? = null)
data class Meme(@NotionField("Name") val name: NotionTitle, @NotionField("Url") val url: NotionRichText, 
                @NotionField("Source") val source: NotionSelect, @NotionField("Tags") val tags: NotionMultiSelect, 
                @NotionField("Added By") val addedBy: NotionRelation?):
    NotionDatatable()
data class SlackUser(@NotionField("SlackId") val slackId: NotionRichText, 
                     @NotionField("SlackTeam") val slackTeam: NotionRichText,
                     @NotionField("RedCardsShown") val redCardsShown: NotionNumber, 
                     @NotionField("RedCardsReceived") val redCardsReceived: NotionNumber, 
                     @NotionField("YellowCardsReceived") val yellowCardsReceived: NotionNumber,
                     @NotionField("YellowCardsShown") val yellowCardsShown: NotionNumber,
                     @NotionField("Permissions") val permissions: NotionMultiSelect, 
                     @NotionField("LeavingOnFriday") val leavingOnFriday: NotionRichText?, 
                     @NotionField("LeavingRestOfWeek") val leavingRestOfWeek: NotionRichText,
                     @NotionField("SlackImChannel") val slackImChannel: NotionRichText): NotionDatatable()