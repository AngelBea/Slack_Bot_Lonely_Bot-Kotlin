package com.lonelybot.not

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class NotionFilterBuilder<T> {
    val filters: MutableList<MutableMap<String, Any>> = mutableListOf()

    companion object{
        inline fun <T: Any> build (builder: NotionFilterBuilder<T>.() -> Unit): NotionFilterBuilder<T>{
            return NotionFilterBuilder<T>().apply(builder)
        }
    }


    fun contains(property: String, type: NotionTypes, typeConditon: T): NotionFilterBuilder<T>{
        filters.add(
            mutableMapOf(
                NotionFields.PROPERTY.name.lowercase() to property,
                type.name.lowercase() to mapOf("contains" to typeConditon)
            )
        )
        return this
    }

    fun equals(property: String, type: NotionTypes, typeConditon: T): NotionFilterBuilder<T>{
        TODO()
    }

    fun doesNotEqual(property: String, type: NotionTypes, typeConditon: T): NotionFilterBuilder<T>{
        TODO()
    }

    fun startsWith(property: String, type: NotionTypes, typeConditon: String): NotionFilterBuilder<T>{
        TODO()
    }

    fun endsWith(property: String, type: NotionTypes, typeConditon: String): NotionFilterBuilder<T>{
        TODO()
    }

    fun isEmpty(property: String, type: NotionTypes, typeConditon: Boolean): NotionFilterBuilder<T>{
        TODO()
    }

    fun isNotEmpty(property: String, type: NotionTypes, typeConditon: Boolean): NotionFilterBuilder<T>{
        TODO()
    }

    fun greaterThan(property: String, type: NotionTypes, typeConditon: Number): NotionFilterBuilder<T>{
        TODO()
    }

    fun lessThan(property: String, type: NotionTypes, typeConditon: Number): NotionFilterBuilder<T>{
        TODO()
    }

    fun lessThanOrQualTo(property: String, type: NotionTypes, typeConditon: Number): NotionFilterBuilder<T>{
        TODO()
    }

    fun greaterThanOrEqualTo(property: String, type: NotionTypes, typeConditon: Number): NotionFilterBuilder<T>{
        TODO()
    }
}