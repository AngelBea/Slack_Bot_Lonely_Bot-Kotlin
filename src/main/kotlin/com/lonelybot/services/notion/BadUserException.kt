package com.lonelybot.services.notion

class BadUserException: Exception() {
    override val message: String
        get() = "No puedes usar este comando con un BOT"
}