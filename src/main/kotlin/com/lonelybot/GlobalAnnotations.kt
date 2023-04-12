package com.lonelybot

import java.lang.annotation.ElementType


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class NotSerializable