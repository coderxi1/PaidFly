package com.coderxi.pulgin.paidfly.paytype

import com.coderxi.pulgin.paidfly.PaidFlyPlugin.Companion.plugin
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object MoneyPayType : PayType() {
    override val name: String = "Money"
    private var economy: Economy? = null
    private var initialized = false
    override fun init() {
        if (initialized) return
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) return plugin.logger.warning("Vault plugin not found! PayType [Money] disabled.")
        val rsp = plugin.server.servicesManager.getRegistration(Economy::class.java)
        economy = rsp?.provider
        initialized = true
    }

    override fun getValue(player: Player): Double {
        return economy?.getBalance(player) ?: 0.0
    }

    override fun pay(player: Player, amount: Double): Boolean {
        val eco = economy ?: return false
        if (!eco.has(player, amount)) return false
        return eco.withdrawPlayer(player, amount).transactionSuccess()
    }
}