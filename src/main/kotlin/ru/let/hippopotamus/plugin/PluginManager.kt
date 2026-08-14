package ru.let.hippopotamus.plugin

import org.yaml.snakeyaml.Yaml
import ru.let.hippopotamus.Bot
import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarFile

internal class PluginManager(private val bot: Bot) {
    private val plugins: MutableSet<LoadedPlugin> = mutableSetOf()

    fun loadPlugins() {
        val folder = File("plugins/config")

        if (!folder.exists())
            folder.mkdirs()

        val files = folder.listFiles()
            ?.filter { it.isFile && it.extension.equals("jar", true) }
            ?: return

        files.forEach {
            try {
                loadPlugin(it)
            } catch (e: Exception) {
                println("Failed to load plugin ${it.name}")
                e.printStackTrace()
            }
        }
    }

    private fun loadPlugin(file: File) {
        val jar = JarFile(file)

        val pluginYml = jar.getJarEntry("plugin.yml")
            ?: throw IllegalStateException("${file.name} doesn't contain plugin.yml")

        val metadata = jar.getInputStream(pluginYml).use { readMetadata(it) }
        val classLoader = URLClassLoader(arrayOf(file.toURI().toURL()), javaClass.classLoader)
        val pluginClass = classLoader.loadClass(metadata.main)

        if (!Plugin::class.java.isAssignableFrom(pluginClass))
            throw IllegalStateException("${metadata.main} does not extend Plugin")

        val plugin = pluginClass.getDeclaredConstructor().newInstance() as Plugin
        plugin.bot = bot

        val loaded = LoadedPlugin(metadata, plugin, classLoader)
        plugins.add(loaded)

        bot.eventManager.register(plugin)

        plugin.onEnable()

        println("Loaded ${metadata.name} ${metadata.version}")
    }

    private fun readMetadata(input: java.io.InputStream): PluginMetadata {
        val yaml = Yaml()

        val data = yaml.load<Map<String, Any>>(input)

        return PluginMetadata(
            name = data["name"]?.toString() ?: error("plugin.yml: missing name"),
            main = data["main"]?.toString() ?: error("plugin.yml: missing main"),
            version = data["version"]?.toString() ?: "unknown"
        )
    }
}