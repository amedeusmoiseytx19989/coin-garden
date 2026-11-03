package com.Lirvoke2.Wezhuk.presentation.app

import androidx.lifecycle.ViewModel
import com.Lirvoke2.Wezhuk.core.navigation.Screen
import com.Lirvoke2.Wezhuk.domain.model.RoundResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    private val backStack = mutableListOf<Screen>()
    private var token = 0

    fun showMenu() {
        backStack.clear()
        _screen.value = Screen.Menu(nextToken())
        syncBack()
    }

    fun showGame() {
        val current = _screen.value
        if (current is Screen.Menu) {
            backStack.add(current)
        }
        _screen.value = Screen.Game(nextToken())
        syncBack()
    }

    fun showGameOver(result: RoundResult, newBest: Boolean, unlockedCoinLabel: Int) {
        _screen.value = Screen.GameOver(
            token = nextToken(),
            score = result.score,
            ringsHit = result.ringsHit,
            shots = result.shots,
            bestStreak = result.bestStreak,
            newBest = newBest,
            unlockedCoinLabel = unlockedCoinLabel
        )
        syncBack()
    }

    fun back() {
        val previous = backStack.removeLastOrNull() ?: return
        _screen.value = when (previous) {
            is Screen.Menu -> Screen.Menu(nextToken())
            else -> previous
        }
        syncBack()
    }

    private fun syncBack() {
        _canGoBack.value = backStack.isNotEmpty()
    }

    private fun nextToken(): Int {
        token += 1
        return token
    }
}
