package com.coderxi.pulgin.paidfly.paytype

import org.bukkit.entity.Player

abstract class PayType {
    abstract val name: String
    abstract fun getValue(player: Player): Double
    abstract fun pay(player: Player, amount: Double): Boolean
    abstract fun init()
}



