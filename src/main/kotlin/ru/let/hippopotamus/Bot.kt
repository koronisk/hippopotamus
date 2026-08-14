package ru.let.hippopotamus

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Guild
import dev.kord.core.event.channel.CategoryCreateEvent
import dev.kord.core.event.channel.CategoryDeleteEvent
import dev.kord.core.event.channel.CategoryUpdateEvent
import dev.kord.core.event.channel.TextChannelCreateEvent
import dev.kord.core.event.channel.TextChannelDeleteEvent
import dev.kord.core.event.channel.TextChannelUpdateEvent
import dev.kord.core.event.channel.VoiceChannelCreateEvent
import dev.kord.core.event.channel.VoiceChannelDeleteEvent
import dev.kord.core.event.channel.VoiceChannelUpdateEvent
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.guild.BanAddEvent
import dev.kord.core.event.guild.BanRemoveEvent
import dev.kord.core.event.guild.EmojisUpdateEvent
import dev.kord.core.event.guild.GuildUpdateEvent
import dev.kord.core.event.guild.IntegrationCreateEvent
import dev.kord.core.event.guild.IntegrationDeleteEvent
import dev.kord.core.event.guild.InviteCreateEvent
import dev.kord.core.event.guild.InviteDeleteEvent
import dev.kord.core.event.guild.MemberJoinEvent
import dev.kord.core.event.guild.MemberLeaveEvent
import dev.kord.core.event.guild.MemberUpdateEvent
import dev.kord.core.event.message.MessageBulkDeleteEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.message.MessageDeleteEvent
import dev.kord.core.event.message.MessageUpdateEvent
import dev.kord.core.event.message.ReactionAddEvent
import dev.kord.core.event.message.ReactionRemoveEvent
import dev.kord.core.event.role.RoleCreateEvent
import dev.kord.core.event.role.RoleDeleteEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import kotlinx.coroutines.flow.any
import ru.let.hippopotamus.config.GlobalConfig
import ru.let.hippopotamus.event.EventManager
import ru.let.hippopotamus.plugin.PluginManager

class Bot(private val config: GlobalConfig) {
    internal val eventManager = EventManager()
    internal val pluginManager = PluginManager(this)

    lateinit var botGuild: Guild
        private set

    lateinit var kord: Kord
        private set

    @OptIn(PrivilegedIntent::class)
    suspend fun start() {
        kord = Kord(config.token)
        botGuild = kord.getGuild(Snowflake(config.guild.toULong()))

        pluginManager.loadPlugins()
        subscribeKord()

        kord.login {
            intents = Intents(
                Intent.GuildMessages,
                Intent.MessageContent
            )
        }
    }

    private fun subscribeKord() {
        kord.on<ReadyEvent> { eventManager.call(this) }

        kord.on<CategoryCreateEvent> { if (channel.guild == botGuild) eventManager.call(this) }
        kord.on<CategoryDeleteEvent> { if (channel.guild == botGuild) eventManager.call(this) }
        kord.on<CategoryUpdateEvent> { if (channel.guild == botGuild) eventManager.call(this) }

        kord.on<TextChannelCreateEvent> { if (channel.guild == botGuild) eventManager.call(this) }
        kord.on<TextChannelDeleteEvent> { if (channel.guild == botGuild) eventManager.call(this) }
        kord.on<TextChannelUpdateEvent> { if (channel.guild == botGuild) eventManager.call(this) }

        kord.on<VoiceChannelCreateEvent> { if (channel.guild == botGuild) eventManager.call(this) }
        kord.on<VoiceChannelDeleteEvent> { if (channel.guild == botGuild) eventManager.call(this) }
        kord.on<VoiceChannelUpdateEvent> { if (channel.guild == botGuild) eventManager.call(this) }

        kord.on<BanAddEvent> { if (guild == botGuild) eventManager.call(this) }
        kord.on<BanRemoveEvent> { if (guild == botGuild) eventManager.call(this) }
        kord.on<EmojisUpdateEvent> { if (guild == botGuild) eventManager.call(this) }
        kord.on<GuildUpdateEvent> { if (guild == botGuild) eventManager.call(this) }

        kord.on<IntegrationCreateEvent> { if (guild == botGuild) eventManager.call(this) }
        kord.on<IntegrationDeleteEvent> { if (guild == botGuild) eventManager.call(this) }

        kord.on<InviteCreateEvent> { if (guild == botGuild) eventManager.call(this) }
        kord.on<InviteDeleteEvent> { if (guild == botGuild) eventManager.call(this) }

        kord.on<MemberJoinEvent> { if (guild == botGuild) eventManager.call(this) }
        kord.on<MemberLeaveEvent> { if (guild == botGuild) eventManager.call(this) }
        kord.on<MemberUpdateEvent> { if (guild == botGuild) eventManager.call(this) }

        kord.on<MessageCreateEvent> { if (guildId == botGuild.id) eventManager.call(this) }
        kord.on<MessageDeleteEvent> { if (guild == botGuild) eventManager.call(this) }
        kord.on<MessageBulkDeleteEvent> { if (guild == botGuild) eventManager.call(this) }
        kord.on<MessageUpdateEvent> {
            val channel = kord.getChannel(channelId)
            if (botGuild.channels.any { it.id == channel?.id }) eventManager.call(this)
        }
        
        kord.on<ReactionAddEvent> { if (guild == botGuild) eventManager.call(this) }
        kord.on<ReactionRemoveEvent> { if (guild == botGuild) eventManager.call(this) }

        kord.on<RoleCreateEvent> { if (guild == botGuild) eventManager.call(this) }
        kord.on<RoleDeleteEvent> { if (guild == botGuild) eventManager.call(this) }
        kord.on<RoleDeleteEvent> { if (guild == botGuild) eventManager.call(this) }
    }
}