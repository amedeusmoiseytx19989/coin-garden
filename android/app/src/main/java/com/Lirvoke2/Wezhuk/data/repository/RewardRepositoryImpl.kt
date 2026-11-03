package com.Lirvoke2.Wezhuk.data.repository

import com.Lirvoke2.Wezhuk.data.local.CoinStrikePreferences
import com.Lirvoke2.Wezhuk.domain.repository.RewardRepository

class RewardRepositoryImpl(
    private val store: CoinStrikePreferences
) : RewardRepository {

    override fun claimedIds(): Set<String> = store.readSet(KEY_CLAIMED, emptySet())

    override fun markClaimed(rewardId: String): Set<String> {
        val updated = claimedIds() + rewardId
        store.writeSet(KEY_CLAIMED, updated)
        return updated
    }

    override fun clearClaims(): Set<String> {
        store.clear(listOf(KEY_CLAIMED))
        return emptySet()
    }

    private companion object {
        const val KEY_CLAIMED = "claimed_rewards"
    }
}
