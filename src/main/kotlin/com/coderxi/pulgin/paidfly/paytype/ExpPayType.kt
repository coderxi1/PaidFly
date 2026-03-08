package com.coderxi.pulgin.paidfly.paytype

import org.bukkit.entity.Player

object ExpPayType : PayType() {
    override val name: String = "Exp"
    override fun getValue(player: Player): Double {
        return player.totalExperience.toDouble()
    }
    override fun pay(player: Player, amount: Double): Boolean {
        if (player.totalExperience < amount) return false
        player.giveExp((-amount).toInt())
        return true
    }

    override fun init() {
    }
}