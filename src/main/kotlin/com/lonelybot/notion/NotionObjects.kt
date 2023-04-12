package com.lonelybot.notion

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

open class NotionParameter(val id: String? = null, val type: String? = null)

data class NotionTitle(val title: MutableList<NotionTextObject>): NotionParameter()
data class NotionRichText(@SerializedName("rich_text") val richText: MutableList<RichTextType>): NotionParameter()
data class NotionDate(val date: NotionDateObject): NotionParameter()
data class NotionSelect(val select: NotionSelectObject): NotionParameter()
data class NotionMultiSelect(@SerializedName("multi_select") val multiSelect: MutableList<MultiSelectObject>) : NotionParameter()
data class NotionNumber(val number: Int): NotionParameter()
data class NotionParent(@SerializedName("database_id") val databaseId: String, val type: String = "database_id")
data class NotionRelation(val relation: List<NotionRelationObject>) : NotionParameter()


open class RichTextType(val type: String, val annotations: NotionAnnotationsObject?, val href: String?, @SerializedName("plain_text") val plainText: String?)
data class NotionTextObject(val text: NotionContentObject, private val textAnnotations: NotionAnnotationsObject? = null, private val textHref: String? = null, private val textValue: String?)
    : RichTextType(NotionTypes.TEXT.name.lowercase(), textAnnotations, textHref, textValue)
data class NotionEquationObject(val equation: NotionExpressionObject, private val expAnnotations: NotionAnnotationsObject? = null, private val expHref: String? = null, private val expValue: String?)
    : RichTextType(NotionTypes.EQUATION.name.lowercase(), expAnnotations, expHref, expValue)
data class MultiSelectObject(val name: String)
data class NotionContentObject(val content: String, val link: NotionLinkObject?)
data class NotionAnnotationsObject(val bold: Boolean, val italic: Boolean, val strikethrough: Boolean, val underline: Boolean, val code: Boolean, val color: String)
data class NotionDateObject(val start: String, val end: String? = null, @SerializedName("time_zone") val timeZone: String? = null)
data class NotionExpressionObject(val expression: String)
data class NotionRelationObject(val id: String)


data class NotionSelectObject(val name: String)
data class NotionLinkObject(val url: String)
