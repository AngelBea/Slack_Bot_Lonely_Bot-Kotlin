package com.lonelybot.singletons

interface IStatefulSingleton<T> {    
    fun add(obj: T)
    fun remove(obj: T): Boolean
    fun getByKey(key: String): T?
    fun isPresent(key: String):  Boolean
}