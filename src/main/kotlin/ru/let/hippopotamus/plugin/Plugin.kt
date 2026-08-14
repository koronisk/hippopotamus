package ru.let.hippopotamus.plugin

import ru.let.hippopotamus.Bot

open class Plugin {
    lateinit var bot: Bot
        internal set

    open suspend fun onEnable() {}
}