package com.Lirvoke2.Wezhuk.domain.usecase

import com.Lirvoke2.Wezhuk.domain.model.CoinSkin

class UnlockCoinUseCase {

    fun newlyUnlocked(
        skins: List<CoinSkin>,
        unlockedIds: Set<String>,
        lifetimePoints: Int
    ): List<CoinSkin> = skins.filter { skin ->
        skin.id !in unlockedIds && lifetimePoints >= skin.unlockPoints
    }

    fun unlockedCount(skins: List<CoinSkin>, unlockedIds: Set<String>): Int =
        skins.count { it.id in unlockedIds }
}
