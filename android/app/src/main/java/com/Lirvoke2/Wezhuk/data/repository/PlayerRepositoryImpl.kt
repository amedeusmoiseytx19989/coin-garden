package com.Lirvoke2.Wezhuk.data.repository

import com.Lirvoke2.Wezhuk.data.local.CoinStrikePreferences
import com.Lirvoke2.Wezhuk.data.sample.SampleData
import com.Lirvoke2.Wezhuk.domain.model.PlayerProfile
import com.Lirvoke2.Wezhuk.domain.model.RoundResult
import com.Lirvoke2.Wezhuk.domain.repository.PlayerRepository

class PlayerRepositoryImpl(
    private val store: CoinStrikePreferences
) : PlayerRepository {

    override fun profile(): PlayerProfile = PlayerProfile(
        strikePoints = store.readInt(KEY_POINTS, 0),
        bestScore = store.readInt(KEY_BEST_SCORE, 0),
        bestStreak = store.readInt(KEY_BEST_STREAK, 0),
        roundsPlayed = store.readInt(KEY_ROUNDS, 0),
        ringsHit = store.readInt(KEY_RINGS, 0),
        shotsFired = store.readInt(KEY_SHOTS, 0),
        equippedSkinId = store.readString(KEY_EQUIPPED, SampleData.DEFAULT_SKIN_ID),
        unlockedSkinIds = store.readSet(KEY_UNLOCKED, setOf(SampleData.DEFAULT_SKIN_ID)),
        lastBonusClaimMs = store.readLong(KEY_BONUS_TIME, 0L),
        bonusDay = store.readInt(KEY_BONUS_DAY, 1),
        soundEnabled = store.readBoolean(KEY_SOUND, true),
        hapticsEnabled = store.readBoolean(KEY_HAPTICS, true),
        reducedMotion = store.readBoolean(KEY_MOTION, false)
    )

    override fun saveRound(result: RoundResult): PlayerProfile {
        val current = profile()
        store.writeInt(KEY_POINTS, current.strikePoints + result.score)
        store.writeInt(KEY_BEST_SCORE, maxOf(current.bestScore, result.score))
        store.writeInt(KEY_BEST_STREAK, maxOf(current.bestStreak, result.bestStreak))
        store.writeInt(KEY_ROUNDS, current.roundsPlayed + 1)
        store.writeInt(KEY_RINGS, current.ringsHit + result.ringsHit)
        store.writeInt(KEY_SHOTS, current.shotsFired + result.shots)
        return profile()
    }

    override fun equipSkin(skinId: String): PlayerProfile {
        store.writeString(KEY_EQUIPPED, skinId)
        return profile()
    }

    override fun unlockSkins(skinIds: Set<String>): PlayerProfile {
        if (skinIds.isNotEmpty()) {
            store.writeSet(KEY_UNLOCKED, profile().unlockedSkinIds + skinIds)
        }
        return profile()
    }

    override fun addPoints(amount: Int): PlayerProfile {
        store.writeInt(KEY_POINTS, profile().strikePoints + amount)
        return profile()
    }

    override fun markDailyClaimed(day: Int, timestampMs: Long): PlayerProfile {
        store.writeInt(KEY_BONUS_DAY, day)
        store.writeLong(KEY_BONUS_TIME, timestampMs)
        return profile()
    }

    override fun updatePreferences(
        soundEnabled: Boolean,
        hapticsEnabled: Boolean,
        reducedMotion: Boolean
    ): PlayerProfile {
        store.writeBoolean(KEY_SOUND, soundEnabled)
        store.writeBoolean(KEY_HAPTICS, hapticsEnabled)
        store.writeBoolean(KEY_MOTION, reducedMotion)
        return profile()
    }

    override fun resetProgress(): PlayerProfile {
        store.clear(
            listOf(
                KEY_POINTS, KEY_BEST_SCORE, KEY_BEST_STREAK, KEY_ROUNDS, KEY_RINGS,
                KEY_SHOTS, KEY_EQUIPPED, KEY_UNLOCKED, KEY_BONUS_TIME, KEY_BONUS_DAY
            )
        )
        return profile()
    }

    private companion object {
        const val KEY_POINTS = "strike_points"
        const val KEY_BEST_SCORE = "best_score"
        const val KEY_BEST_STREAK = "best_streak"
        const val KEY_ROUNDS = "rounds_played"
        const val KEY_RINGS = "rings_hit"
        const val KEY_SHOTS = "shots_fired"
        const val KEY_EQUIPPED = "equipped_skin"
        const val KEY_UNLOCKED = "unlocked_skins"
        const val KEY_BONUS_TIME = "bonus_claim_ms"
        const val KEY_BONUS_DAY = "bonus_day"
        const val KEY_SOUND = "sound_enabled"
        const val KEY_HAPTICS = "haptics_enabled"
        const val KEY_MOTION = "reduced_motion"
    }
}
