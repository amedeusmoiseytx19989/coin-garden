package com.Lirvoke2.Wezhuk.domain.model

data class CoinSkin(
    val id: String,
    val labelRes: Int,
    val drawableRes: Int,
    val bonusPercent: Int,
    val unlockPoints: Int,
    val tinted: Boolean = false
) {
    val bonusFactor: Float
        get() = 1f + bonusPercent / 100f
}
