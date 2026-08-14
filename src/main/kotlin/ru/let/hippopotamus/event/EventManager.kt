package ru.let.hippopotamus.event

import dev.kord.core.event.Event
import ru.let.hippopotamus.plugin.Plugin
import kotlin.jvm.java

class EventManager {

    private val listeners: MutableSet<Listener> = mutableSetOf()

    fun register(plugin: Plugin) {
        val clazz = plugin.javaClass

        for (method in clazz.methods) {
            if (!method.isAnnotationPresent(EventHandler::class.java))
                continue

            if (method.parameterCount != 1)
                throw IllegalArgumentException("${method.name} in ${clazz.name} must have exactly one parameter")

            val eventType = method.parameterTypes[0]

            if (!Event::class.java.isAssignableFrom(eventType))
                throw IllegalArgumentException("${method.name} parameter must extend Event")

            method.isAccessible = true

            @Suppress("UNCHECKED_CAST")
            val eventClass = eventType as Class<out Event>

            listeners += Listener(plugin, eventClass, method)
        }
    }

    fun call(event: Event) {
        listeners.forEach {
            if (!it.eventType.isAssignableFrom(event.javaClass))
                return@forEach

            it.method.invoke(it.plugin, event)
        }
    }
}