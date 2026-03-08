package com.coderxi.pulgin.paidfly.expansion

import com.coderxi.pulgin.paidfly.service.PaidFlyService
import com.coderxi.pulgin.paidfly.service.PayService
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class PaidFlyPAPIExpansion: PlaceholderExpansion() {
    override fun persist() = true
    override fun getIdentifier(): String = "paidfly"
    override fun getAuthor(): String = "coderxi"
    override fun getVersion(): String = "1.0.0"

    override fun onPlaceholderRequest(player: Player?, params: String): String? {
        if (player == null) return ""
        val key = params.lowercase()
        if (key.startsWith("pay_type")) {
            val payRule = PayService.getPayRuleByPlayer(player)
            val payType = PayService.getPayTypeByName(payRule.payType)
            val value = payType?.getValue(player) ?: 0.0
            return when {
                key == "pay_type" -> payRule.payTypeLocalName
                key == "pay_type_value" -> value.toString()
                key.startsWith("pay_type_value_fixed") -> {
                    val digits = key.removePrefix("pay_type_value_fixed").removePrefix("_").toIntOrNull() ?: 1
                    "%.${digits}f".format(value)
                }
                else -> null
            }
        }
        return when (key) {
            "status" -> PaidFlyService.isFlying(player).toString()
            "remaining_seconds" -> PaidFlyService.getRemainingFlySeconds(player).toString()
            else -> null
        }
    }
}