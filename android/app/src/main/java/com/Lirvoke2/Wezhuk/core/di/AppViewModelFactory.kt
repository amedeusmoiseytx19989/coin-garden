package com.Lirvoke2.Wezhuk.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Lirvoke2.Wezhuk.domain.repository.PlayerRepository
import com.Lirvoke2.Wezhuk.domain.repository.RewardRepository
import com.Lirvoke2.Wezhuk.presentation.app.AppViewModel
import com.Lirvoke2.Wezhuk.presentation.game.GameViewModel
import com.Lirvoke2.Wezhuk.presentation.gameover.GameOverViewModel
import com.Lirvoke2.Wezhuk.presentation.menu.MenuViewModel
import com.Lirvoke2.Wezhuk.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val players: PlayerRepository,
    private val rewards: RewardRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(players, rewards)
            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(players)
            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(players, rewards)
            else -> throw IllegalArgumentException(modelClass.name)
        }
        @Suppress("UNCHECKED_CAST")
        return created as T
    }
}
