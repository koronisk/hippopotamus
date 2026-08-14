package ru.let.hippopotamus

import ru.let.hippopotamus.config.GlobalConfigLoader

suspend fun main() {
    val config = GlobalConfigLoader().loadOrExit()
    val bot = Bot(config)
    bot.start()
}