package com.coderxi.pulgin.paidfly.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Path

class Localizer(private val plugin: JavaPlugin, private val filepathSupplier: () -> String) {

    private lateinit var file: File
    private var map: MutableMap<String, Any> = mutableMapOf()

    fun reload() {
        val filepath = filepathSupplier()
        file = File(plugin.dataFolder, filepath)
        if (!file.exists()) {
            plugin.saveResource(filepath, false)
        }
        val config: FileConfiguration = YamlConfiguration.loadConfiguration(file)
        val defaultConfigStream = plugin.getResource(filepath)
        if (defaultConfigStream != null) {
            config.setDefaults(YamlConfiguration.loadConfiguration(InputStreamReader(defaultConfigStream, StandardCharsets.UTF_8)))
        }
        val serializer = LegacyComponentSerializer.legacyAmpersand()
        map = mutableMapOf<String, Any>().apply {
            for (key in config.getKeys(true)) {
                when (val value = config.get(key)) {
                    is String -> put(key, serializer.deserialize(value))
                    is List<*> -> {
                        val list = value.mapNotNull { (it as? String)?.let(serializer::deserialize) }
                        put(key, list)
                    }
                }
            }
        }
    }

    fun get(key: String) = if (map.containsKey(key)) map[key]!! as TextComponent else Component.text(key)
    fun getList(key: String) = (map[key] as? List<*>)?.filterIsInstance<TextComponent>() ?: listOf(Component.text(key))

    companion object {
        private val localizers: MutableMap<Any, Localizer> = mutableMapOf()
        fun of(plugin: JavaPlugin, pathSupplier: () -> String): Localizer {
            if (localizers.containsKey(plugin)) return localizers[plugin]!!
            val localizer = Localizer(plugin,pathSupplier)
            localizers[plugin] = localizer
            return localizer
        }
    }

    fun replaceArgs(comp: Component, vararg args: Pair<String, Any?>): TextComponent {
        return args.fold(comp) { acc, (k, v) ->
            acc.replaceText { it.match(Regex.escape("{$k}")).replacement(v?.toString() ?: "") }
        } as TextComponent
    }

    fun getAndReplaceArgs(textKey: String, vararg args: Pair<String, Any?>): TextComponent {
        return if (args.isEmpty()) get(textKey) else replaceArgs(get(textKey), *args)
    }
}