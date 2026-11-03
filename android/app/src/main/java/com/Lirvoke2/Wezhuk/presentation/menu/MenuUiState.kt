package com.Lirvoke2.Wezhuk.presentation.menu

import com.Lirvoke2.Wezhuk.domain.model.CoinSkin
import com.Lirvoke2.Wezhuk.domain.model.DailyBonus
import com.Lirvoke2.Wezhuk.domain.model.DailyBonusState
import com.Lirvoke2.Wezhuk.domain.model.PlayerProfile
import com.Lirvoke2.Wezhuk.domain.model.RankState
import com.Lirvoke2.Wezhuk.domain.model.RewardProgress

enum class MenuOverlay { NONE, COLLECTION, REWARDS, PROGRESS, DAILY, SETTINGS, RULES }

data class CoinEntry(
    val skin: CoinSkin,
    val unlocked: Boolean,
    val equipped: Boolean
)

data class MenuUiState(
    val profile: PlayerProfile,
    val rank: RankState,
    val daily: DailyBonusState,
    val coins: List<CoinEntry>,
    val rewards: List<RewardProgress>,
    val ladder: List<DailyBonus>,
    val overlay: MenuOverlay = MenuOverlay.NONE,
    val resetArmed: Boolean = false,
    val pulseKey: Int = 0
) {
    val unlockedCoins: Int
        get() = coins.count { it.unlocked }

    val totalCoins: Int
        get() = coins.size

    val readyRewards: Int
        get() = rewards.count { it.ready }
}
