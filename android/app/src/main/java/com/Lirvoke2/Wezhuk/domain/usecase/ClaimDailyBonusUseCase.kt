package com.Lirvoke2.Wezhuk.domain.usecase

import com.Lirvoke2.Wezhuk.core.config.GameConfig
import com.Lirvoke2.Wezhuk.domain.model.DailyBonus
import com.Lirvoke2.Wezhuk.domain.model.DailyBonusState
import com.Lirvoke2.Wezhuk.domain.model.PlayerProfile

class ClaimDailyBonusUseCase {

    fun state(profile: PlayerProfile, ladder: List<DailyBonus>, nowMs: Long): DailyBonusState {
        val day = profile.bonusDay.coerceIn(1, GameConfig.DAILY_TIERS)
        val tier = ladder.firstOrNull { it.day == day } ?: ladder.first()
        if (profile.lastBonusClaimMs <= 0L) {
            return DailyBonusState(day = day, points = tier.points, available = true, remainingMs = 0L)
        }
        val elapsed = nowMs - profile.lastBonusClaimMs
        val remaining = GameConfig.DAILY_COOLDOWN_MS - elapsed
        return if (remaining <= 0L) {
            DailyBonusState(day = day, points = tier.points, available = true, remainingMs = 0L)
        } else {
            DailyBonusState(day = day, points = tier.points, available = false, remainingMs = remaining)
        }
    }

    fun nextDay(current: Int): Int = if (current >= GameConfig.DAILY_TIERS) 1 else current + 1
}
