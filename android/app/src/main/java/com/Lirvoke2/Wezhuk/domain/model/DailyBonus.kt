package com.Lirvoke2.Wezhuk.domain.model

data class DailyBonus(val day: Int, val points: Int)

data class DailyBonusState(
    val day: Int,
    val points: Int,
    val available: Boolean,
    val remainingMs: Long
)
