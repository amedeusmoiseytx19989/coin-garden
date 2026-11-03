package com.Lirvoke2.Wezhuk.presentation.gameover

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Lirvoke2.Wezhuk.R
import com.Lirvoke2.Wezhuk.core.di.ServiceLocator
import com.Lirvoke2.Wezhuk.core.navigation.Screen
import com.Lirvoke2.Wezhuk.core.theme.AccentGold
import com.Lirvoke2.Wezhuk.core.theme.AccentLight
import com.Lirvoke2.Wezhuk.core.theme.BackgroundDarker
import com.Lirvoke2.Wezhuk.core.theme.BackgroundDeep
import com.Lirvoke2.Wezhuk.core.theme.PrimaryTeal
import com.Lirvoke2.Wezhuk.core.theme.SecondaryAqua
import com.Lirvoke2.Wezhuk.core.theme.SurfaceDeep
import com.Lirvoke2.Wezhuk.core.theme.TextPrimary
import com.Lirvoke2.Wezhuk.core.theme.TextSecondary
import com.Lirvoke2.Wezhuk.core.ui.PrimaryButton
import com.Lirvoke2.Wezhuk.core.ui.SecondaryButton
import com.Lirvoke2.Wezhuk.core.ui.SectionHint
import com.Lirvoke2.Wezhuk.core.ui.StatCard
import com.Lirvoke2.Wezhuk.core.ui.formatPoints

@Composable
fun GameOverScreen(
    destination: Screen.GameOver,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(
        key = "gameover-${destination.token}",
        factory = ServiceLocator.factory
    )
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    var target by remember { mutableStateOf(0) }
    LaunchedEffect(destination.token) { target = destination.score }
    val shownScore by animateIntAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = 900),
        label = "scoreCount"
    )

    val decor = rememberInfiniteTransition(label = "resultDecor")
    val bob by decor.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "trophyBob"
    )

    val accuracy = if (destination.shots <= 0) {
        0
    } else {
        (destination.ringsHit * 100) / destination.shots
    }

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
                            BackgroundDarker.copy(alpha = 0.92f),
                            PrimaryTeal.copy(alpha = 0.30f),
                            BackgroundDeep.copy(alpha = 0.90f)
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 120.dp)
                .size(280.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(AccentGold.copy(alpha = 0.18f), Color.Transparent)
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = if (destination.score > 0) {
                    stringResource(R.string.result_title)
                } else {
                    stringResource(R.string.result_title_blank)
                },
                style = MaterialTheme.typography.displaySmall,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            Image(
                painter = painterResource(
                    if (destination.newBest) R.drawable.sprite_5 else R.drawable.sprite_3
                ),
                contentDescription = stringResource(R.string.trophy_description),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(if (destination.newBest) 88.dp else 76.dp)
                    .graphicsLayer {
                        translationY = bob * density
                        alpha = if (destination.newBest) 1f else 0.85f
                    }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = formatPoints(shownScore),
                style = MaterialTheme.typography.displayMedium,
                color = AccentGold,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
            Text(
                text = stringResource(R.string.result_points),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )

            if (destination.newBest) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_star),
                        contentDescription = null,
                        tint = AccentLight,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.new_best),
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            val labelRings = stringResource(R.string.stat_rings)
            val labelStreak = stringResource(R.string.stat_streak)
            val labelAccuracy = stringResource(R.string.stat_accuracy)
            val labelBest = stringResource(R.string.stat_best)
            val labelRounds = stringResource(R.string.stat_rounds)
            val accuracyText = stringResource(R.string.percent_format, accuracy)

            val cards = ArrayList<Triple<String, String, Color>>()
            if (destination.ringsHit > 0) {
                cards.add(Triple(labelRings, destination.ringsHit.toString(), SecondaryAqua))
            }
            if (destination.bestStreak > 0) {
                cards.add(Triple(labelStreak, destination.bestStreak.toString(), AccentGold))
            }
            if (accuracy > 0) {
                cards.add(Triple(labelAccuracy, accuracyText, TextPrimary))
            }
            if (cards.size < 2 && state.profile.bestScore > 0) {
                cards.add(Triple(labelBest, formatPoints(state.profile.bestScore), AccentLight))
            }
            if (cards.size < 2 && state.profile.roundsPlayed > 0) {
                cards.add(
                    Triple(labelRounds, state.profile.roundsPlayed.toString(), SecondaryAqua)
                )
            }

            if (cards.size >= 2) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    cards.take(3).forEach { card ->
                        StatCard(
                            label = card.first,
                            value = card.second,
                            accent = card.third,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (destination.unlockedCoinLabel != 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color.Transparent,
                    border = BorderStroke(width = 1.dp, color = SecondaryAqua.copy(alpha = 0.55f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(SurfaceDeep.copy(alpha = 0.8f))
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sprite_4),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(
                                R.string.unlock_banner,
                                stringResource(destination.unlockedCoinLabel)
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = SecondaryAqua
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(
                    R.string.rank_line,
                    state.rank.index,
                    stringResource(state.rank.labelRes)
                ),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.weight(1f))

            SectionHint(text = stringResource(R.string.result_hint))

            PrimaryButton(
                text = stringResource(R.string.play_again),
                icon = R.drawable.ic_refresh,
                onClick = onPlayAgain,
                height = 68.dp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            SecondaryButton(
                text = stringResource(R.string.menu),
                iconRes = R.drawable.icon_home,
                onClick = onMenu,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
