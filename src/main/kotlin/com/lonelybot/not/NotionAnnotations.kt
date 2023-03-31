package com.lonelybot.not


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class NotionField(val fieldValue: String)