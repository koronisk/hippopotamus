package ru.let.hippopotamus.event

import dev.kord.core.event.Event
import ru.let.hippopotamus.plugin.Plugin
import kotlin.jvm.java
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.callSuspend
import kotlin.reflect.jvm.kotlinFunction

internal class EventManager {
    private val listeners: MutableSet<Listener> = mutableSetOf()

    fun register(plugin: Plugin) {

        for (method in plugin.javaClass.methods) {
            if (!method.isAnnotationPresent(EventHandler::class.java))
                continue

            val function = method.kotlinFunction
                ?: throw IllegalArgumentException("Cannot get Kotlin function for ${method.name}")

            val parameters = function.parameters.filter { it.kind == KParameter.Kind.VALUE }

            if (parameters.size != 1)
                throw IllegalArgumentException("${method.name} must have exactly one event parameter")

            val eventType = parameters[0].type.classifier as? KClass<*>
                ?: throw IllegalArgumentException("Cannot determine event type")

            if (!Event::class.java.isAssignableFrom(eventType.java))
                throw IllegalArgumentException("${method.name} parameter must extend Event")

            @Suppress("UNCHECKED_CAST")
            listeners += Listener(plugin, eventType.java as Class<out Event>, function)
        }
    }

    suspend fun call(event: Event) {
        listeners.forEach {
            if (!it.eventType.isAssignableFrom(event.javaClass))
                return@forEach

            it.function.callSuspend(it.plugin, event)
        }
    }
}