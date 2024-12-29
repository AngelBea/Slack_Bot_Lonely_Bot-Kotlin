package com.lonelybot.notion

import com.google.gson.annotations.SerializedName
import kotlinx.html.*

sealed interface NotionBlock{
    fun appendToHtml(article: ARTICLE)
    fun appendToHtml(article: ARTICLE, classes: Set<String>)
}

abstract class PageBlock(@SerializedName("has_children") val hasChildren: Boolean = false, val type: String = ""):
    NotionBlock

data class NotionFirstHeadingBlock(@SerializedName("heading_1") val heading: NotionRichText): PageBlock() {
    override fun appendToHtml(article: ARTICLE){
        article.apply {
            h1 {
                +heading.value
            }
        }
    }

    override fun appendToHtml(article: ARTICLE, classes: Set<String>) {
        article.apply {
            h1 {
                this.classes = classes
                +heading.value
            }
        }
    }
}

data class NotionThirdHeadingBlock(@SerializedName("heading_3") val heading: NotionRichText): PageBlock() {
    override fun appendToHtml(article: ARTICLE){
        article.apply {
            h3 {
                +heading.value
            }
        }
    }

    override fun appendToHtml(article: ARTICLE, classes: Set<String>) {
        article.apply {
            h3 {
                this.classes = classes
                +heading.value
            }
        }
    }
}
data class NotionDivider(@SerializedName("heading_3") val divider: Any?): PageBlock() {
    override fun appendToHtml(article: ARTICLE){
        article.apply {
            hr {  }
        }
    }

    override fun appendToHtml(article: ARTICLE, classes: Set<String>) {
        article.apply {
            hr {
                this.classes = classes
            }
        }
    }
}

data class NotionSecondHeadingBlock(@SerializedName("heading_2") val heading: NotionRichText): PageBlock() {
    override fun appendToHtml(article: ARTICLE) {
        article.apply {
            h2 {
                +heading.value
            }
        }
    }

    override fun appendToHtml(article: ARTICLE, classes: Set<String>) {
        article.apply {
            h2 {
                this.classes = classes
                +heading.value
            }
        }
    }
}

data class NotionParagraphBlock(val paragraph: NotionRichText): PageBlock() {
    override fun appendToHtml(article: ARTICLE) {
        article.apply {
            p {
                paragraph.appendHtmlValue(this)
            }
        }
    }

    override fun appendToHtml(article: ARTICLE, classes: Set<String>) {
        article.apply {
            p {
                this.classes = classes
                paragraph.appendHtmlValue(this)
            }
        }
    }
}

data class NotionImageBlock(val image: NotionImage): PageBlock() {
    override fun appendToHtml(article: ARTICLE) {
        val tokens = listOf("class:")
        val regexToken = tokens.joinToString("|").toRegex()
        article.apply {
            val hasSpecialTokens = image.captionValue?.contains(regexToken) == true
            figure {
                img {
                    if (hasSpecialTokens){
                        this.classes = image.captionValue!!.replace(regexToken, "")
                            .split(" ")
                            .filter { it.isNotBlank() }
                            .map { it.trim() }
                            .toSet()
                    }
                    src = image.value
                }
                if (!hasSpecialTokens){
                    figcaption {
                        +(image.captionValue ?: "")
                    }
                }
            }
        }
    }

    override fun appendToHtml(article: ARTICLE, classes: Set<String>) {
        val tokens = listOf("classes:")
        val regexToken = tokens.joinToString("|").toRegex()
        article.apply {
            val hasSpecialTokens = image.captionValue?.contains(regexToken) == true
            figure {
                img {
                    if (hasSpecialTokens){
                        val classesMutable = classes.toMutableSet()
                        classesMutable.addAll(image.captionValue!!.replace(regexToken, "")
                            .split(" ")
                            .filter { it.isNotBlank() }
                            .map { it.trim() })
                        this.classes = classesMutable
                    }
                    src = image.value
                }
                if (!hasSpecialTokens){
                    figcaption {
                        +(image.captionValue ?: "")
                    }
                }
            }
        }
    }

}