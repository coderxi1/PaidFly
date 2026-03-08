package com.coderxi.pulgin.paidfly.utils

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound

object SoundUtils {
    fun parseSoundExpress(expr: String): Sound {
        val split = expr.split(",")
        val key = Key.key(split[0])
        val volume = split.getOrNull(1)?.toFloatOrNull() ?: 1f
        val pitch = split.getOrNull(2)?.toFloatOrNull() ?: 1f
        return Sound.sound(key, Sound.Source.MASTER, volume, pitch)
    }
}