package com.Lirvoke2.Wezhuk.domain.model

data class Rank(val index: Int, val labelRes: Int, val minXp: Int)

data class RankState(
    val index: Int,
    val labelRes: Int,
    val currentXp: Int,
    val targetXp: Int
) {
    val fraction: Float
        get() = if (targetXp <= 0) 1f else (currentXp.toFloat() / targetXp).coerceIn(0f, 1f)
}
