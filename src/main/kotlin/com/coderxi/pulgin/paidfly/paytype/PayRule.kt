package com.coderxi.pulgin.paidfly.paytype

import com.coderxi.pulgin.paidfly.PaidFlyPlugin.Companion.plugin
import org.bukkit.configuration.ConfigurationSection

data class PayRule(
    val payType: String,
    val payInterval: Long,  // ticks
    val payCost: Double,
    val autoOffThreshold: Double
) {
    val payTypeLocalName get() = plugin.localizer.get("PayType.$payType").content()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PayRule) return false

        return payType == other.payType &&
                payInterval == other.payInterval &&
                payCost == other.payCost &&
                autoOffThreshold == other.autoOffThreshold
    }

    override fun hashCode(): Int {
        var result = payType.hashCode()
        result = 31 * result + payInterval.hashCode()
        result = 31 * result + payCost.hashCode()
        result = 31 * result + autoOffThreshold.hashCode()
        return result
    }

    companion object {
        fun fromConfigurationSection(section: ConfigurationSection): PayRule {
            return PayRule(
                payType = section.getString("PayType") ?: "Money",
                payInterval = parseInterval(section.getString("PayInterval") ?: "1s"),
                payCost = section.getDouble("PayCost"),
                autoOffThreshold = section.getDouble("AutoOffThreshold")
            )
        }
        private fun parseInterval(text: String): Long {
            val lower = text.lowercase().trim()
            return when {
                lower.endsWith("t") -> {
                    lower.dropLast(1).toLong()
                }
                lower.endsWith("s") -> {
                    val value = lower.dropLast(1).toLong()
                    value * 20
                }
                lower.endsWith("m") -> {
                    val value = lower.dropLast(1).toLong()
                    value * 20 * 60
                }
                lower.endsWith("h") -> {
                    val value = lower.dropLast(1).toLong()
                    value * 20 * 60 * 60
                }
                else -> {
                    lower.toLong()
                }
            }
        }
    }
}