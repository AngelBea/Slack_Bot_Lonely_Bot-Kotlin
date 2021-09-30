package com.lonelybot.not


class NotAFilterException: Exception(){
    override val message: String?
        get() = "Only OR/AND condition can be invoked on Object"
}
