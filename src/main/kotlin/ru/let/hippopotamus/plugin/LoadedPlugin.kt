package ru.let.hippopotamus.plugin

import java.net.URLClassLoader

internal data class LoadedPlugin(
    val metadata: PluginMetadata,
    val plugin: Plugin,
    val classLoader: URLClassLoader
)