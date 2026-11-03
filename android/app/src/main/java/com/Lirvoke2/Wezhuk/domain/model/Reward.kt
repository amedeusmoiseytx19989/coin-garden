package com.Lirvoke2.Wezhuk.domain.model

enum class RewardMetric { RINGS, STREAK, POINTS, ROUNDS, COINS }

data class Reward(
    val id: String,
    val labelRes: Int,
    val metric: RewardMetric,
    val target: Int,
    val points: Int
)

data class RewardProgress(
    val reward: Reward,
    val current: Int,
    val claimed: Boolean
) {
    val ready: Boolean
        get() = !claimed && current >= reward.target

    val fraction: Float
        get() = if (reward.target <= 0) 0f else (current.toFloat() / reward.target).coerceIn(0f, 1f)
}
