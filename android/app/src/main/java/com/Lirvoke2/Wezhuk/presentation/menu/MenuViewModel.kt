package com.Lirvoke2.Wezhuk.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lirvoke2.Wezhuk.data.sample.SampleData
import com.Lirvoke2.Wezhuk.domain.model.PlayerProfile
import com.Lirvoke2.Wezhuk.domain.model.Reward
import com.Lirvoke2.Wezhuk.domain.model.RewardMetric
import com.Lirvoke2.Wezhuk.domain.model.RewardProgress
import com.Lirvoke2.Wezhuk.domain.repository.PlayerRepository
import com.Lirvoke2.Wezhuk.domain.repository.RewardRepository
import com.Lirvoke2.Wezhuk.domain.usecase.ClaimDailyBonusUseCase
import com.Lirvoke2.Wezhuk.domain.usecase.ResolveRankUseCase
import com.Lirvoke2.Wezhuk.domain.usecase.UnlockCoinUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MenuViewModel(
    private val players: PlayerRepository,
    private val rewards: RewardRepository
) : ViewModel() {

    private val resolveRank = ResolveRankUseCase()
    private val dailyBonus = ClaimDailyBonusUseCase()
    private val unlockCoins = UnlockCoinUseCase()

    private val _state = MutableStateFlow(compose(players.profile(), MenuOverlay.NONE, false, 0))
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        syncUnlocks()
        viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                val snapshot = _state.value
                if (!snapshot.daily.available) {
                    _state.value = compose(
                        snapshot.profile,
                        snapshot.overlay,
                        snapshot.resetArmed,
                        snapshot.pulseKey
                    )
                }
            }
        }
    }

    fun openOverlay(overlay: MenuOverlay) {
        _state.value = _state.value.copy(overlay = overlay, resetArmed = false)
    }

    fun closeOverlay() {
        _state.value = _state.value.copy(overlay = MenuOverlay.NONE, resetArmed = false)
    }

    fun claimDaily() {
        viewModelScope.launch {
            val snapshot = _state.value
            if (!snapshot.daily.available) {
                return@launch
            }
            val credited = players.addPoints(snapshot.daily.points)
            val nextDay = dailyBonus.nextDay(credited.bonusDay)
            val saved = players.markDailyClaimed(nextDay, System.currentTimeMillis())
            publish(saved, snapshot.overlay, bump = true)
        }
    }

    fun equipSkin(skinId: String) {
        viewModelScope.launch {
            val entry = _state.value.coins.firstOrNull { it.skin.id == skinId }
            if (entry == null || !entry.unlocked) {
                return@launch
            }
            publish(players.equipSkin(skinId), _state.value.overlay, bump = false)
        }
    }

    fun claimReward(rewardId: String) {
        viewModelScope.launch {
            val progress = _state.value.rewards.firstOrNull { it.reward.id == rewardId }
            if (progress == null || !progress.ready) {
                return@launch
            }
            rewards.markClaimed(rewardId)
            publish(players.addPoints(progress.reward.points), _state.value.overlay, bump = true)
        }
    }

    fun setSound(enabled: Boolean) {
        updatePreferences(sound = enabled)
    }

    fun setHaptics(enabled: Boolean) {
        updatePreferences(haptics = enabled)
    }

    fun setReducedMotion(enabled: Boolean) {
        updatePreferences(motion = enabled)
    }

    fun armReset() {
        _state.value = _state.value.copy(resetArmed = true)
    }

    fun confirmReset() {
        viewModelScope.launch {
            rewards.clearClaims()
            val cleared = players.resetProgress()
            _state.value = compose(cleared, MenuOverlay.SETTINGS, false, _state.value.pulseKey + 1)
        }
    }

    private fun updatePreferences(
        sound: Boolean? = null,
        haptics: Boolean? = null,
        motion: Boolean? = null
    ) {
        viewModelScope.launch {
            val current = _state.value.profile
            val saved = players.updatePreferences(
                soundEnabled = sound ?: current.soundEnabled,
                hapticsEnabled = haptics ?: current.hapticsEnabled,
                reducedMotion = motion ?: current.reducedMotion
            )
            publish(saved, _state.value.overlay, bump = false)
        }
    }

    private fun syncUnlocks() {
        viewModelScope.launch {
            val current = players.profile()
            val fresh = unlockCoins.newlyUnlocked(
                SampleData.coinSkins,
                current.unlockedSkinIds,
                current.strikePoints
            )
            val profile = if (fresh.isEmpty()) {
                current
            } else {
                players.unlockSkins(fresh.map { it.id }.toSet())
            }
            publish(profile, _state.value.overlay, bump = false)
        }
    }

    private fun publish(profile: PlayerProfile, overlay: MenuOverlay, bump: Boolean) {
        val pulse = if (bump) _state.value.pulseKey + 1 else _state.value.pulseKey
        _state.value = compose(profile, overlay, false, pulse)
    }

    private fun compose(
        profile: PlayerProfile,
        overlay: MenuOverlay,
        resetArmed: Boolean,
        pulseKey: Int
    ): MenuUiState {
        val claimed = rewards.claimedIds()
        val entries = SampleData.coinSkins.map { skin ->
            CoinEntry(
                skin = skin,
                unlocked = skin.id in profile.unlockedSkinIds ||
                    profile.strikePoints >= skin.unlockPoints,
                equipped = skin.id == profile.equippedSkinId
            )
        }
        val unlocked = entries.count { it.unlocked }
        return MenuUiState(
            profile = profile,
            rank = resolveRank.resolve(SampleData.rankLadder, profile.strikePoints),
            daily = dailyBonus.state(profile, SampleData.dailyLadder, System.currentTimeMillis()),
            coins = entries,
            rewards = SampleData.milestones.map { reward ->
                RewardProgress(
                    reward = reward,
                    current = metricValue(reward, profile, unlocked),
                    claimed = reward.id in claimed
                )
            },
            ladder = SampleData.dailyLadder,
            overlay = overlay,
            resetArmed = resetArmed,
            pulseKey = pulseKey
        )
    }

    private fun metricValue(reward: Reward, profile: PlayerProfile, unlockedCoins: Int): Int =
        when (reward.metric) {
            RewardMetric.RINGS -> profile.ringsHit
            RewardMetric.STREAK -> profile.bestStreak
            RewardMetric.POINTS -> profile.strikePoints
            RewardMetric.ROUNDS -> profile.roundsPlayed
            RewardMetric.COINS -> unlockedCoins
        }
}
