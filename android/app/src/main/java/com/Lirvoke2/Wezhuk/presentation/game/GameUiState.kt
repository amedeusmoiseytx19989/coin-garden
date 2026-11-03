package com.Lirvoke2.Wezhuk.presentation.game

import com.Lirvoke2.Wezhuk.domain.model.CoinPoint
import com.Lirvoke2.Wezhuk.domain.model.CoinSkin
import com.Lirvoke2.Wezhuk.domain.model.RoundResult
import com.Lirvoke2.Wezhuk.domain.model.TargetRing

enum class GamePhase { READY, IN_FLIGHT, RESOLVING, FINISHED }

data class HitMarker(
    val id: Int,
    val x: Float,
    val y: Float,
    val value: Int,
    val hit: Boolean
)

data class GameUiState(
    val phase: GamePhase,
    val score: Int,
    val streak: Int,
    val bestStreak: Int,
    val multiplier: Int,
    val coinsLeft: Int,
    val coinBank: Int,
    val remainingMs: Long,
    val rings: List<TargetRing>,
    val coin: CoinPoint?,
    val coinSpin: Float,
    val trail: List<CoinPoint>,
    val marker: HitMarker?,
    val resolveProgress: Float,
    val ringsHit: Int,
    val shots: Int,
    val skin: CoinSkin,
    val reloadBannerLeftMs: Long,
    val paused: Boolean
) {
    val canLaunch: Boolean
        get() = phase == GamePhase.READY && !paused && coinsLeft > 0
}

data class RoundOutcome(
    val result: RoundResult,
    val newBest: Boolean,
    val unlockedCoinLabel: Int
)
