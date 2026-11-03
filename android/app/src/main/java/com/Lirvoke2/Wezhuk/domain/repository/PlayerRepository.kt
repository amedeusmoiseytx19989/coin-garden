package com.Lirvoke2.Wezhuk.domain.repository

import com.Lirvoke2.Wezhuk.domain.model.PlayerProfile
import com.Lirvoke2.Wezhuk.domain.model.RoundResult

interface PlayerRepository {

    fun profile(): PlayerProfile

    fun saveRound(result: RoundResult): PlayerProfile

    fun equipSkin(skinId: String): PlayerProfile

    fun unlockSkins(skinIds: Set<String>): PlayerProfile

    fun addPoints(amount: Int): PlayerProfile

    fun markDailyClaimed(day: Int, timestampMs: Long): PlayerProfile

    fun updatePreferences(
        soundEnabled: Boolean,
        hapticsEnabled: Boolean,
        reducedMotion: Boolean
    ): PlayerProfile

    fun resetProgress(): PlayerProfile
}
