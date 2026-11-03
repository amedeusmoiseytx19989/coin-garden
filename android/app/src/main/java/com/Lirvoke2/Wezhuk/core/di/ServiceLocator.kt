package com.Lirvoke2.Wezhuk.core.di

import android.content.Context
import com.Lirvoke2.Wezhuk.data.local.CoinStrikePreferences
import com.Lirvoke2.Wezhuk.data.repository.PlayerRepositoryImpl
import com.Lirvoke2.Wezhuk.data.repository.RewardRepositoryImpl

object ServiceLocator {

    private var cached: AppViewModelFactory? = null

    fun init(context: Context) {
        if (cached != null) {
            return
        }
        val store = CoinStrikePreferences(context.applicationContext)
        cached = AppViewModelFactory(
            players = PlayerRepositoryImpl(store),
            rewards = RewardRepositoryImpl(store)
        )
    }

    val factory: AppViewModelFactory
        get() = requireNotNull(cached)
}
