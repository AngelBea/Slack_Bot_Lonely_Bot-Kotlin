package com.lonelybot.not

import com.google.gson.annotations.SerializedName

interface NotionDatatable
data class Memes(val name: NotionTitle, val url: NotionRichText): NotionDatatable
open class NotionParameter(val id: String = "", val type: String = "")

data class NotionTitle(val title: List<NotionTextObject>): NotionParameter()
data class NotionRichText(@SerializedName("rich_text") val richText: List<NotionTextObject>): NotionParameter()
data class NotionTextObject(val type: String, val text: NotionContent, val annotations: NotionAnnotations, @SerializedName("plain_text") val value: String, val href: String)
data class NotionContent(val content: String, val link: NotionLinkObject)
data class NotionAnnotations(val bold: Boolean, val italic: Boolean, val strikethrough: Boolean, val underline: Boolean, val code: Boolean, val color: String)
data class NotionLinkObject(val url: String)

