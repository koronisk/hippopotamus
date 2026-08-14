package ru.let.hippopotamus.event

import dev.kord.core.event.Event
import ru.let.hippopotamus.plugin.Plugin
import java.lang.reflect.Method

data class Listener(
    val plugin: Plugin,
    val eventType: Class<out Event>,
    val method: Method
)