package com.coderxi.pulgin.paidfly.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class PaidFlyTabCompleter : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>? {
        if (command.name.equals("paidfly", ignoreCase = true) || command.name.equals("fly", ignoreCase = true)) {
            if (args.size == 1) {
                val options = mutableListOf("help", "on", "off", "toggle")
                if (sender.hasPermission("paidfly.admin")) {
                    options.add("reload")
                }
                return options.filter { it.startsWith(args[0], ignoreCase = true) }.toMutableList()
            }
            if (args.size == 2 && sender.hasPermission("paidfly.others")) {
                return Bukkit.getOnlinePlayers().map { it.name }.filter { it.startsWith(args[1], ignoreCase = true) }.toMutableList()
            }
        }
        return null
    }
}