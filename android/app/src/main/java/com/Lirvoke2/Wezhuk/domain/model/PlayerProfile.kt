package com.Lirvoke2.Wezhuk.domain.model

data class PlayerProfile(
    val strikePoints: Int = 0,
    val bestScore: Int = 0,
    val bestStreak: Int = 0,
    val roundsPlayed: Int = 0,
    val ringsHit: Int = 0,
    val shotsFired: Int = 0,
    val equippedSkinId: String = "brass",
    val unlockedSkinIds: Set<String> = setOf("brass"),
    val lastBonusClaimMs: Long = 0L,
    val bonusDay: Int = 1,
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val reducedMotion: Boolean = false
) {
    val accuracy: Int
        get() = if (shotsFired <= 0) 0 else (ringsHit * 100) / shotsFired
}
