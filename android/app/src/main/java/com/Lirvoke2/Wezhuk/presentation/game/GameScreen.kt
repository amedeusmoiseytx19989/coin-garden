package com.Lirvoke2.Wezhuk.presentation.game

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Lirvoke2.Wezhuk.R
import com.Lirvoke2.Wezhuk.core.config.GameConfig
import com.Lirvoke2.Wezhuk.core.di.ServiceLocator
import com.Lirvoke2.Wezhuk.core.theme.AccentGold
import com.Lirvoke2.Wezhuk.core.theme.BackgroundDarker
import com.Lirvoke2.Wezhuk.core.theme.PrimaryTeal
import com.Lirvoke2.Wezhuk.core.theme.SecondaryAqua
import com.Lirvoke2.Wezhuk.core.theme.SurfaceDeep
import com.Lirvoke2.Wezhuk.core.theme.TextPrimary
import com.Lirvoke2.Wezhuk.core.theme.WarnMiss
import com.Lirvoke2.Wezhuk.core.ui.ScorePill
import com.Lirvoke2.Wezhuk.core.ui.formatClock
import com.Lirvoke2.Wezhuk.core.ui.formatPoints
import com.Lirvoke2.Wezhuk.presentation.dialog.PauseDialog

@Composable
fun GameScreen(
    token: Int,
    onExit: () -> Unit,
    onFinished: (RoundOutcome) -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(
        key = "game-$token",
        factory = ServiceLocator.factory
    )
    val state by gameViewModel.state.collectAsStateWithLifecycle()
    val finishHandler by rememberUpdatedState(onFinished)

    LaunchedEffect(Unit) {
        gameViewModel.events.collect { outcome -> finishHandler(outcome) }
    }

    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    BackHandler(enabled = state.paused) { gameViewModel.resume() }

    val sweepTransition = rememberInfiniteTransition(label = "reticle")
    val sweep by sweepTransition.animateFloat(
        initialValue = GameConfig.SWEEP_MIN_X,
        targetValue = GameConfig.SWEEP_MAX_X,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = GameConfig.SWEEP_PERIOD_MS,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "reticleSweep"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SurfaceDeep.copy(alpha = 0.82f),
                            PrimaryTeal.copy(alpha = 0.24f),
                            BackgroundDarker.copy(alpha = 0.90f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(
                score = state.score,
                streak = state.streak,
                remainingMs = state.remainingMs,
                onPause = { gameViewModel.pause() }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Arena(
                    state = state,
                    sweep = sweep,
                    modifier = Modifier.fillMaxSize()
                )
            }

            LaunchBar(
                coinsLeft = state.coinsLeft,
                coinBank = state.coinBank,
                skin = state.skin,
                enabled = state.canLaunch,
                onLaunch = { gameViewModel.fireCoin(sweep) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (state.reloadBannerLeftMs > 0L) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.Transparent,
                border = BorderStroke(width = 1.dp, color = AccentGold.copy(alpha = 0.6f)),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .safeDrawingPadding()
                    .padding(top = 92.dp)
            ) {
                Text(
                    text = stringResource(R.string.reload_banner, GameConfig.RELOAD_BONUS),
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentGold,
                    modifier = Modifier
                        .background(SurfaceDeep.copy(alpha = 0.9f))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }

        if (state.paused) {
            PauseDialog(
                onResume = { gameViewModel.resume() },
                onMenu = onExit
            )
        }
    }
}

@Composable
private fun GameHeader(
    score: Int,
    streak: Int,
    remainingMs: Long,
    onPause: () -> Unit
) {
    val urgent = remainingMs <= 10000L
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(SurfaceDeep.copy(alpha = 0.55f))
                .padding(horizontal = 12.dp)
        ) {
            IconButton(
                onClick = onPause,
                modifier = Modifier.size(48.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_pause),
                    contentDescription = stringResource(R.string.pause_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(26.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.round_clock, formatClock(remainingMs)),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (urgent) WarnMiss else TextPrimary,
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(PrimaryTeal.copy(alpha = 0.3f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(
                                (remainingMs.toFloat() / GameConfig.ROUND_DURATION_MS)
                                    .coerceIn(0f, 1f)
                            )
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (urgent) WarnMiss else AccentGold)
                    )
                }
            }
            ScorePill(
                value = formatPoints(score),
                spriteRes = R.drawable.sprite_1,
                stateLabel = stringResource(R.string.score_state, score, streak),
                spriteSize = 18.dp
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(SecondaryAqua.copy(alpha = 0.25f))
        )
    }
}

@Composable
private fun Arena(
    state: GameUiState,
    sweep: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 1.dp,
                color = SecondaryAqua.copy(alpha = 0.22f),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
        ) {
            val fieldWidth = maxWidth
            val fieldHeight = maxHeight

            ArenaCanvas(
                sweep = sweep,
                showReticle = state.phase == GamePhase.READY && !state.paused,
                trail = state.trail,
                marker = state.marker,
                resolveProgress = state.resolveProgress,
                modifier = Modifier.fillMaxSize()
            )

            state.rings.forEach { ring ->
                if (!ring.hidden) {
                    val ringSize = ring.sizeUnits.dp
                    Image(
                        painter = painterResource(R.drawable.sprite_3),
                        contentDescription = stringResource(R.string.ring_description),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(ringSize)
                            .offset(
                                x = anchor(ring.x, fieldWidth, ringSize),
                                y = anchor(ring.laneY, fieldHeight, ringSize)
                            )
                    )
                }
            }

            val coin = state.coin
            if (coin != null) {
                val coinSize = 46.dp
                Image(
                    painter = painterResource(state.skin.drawableRes),
                    contentDescription = stringResource(R.string.coin_description),
                    contentScale = ContentScale.Fit,
                    colorFilter = if (state.skin.tinted) {
                        ColorFilter.tint(SurfaceDeep.copy(alpha = 0.55f))
                    } else {
                        null
                    },
                    modifier = Modifier
                        .size(coinSize)
                        .offset(
                            x = anchor(coin.x, fieldWidth, coinSize),
                            y = anchor(coin.y, fieldHeight, coinSize)
                        )
                        .graphicsLayer { rotationZ = state.coinSpin }
                )
            }

            val marker = state.marker
            if (marker != null && marker.hit && marker.value > 0) {
                val floaterWidth = 88.dp
                val rise = (40.dp * state.resolveProgress)
                Text(
                    text = stringResource(R.string.floater_format, marker.value),
                    style = MaterialTheme.typography.titleMedium,
                    color = AccentGold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(floaterWidth)
                        .offset(
                            x = anchor(marker.x, fieldWidth, floaterWidth),
                            y = (anchor(marker.y, fieldHeight, 28.dp) - rise)
                                .coerceAtLeast(0.dp)
                        )
                        .alpha((1f - state.resolveProgress).coerceIn(0f, 1f))
                )
            }

            Launcher(
                angleFraction = sweep,
                recoil = state.phase == GamePhase.IN_FLIGHT,
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            StreakMeter(
                streak = state.streak,
                multiplier = state.multiplier,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(0.42f)
                    .padding(end = 4.dp)
                    .semantics {
                        liveRegion = LiveRegionMode.Polite
                    }
            )
        }
    }
}

private fun anchor(fraction: Float, extent: Dp, item: Dp): Dp {
    val half = item / 2f
    if (extent <= item) {
        return (extent - item) / 2f
    }
    val center = (extent * fraction).coerceIn(half, extent - half)
    return center - half
}
