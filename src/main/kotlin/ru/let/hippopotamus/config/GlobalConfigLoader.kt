package ru.let.hippopotamus.config

import tools.jackson.module.kotlin.jacksonObjectMapper
import tools.jackson.module.kotlin.readValue
import java.io.File
import kotlin.system.exitProcess


internal class GlobalConfigLoader {
    fun loadOrExit(): GlobalConfig {
        val mapper = jacksonObjectMapper()

        if (!File("global.json").exists()) {
            println("Global config (\"global.json\") was not found in root directory")
            exitProcess(0)
        }

        val config = mapper.readValue<GlobalConfig>(File("global.json"))
        
        return config
    }
}