package com.Lirvoke2.Wezhuk.domain.model

data class RoundResult(
    val score: Int,
    val ringsHit: Int,
    val shots: Int,
    val bestStreak: Int
) {
    val accuracy: Int
        get() = if (shots <= 0) 0 else (ringsHit * 100) / shots
}
