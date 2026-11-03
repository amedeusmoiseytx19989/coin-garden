package com.Lirvoke2.Wezhuk.data.sample

import com.Lirvoke2.Wezhuk.R
import com.Lirvoke2.Wezhuk.domain.model.CoinSkin
import com.Lirvoke2.Wezhuk.domain.model.DailyBonus
import com.Lirvoke2.Wezhuk.domain.model.Rank
import com.Lirvoke2.Wezhuk.domain.model.Reward
import com.Lirvoke2.Wezhuk.domain.model.RewardMetric
import com.Lirvoke2.Wezhuk.domain.model.TargetRing

object SampleData {

    const val DEFAULT_SKIN_ID = "brass"

    val coinSkins: List<CoinSkin> = listOf(
        CoinSkin(
            id = "brass",
            labelRes = R.string.coin_brass,
            drawableRes = R.drawable.sprite_1,
            bonusPercent = 0,
            unlockPoints = 0
        ),
        CoinSkin(
            id = "teal_alloy",
            labelRes = R.string.coin_teal,
            drawableRes = R.drawable.sprite_2,
            bonusPercent = 5,
            unlockPoints = 800
        ),
        CoinSkin(
            id = "aurora",
            labelRes = R.string.coin_aurora,
            drawableRes = R.drawable.sprite_4,
            bonusPercent = 10,
            unlockPoints = 2500
        ),
        CoinSkin(
            id = "obsidian",
            labelRes = R.string.coin_obsidian,
            drawableRes = R.drawable.sprite_2,
            bonusPercent = 15,
            unlockPoints = 6000,
            tinted = true
        ),
        CoinSkin(
            id = "solar",
            labelRes = R.string.coin_solar,
            drawableRes = R.drawable.sprite_5,
            bonusPercent = 20,
            unlockPoints = 12000
        )
    )

    val dailyLadder: List<DailyBonus> = listOf(
        DailyBonus(day = 1, points = 50),
        DailyBonus(day = 2, points = 75),
        DailyBonus(day = 3, points = 100),
        DailyBonus(day = 4, points = 150),
        DailyBonus(day = 5, points = 200),
        DailyBonus(day = 6, points = 300),
        DailyBonus(day = 7, points = 500)
    )

    val rankLadder: List<Rank> = listOf(
        Rank(index = 1, labelRes = R.string.rank_rookie, minXp = 0),
        Rank(index = 2, labelRes = R.string.rank_pitcher, minXp = 500),
        Rank(index = 3, labelRes = R.string.rank_sharpshooter, minXp = 1200),
        Rank(index = 4, labelRes = R.string.rank_ringmaster, minXp = 2000),
        Rank(index = 5, labelRes = R.string.rank_gold_striker, minXp = 3500),
        Rank(index = 6, labelRes = R.string.rank_legend, minXp = 6000)
    )

    val milestones: List<Reward> = listOf(
        Reward(
            id = "first_rings",
            labelRes = R.string.reward_rings,
            metric = RewardMetric.RINGS,
            target = 10,
            points = 120
        ),
        Reward(
            id = "streak_eight",
            labelRes = R.string.reward_streak,
            metric = RewardMetric.STREAK,
            target = 8,
            points = 250
        ),
        Reward(
            id = "thousand_points",
            labelRes = R.string.reward_points,
            metric = RewardMetric.POINTS,
            target = 1000,
            points = 300
        ),
        Reward(
            id = "five_rounds",
            labelRes = R.string.reward_rounds,
            metric = RewardMetric.ROUNDS,
            target = 5,
            points = 400
        ),
        Reward(
            id = "full_vault",
            labelRes = R.string.reward_coins,
            metric = RewardMetric.COINS,
            target = 5,
            points = 900
        )
    )

    fun initialRings(): List<TargetRing> = listOf(
        TargetRing(
            id = 0,
            laneY = 0.56f,
            sizeUnits = 88,
            value = 80,
            x = 0.30f,
            speed = 0.085f,
            direction = 1
        ),
        TargetRing(
            id = 1,
            laneY = 0.39f,
            sizeUnits = 72,
            value = 120,
            x = 0.64f,
            speed = 0.115f,
            direction = -1
        ),
        TargetRing(
            id = 2,
            laneY = 0.22f,
            sizeUnits = 60,
            value = 200,
            x = 0.46f,
            speed = 0.145f,
            direction = 1
        )
    )

    fun skinById(id: String): CoinSkin =
        coinSkins.firstOrNull { it.id == id } ?: coinSkins.first()
}
