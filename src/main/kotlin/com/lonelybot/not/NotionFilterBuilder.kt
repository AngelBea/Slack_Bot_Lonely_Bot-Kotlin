package com.lonelybot.not

class NotionFilterBuilder {
    val filters: MutableList<MutableMap<String, Any>> = mutableListOf()

    companion object{
        inline fun build (builder: NotionFilterBuilder.() -> Unit): NotionFilterBuilder{
            return NotionFilterBuilder().apply(builder)
        }
    }
    
    fun and(builder: NotionFilterBuilder.() -> Unit){
        val filter = NotionFilter{
            and(builder)
        }
        
        val andMap = mutableMapOf<String, Any>(filter.retrieve().keys.toList()[0] to filter.retrieve().values.toList()[0])
        filters.add(andMap)
    }

    fun or(builder: NotionFilterBuilder.() -> Unit){
        val filter = NotionFilter{
            or(builder)
        }

        val orMap = mutableMapOf<String, Any>(filter.retrieve().keys.toList()[0] to filter.retrieve().values.toList()[0])
        filters.add(orMap)
    }
    
    fun contains(field: String, type: NotionTypes, typeConditon: Any): NotionFilterBuilder{
        filters.add(
            mutableMapOf(
                NotionFields.PROPERTY.name.lowercase() to field,
                type.name.lowercase() to mapOf("contains" to typeConditon)
            )
        )
        return this
    }

    fun equals(field: String, type: NotionTypes, typeConditon: Any): NotionFilterBuilder{
        filters.add(
            mutableMapOf(
                NotionFields.PROPERTY.name.lowercase() to field,
                type.name.lowercase() to mapOf("equals" to typeConditon)
            )
        )
        return this
    }

    fun doesNotEqual(field: String, type: NotionTypes, typeConditon: Any): NotionFilterBuilder{
        filters.add(
            mutableMapOf(
                NotionFields.PROPERTY.name.lowercase() to field,
                type.name.lowercase() to mapOf("does_not_equal" to typeConditon)
            )
        )
        return this
    }

    fun startsWith(field: String, type: NotionTypes, typeConditon: String): NotionFilterBuilder{
        TODO()
    }

    fun endsWith(field: String, type: NotionTypes, typeConditon: String): NotionFilterBuilder{
        TODO()
    }

    fun isEmpty(field: String, type: NotionTypes, typeConditon: Boolean): NotionFilterBuilder{
        TODO()
    }

    fun isNotEmpty(field: String, type: NotionTypes, typeConditon: Boolean): NotionFilterBuilder{
        TODO()
    }

    fun greaterThan(field: String, type: NotionTypes, typeConditon: Number): NotionFilterBuilder{
        TODO()
    }

    fun lessThan(field: String, type: NotionTypes, typeConditon: Number): NotionFilterBuilder{
        TODO()
    }

    fun lessThanOrEqualTo(field: String, type: NotionTypes, typeConditon: Number): NotionFilterBuilder{
        TODO()
    }

    fun greaterThanOrEqualTo(field: String, type: NotionTypes, typeConditon: Number): NotionFilterBuilder{
        TODO()
    }
}