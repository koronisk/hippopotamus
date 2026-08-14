package ru.let.hippopotamus

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Guild
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import ru.let.hippopotamus.config.GlobalConfig
import ru.let.hippopotamus.event.EventManager
import ru.let.hippopotamus.plugin.PluginManager

class Bot(private val config: GlobalConfig) {
    internal val eventManager = EventManager()
    internal val pluginManager = PluginManager(this)

    lateinit var guild: Guild
        private set
    
    lateinit var kord: Kord
        private set

    @OptIn(PrivilegedIntent::class)
    suspend fun start() {
        pluginManager.loadPlugins()
        kord = Kord(config.token)
        guild = kord.getGuild(Snowflake(config.guild.toULong()))

        kord.on<MessageCreateEvent> { eventManager.call(this) }

        kord.login {
            intents = Intents(
                Intent.GuildMessages,
                Intent.MessageContent
            )
        }
    }
}