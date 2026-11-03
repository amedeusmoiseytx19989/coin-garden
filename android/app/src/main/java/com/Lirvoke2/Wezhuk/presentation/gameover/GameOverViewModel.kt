package com.Lirvoke2.Wezhuk.presentation.gameover

import androidx.lifecycle.ViewModel
import com.Lirvoke2.Wezhuk.data.sample.SampleData
import com.Lirvoke2.Wezhuk.domain.model.PlayerProfile
import com.Lirvoke2.Wezhuk.domain.model.RankState
import com.Lirvoke2.Wezhuk.domain.repository.PlayerRepository
import com.Lirvoke2.Wezhuk.domain.repository.RewardRepository
import com.Lirvoke2.Wezhuk.domain.usecase.ResolveRankUseCase
import com.Lirvoke2.Wezhuk.domain.usecase.UnlockCoinUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GameOverUiState(
    val profile: PlayerProfile,
    val rank: RankState,
    val unlockedCoins: Int,
    val totalCoins: Int,
    val pendingRewards: Int
)

class GameOverViewModel(
    private val players: PlayerRepository,
    private val rewards: RewardRepository
) : ViewModel() {

    private val resolveRank = ResolveRankUseCase()
    private val unlockCoins = UnlockCoinUseCase()

    private val _state = MutableStateFlow(snapshot())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    private fun snapshot(): GameOverUiState {
        val profile = players.profile()
        val claimed = rewards.claimedIds()
        val unlocked = unlockCoins.unlockedCount(SampleData.coinSkins, profile.unlockedSkinIds)
        return GameOverUiState(
            profile = profile,
            rank = resolveRank.resolve(SampleData.rankLadder, profile.strikePoints),
            unlockedCoins = unlocked,
            totalCoins = SampleData.coinSkins.size,
            pendingRewards = SampleData.milestones.count { it.id !in claimed }
        )
    }
}
