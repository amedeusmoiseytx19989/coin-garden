package com.Lirvoke2.Wezhuk.domain.repository

interface RewardRepository {

    fun claimedIds(): Set<String>

    fun markClaimed(rewardId: String): Set<String>

    fun clearClaims(): Set<String>
}
