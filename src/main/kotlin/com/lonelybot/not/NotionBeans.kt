package com.lonelybot.not

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

interface NotionDatatable
data class Meme(val name: NotionTitle, val url: NotionRichText, val source: NotionSelect, val date: NotionDate): NotionDatatable
open class NotionParameter(val id: String = "", val type: String = "")

data class NotionTitle(val title: List<NotionTextObject>): NotionParameter()
data class NotionRichText(@SerializedName("rich_text") val richText: List<NotionTextObject>): NotionParameter()
data class NotionDate(val date: NotionDateObject): NotionParameter()
data class NotionSelect(val select: NotionSelectObject): NotionParameter()
data class NotionParent(@SerializedName("database_id") val databaseId: String)
data class NotionMultiSelect(@SerializedName("multi_select") val multiSelect: MultiSelectObject)

data class MultiSelectObject(val name: String)
data class NotionTextObject(val type: String, val text: NotionContentObject, val annotations: NotionAnnotationsObject?, @SerializedName("plain_text") val value: String?, val href: String?)
data class NotionContentObject(val content: String, val link: NotionLinkObject)
data class NotionAnnotationsObject(val bold: Boolean, val italic: Boolean, val strikethrough: Boolean, val underline: Boolean, val code: Boolean, val color: String)
data class NotionDateObject(val start: LocalDateTime, val end: LocalDateTime? = null, @SerializedName("time_zone") val timeZone: String? = null)

data class NotionSelectObject(val name: String)
data class NotionLinkObject(val url: String)
