package com.lonelybot.notion


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class NotionField(val fieldValue: String)