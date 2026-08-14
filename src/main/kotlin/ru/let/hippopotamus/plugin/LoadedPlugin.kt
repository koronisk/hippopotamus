package ru.let.hippopotamus.plugin

import java.net.URLClassLoader

data class LoadedPlugin(
    val metadata: PluginMetadata,
    val plugin: Plugin,
    val classLoader: URLClassLoader
)