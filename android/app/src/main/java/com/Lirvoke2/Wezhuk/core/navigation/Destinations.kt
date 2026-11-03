package com.Lirvoke2.Wezhuk.core.navigation

sealed interface Screen {

    data object Splash : Screen

    data class Menu(val token: Int) : Screen

    data class Game(val token: Int) : Screen

    data class GameOver(
        val token: Int,
        val score: Int,
        val ringsHit: Int,
        val shots: Int,
        val bestStreak: Int,
        val newBest: Boolean,
        val unlockedCoinLabel: Int
    ) : Screen
}
