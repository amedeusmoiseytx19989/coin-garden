package com.Lirvoke2.Wezhuk.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Lirvoke2.Wezhuk.core.di.ServiceLocator
import com.Lirvoke2.Wezhuk.core.navigation.Screen
import com.Lirvoke2.Wezhuk.core.theme.BackgroundDarker
import com.Lirvoke2.Wezhuk.presentation.game.GameScreen
import com.Lirvoke2.Wezhuk.presentation.gameover.GameOverScreen
import com.Lirvoke2.Wezhuk.presentation.menu.MenuScreen
import com.Lirvoke2.Wezhuk.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.factory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) { appViewModel.back() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDarker)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                (fadeIn(animationSpec = tween(220)) + scaleIn(initialScale = 0.94f)) togetherWith
                    fadeOut(animationSpec = tween(180))
            },
            label = "screenHost"
        ) { current ->
            when (current) {
                is Screen.Splash -> SplashScreen(onFinished = { appViewModel.showMenu() })

                is Screen.Menu -> MenuScreen(
                    token = current.token,
                    onPlay = { appViewModel.showGame() }
                )

                is Screen.Game -> GameScreen(
                    token = current.token,
                    onExit = { appViewModel.showMenu() },
                    onFinished = { outcome ->
                        appViewModel.showGameOver(
                            result = outcome.result,
                            newBest = outcome.newBest,
                            unlockedCoinLabel = outcome.unlockedCoinLabel
                        )
                    }
                )

                is Screen.GameOver -> GameOverScreen(
                    destination = current,
                    onPlayAgain = { appViewModel.showGame() },
                    onMenu = { appViewModel.showMenu() }
                )
            }
        }
    }
}
