package com.Lirvoke2.Wezhuk.domain.usecase

import com.Lirvoke2.Wezhuk.core.config.GameConfig
import com.Lirvoke2.Wezhuk.domain.model.CoinPoint
import com.Lirvoke2.Wezhuk.domain.model.TargetRing
import kotlin.math.abs

class LaunchCoinUseCase {

    fun coinPoint(targetX: Float, progress: Float): CoinPoint {
        val clamped = progress.coerceIn(0f, 1f)
        val eased = clamped * clamped * (3f - 2f * clamped)
        val x = 0.5f + (targetX - 0.5f) * eased
        val y = GameConfig.COIN_START_Y - 1.25f * clamped + 0.42f * clamped * clamped
        return CoinPoint(x.coerceIn(0.04f, 0.96f), y.coerceIn(0.04f, 1f))
    }

    fun spinAt(progress: Float): Float = 1080f * progress.coerceIn(0f, 1f)

    fun advanceRings(rings: List<TargetRing>, deltaMs: Long): List<TargetRing> {
        val seconds = deltaMs / 1000f
        return rings.map { ring ->
            if (ring.hidden) {
                val left = ring.respawnLeftMs - deltaMs
                if (left > 0L) {
                    ring.copy(respawnLeftMs = left)
                } else {
                    val entry = if (ring.direction > 0) GameConfig.ARENA_MIN_X else GameConfig.ARENA_MAX_X
                    ring.copy(hidden = false, respawnLeftMs = 0L, x = entry)
                }
            } else {
                var next = ring.x + ring.speed * ring.direction * seconds
                var direction = ring.direction
                if (next <= GameConfig.ARENA_MIN_X) {
                    next = GameConfig.ARENA_MIN_X
                    direction = 1
                } else if (next >= GameConfig.ARENA_MAX_X) {
                    next = GameConfig.ARENA_MAX_X
                    direction = -1
                }
                ring.copy(x = next, direction = direction)
            }
        }
    }

    fun detectHit(rings: List<TargetRing>, point: CoinPoint): TargetRing? = rings
        .filter { !it.hidden }
        .firstOrNull { ring ->
            abs(point.y - ring.laneY) <= GameConfig.RING_TOLERANCE_Y &&
                abs(point.x - ring.x) <= GameConfig.RING_TOLERANCE_X
        }

    fun multiplierFor(streak: Int): Int =
        (1 + streak / GameConfig.STREAK_STEP).coerceIn(1, GameConfig.MAX_MULTIPLIER)

    fun pointsFor(ringValue: Int, multiplier: Int, bonusFactor: Float): Int =
        (ringValue * multiplier * bonusFactor).toInt()
}
