package com.coderxi.pulgin.paidfly.command

import com.coderxi.pulgin.paidfly.PaidFlyPlugin.Companion.plugin
import com.coderxi.pulgin.paidfly.service.PaidFlyService
import com.coderxi.pulgin.paidfly.service.PaidFlyService.isFlying
import com.coderxi.pulgin.paidfly.utils.sendLocalMessage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object PaidFlyCmdExecutor : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val subCommand = if (!args.isEmpty()) args[0].lowercase() else null
        when (subCommand) {
            "reload" -> {
                if (!sender.hasPermission("paidfly.reload")) {
                    sender.sendLocalMessage("System.NoPermission")
                    return true
                }
                plugin.reload()
                sender.sendLocalMessage("System.ReloadSuccess")
            }
            "help" -> {
                sender.sendMessage(Component.join(JoinConfiguration.separator(Component.newline()),buildList {
                    addAll(plugin.localizer.getList("Help.Default"))
                    if (sender.hasPermission("paidfly.others"))
                        addAll(plugin.localizer.getList("Help.Others"))
                    if (sender.hasPermission("paidfly.admin"))
                        addAll(plugin.localizer.getList("Help.Admin"))
                }))
            }
            "on","off","toggle",null -> {
                if (!sender.hasPermission("paidfly.fly")) {
                    sender.sendLocalMessage("System.NoPermission")
                    return true
                }
                val targetName = if (args.size > 1) args[1].lowercase() else null
                if (targetName == null && sender !is Player) {
                    sender.sendLocalMessage("System.PlayerOnlyCommand")
                    return true
                }
                val targetPlayer =  if (targetName != null) Bukkit.getPlayerExact(targetName) else sender as Player
                if (targetPlayer == null) {
                    sender.sendLocalMessage("System.PlayerNotOnline")
                    return true
                }
                val isSelf = sender == targetPlayer
                if (!isSelf && !sender.hasPermission("paidfly.others")) {
                    sender.sendLocalMessage("System.NoPermission")
                    return true
                }
                val enable = when (subCommand ?: "toggle") {
                    "on" -> true
                    "off" -> false
                    "toggle" -> !isFlying(targetPlayer)
                    else -> false
                }
                //fly logic
                if (isSelf) {
                    if (enable) { PaidFlyService.startFlySelf(targetPlayer) }
                    else { PaidFlyService.stopFlySelf(targetPlayer)}
                } else {
                    if (enable) { PaidFlyService.startFlyOther(sender, targetPlayer) }
                    else { PaidFlyService.stopFlyOther(sender, targetPlayer)}
                }

            }
            else -> {
                sender.sendLocalMessage("System.UnknownCommand")
            }
        }
        return true
    }
}