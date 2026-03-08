package com.coderxi.pulgin.paidfly.utils

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import com.coderxi.pulgin.paidfly.PaidFlyPlugin.Companion.plugin
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.title.Title

fun CommandSender.sendLocalMessage(textKey: String, vararg args: Pair<String, String>) {
    sendMessage(plugin.localizer.getAndReplaceArgs(textKey, *args))
}

fun Player.sendLocalTitle(titleKey: String, vararg args: Pair<String, String>) {
    val titleAndSubTitle = plugin.localizer.getList(titleKey)
    val title = plugin.localizer.replaceArgs(titleAndSubTitle[0], *args)
    val subtitle = plugin.localizer.replaceArgs(titleAndSubTitle[1], *args)
    showTitle(Title.title(title,subtitle))
}

fun CommandSender.sendLocalAction(textKey: String, vararg args: Pair<String, String>) {
    sendActionBar(plugin.localizer.getAndReplaceArgs(textKey, *args))
}

val Player.isOnGroundByServerCheck: Boolean get() {
    val loc = location.clone().add(0.0, -0.1, 0.0)
    val block = loc.block
    return block.type.isSolid
}

