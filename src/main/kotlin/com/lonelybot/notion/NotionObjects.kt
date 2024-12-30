package com.lonelybot.notion

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.html.P
import kotlinx.html.unsafe
import java.time.LocalDateTime


abstract class NotionParameter(val id: String? = null, val type: String? = null) {
    abstract val value: Any
}

data class NotionTitle(val title: MutableList<NotionTextObject>): NotionParameter(){
    override val value: String
        get() = title[0].text!!.content
}

data class NotionRichText(@SerializedName("rich_text") val richText: MutableList<RichTextType>): NotionParameter(){
    override val value: String
        get() = if (richText.isNotEmpty()) richText.joinToString(" "){ it.plainText!! } else ""

    fun appendHtmlValue(pTag: P){
        pTag.apply {
            richText.forEach {
                val tags = mutableListOf<String>()
                val closeTags = mutableListOf<String>()
                if (it.text?.link?.url != null){ tags.add("<a href=${it.text?.link?.url}>"); closeTags.add("</a>") }
                if (it.annotations?.bold == true){ tags.add("<b>"); closeTags.add("</b>") }
                if (it.annotations?.underline == true){ tags.add("<u>"); closeTags.add("</u>") }
                if (it.annotations?.strikethrough == true){ tags.add("<s>"); closeTags.add("</s>") }
                if (it.annotations?.italic == true){ tags.add("<i>"); closeTags.add("</i>") }
                unsafe {
                   raw(
                       "${tags.joinToString("")}${it.plainText}${closeTags.reversed().joinToString("")}"
                   )
                }
            }
        }
    }
}

data class NotionDate(val date: NotionDateObject): NotionParameter(){
    override val value: LocalDateTime
        get() = LocalDateTime.parse(date.start)
}

data class NotionSelect(val select: NotionSelectObject): NotionParameter(){
    override val value: String
        get() = select.name
}

data class NotionMultiSelect(@SerializedName("multi_select") val multiSelect: MutableList<MultiSelectObject>) : NotionParameter(){
    override val value: List<String>
        get() = multiSelect.map { it.name }
}

data class NotionNumber(val number: Int): NotionParameter(){
    override val value: Int
        get() = number
}

data class NotionParent(@SerializedName("database_id") val databaseId: String, val type: String = "database_id")

data class NotionRelation(val relation: List<NotionRelationObject>) : NotionParameter(){
    override val value: String
        get() = relation.first().id
}

data class NotionImage(val file: NotionLinkObject?, val external: NotionLinkObject?, val caption: MutableList<RichTextType>?) : NotionParameter(){
    override val value: String
        get() = file?.url ?: external!!.url
    val captionValue: String?
        get() = if(caption?.isNotEmpty() == true) caption.first().plainText else ""
}

data class NotionStatus(val status: NotionStatusObject) : NotionParameter() {
    override val value: String
        get() = status.name
}


data class NotionStringFormula(val formula: NotionStringFormulaObject) : NotionParameter() {
    override val value: String
        get() = formula.string
}


data class NotionStringFormulaObject(val string: String)

data class NotionStatusObject(val id: String, val name: String, val color: String)

open class RichTextType(val type: String, open val annotations: NotionAnnotationsObject?,
                        open val href: String?, @SerializedName("plain_text") open val plainText: String?,
                        open val text: NotionContentObject? = null)

data class NotionTextObject(@Transient private val annotationsValue: NotionAnnotationsObject?,
                            @Transient private val hrefValue: String?,
                            @Transient private val plainTextValue: String?,
                            @Transient private val textValue: NotionContentObject? = null)
    : RichTextType(NotionTypes.TEXT.name.lowercase(), annotationsValue, hrefValue, plainTextValue, textValue)

data class NotionEquationObject(val equation: NotionExpressionObject,
                                @Transient private val expAnnotations: NotionAnnotationsObject? = null,
                                @Transient private val expHref: String? = null,
                                @Transient private val expValue: String?)
    : RichTextType(NotionTypes.EQUATION.name.lowercase(), expAnnotations, expHref, expValue)

data class MultiSelectObject(val name: String)

data class NotionContentObject(val content: String, val link: NotionLinkObject?)

data class NotionAnnotationsObject(val bold: Boolean, val italic: Boolean, val strikethrough: Boolean, val underline: Boolean, val code: Boolean, val color: String)

data class NotionDateObject(val start: String, val end: String? = null, @SerializedName("time_zone") val timeZone: String? = null)

data class NotionExpressionObject(val expression: String)

data class NotionRelationObject(val id: String)

data class NotionSelectObject(val name: String)

data class NotionLinkObject(val url: String)

open class NotionPage(val id: String? = null, val cover: NotionImage? = null,
                      @SerializedName("created_time") val createdTime : LocalDateTime = LocalDateTime.now(),
                      @SerializedName("last_edited_time") val lastEditedTime: LocalDateTime = LocalDateTime.now(),
                      val icon: NotionImage? = null)