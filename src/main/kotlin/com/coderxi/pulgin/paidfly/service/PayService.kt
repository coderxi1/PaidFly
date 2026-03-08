package com.coderxi.pulgin.paidfly.service

import com.coderxi.pulgin.paidfly.PaidFlyPlugin.Companion.plugin
import com.coderxi.pulgin.paidfly.paytype.PayRule
import com.coderxi.pulgin.paidfly.paytype.ExpPayType
import com.coderxi.pulgin.paidfly.paytype.MoneyPayType
import com.coderxi.pulgin.paidfly.paytype.PayType
import com.coderxi.pulgin.paidfly.utils.BaseService
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object PayService : BaseService() {

    lateinit var payRuleMain: PayRule
        private set
    lateinit var payRuleGroups: Map<String, PayRule>
        private set

    private val cache = ConcurrentHashMap<UUID, PayRule>()

    private val payTypes : Map<String, PayType>  = listOf(ExpPayType, MoneyPayType).associateBy { it.name }

    override fun init() {
        reload()
        payTypes.values.forEach { it.init() }
        plugin.server.pluginManager.registerEvents(Listener(),plugin)
    }

    override fun reload() {
        payRuleMain = PayRule.fromConfigurationSection(plugin.config.getConfigurationSection("Main")!!)
        payRuleGroups = plugin.config.getConfigurationSection("Group")?.getKeys(false)?.associateWith { group ->
            PayRule.fromConfigurationSection(plugin.config.getConfigurationSection("Group.$group")!!)
        } ?: emptyMap()
        cache.clear()
    }

    fun getPayRuleByPlayer(player: Player): PayRule {
        return cache.computeIfAbsent(player.uniqueId) {
            payRuleGroups.entries.firstOrNull { (group, _) ->
                player.hasPermission("paidfly.group.$group")
            }?.value ?: payRuleMain
        }
    }

    fun getPayTypeByName(name: String): PayType? {
        return if (payTypes.containsKey(name)) payTypes[name] else null
    }

    fun testEnough(player: Player): Boolean {
        val rule = getPayRuleByPlayer(player)
        val payType = getPayTypeByName(rule.payType)
        return payType!!.getValue(player) >= rule.payCost
    }

    private class Listener : org.bukkit.event.Listener {
        @EventHandler
        fun onPlayerQuit(event: PlayerQuitEvent) {
            cache.remove(event.player.uniqueId)
        }
    }

}