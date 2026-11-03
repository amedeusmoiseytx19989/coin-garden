package com.Lirvoke2.Wezhuk.presentation.menu

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Lirvoke2.Wezhuk.R
import com.Lirvoke2.Wezhuk.core.di.ServiceLocator
import com.Lirvoke2.Wezhuk.core.theme.AccentGold
import com.Lirvoke2.Wezhuk.core.theme.AccentLight
import com.Lirvoke2.Wezhuk.core.theme.BackgroundDeep
import com.Lirvoke2.Wezhuk.core.theme.BackgroundDarker
import com.Lirvoke2.Wezhuk.core.theme.BackgroundLifted
import com.Lirvoke2.Wezhuk.core.theme.PrimaryTeal
import com.Lirvoke2.Wezhuk.core.theme.SecondaryAqua
import com.Lirvoke2.Wezhuk.core.theme.SurfaceCard
import com.Lirvoke2.Wezhuk.core.theme.TextPrimary
import com.Lirvoke2.Wezhuk.core.theme.TextSecondary
import com.Lirvoke2.Wezhuk.core.ui.BentoTile
import com.Lirvoke2.Wezhuk.core.ui.PrimaryButton
import com.Lirvoke2.Wezhuk.core.ui.ScorePill
import com.Lirvoke2.Wezhuk.core.ui.SecondaryButton
import com.Lirvoke2.Wezhuk.core.ui.SectionHint
import com.Lirvoke2.Wezhuk.core.ui.formatCountdown
import com.Lirvoke2.Wezhuk.core.ui.formatPoints
import com.Lirvoke2.Wezhuk.presentation.collection.CollectionPanel
import com.Lirvoke2.Wezhuk.presentation.dialog.DailyBonusDialog
import com.Lirvoke2.Wezhuk.presentation.dialog.RulesDialog
import com.Lirvoke2.Wezhuk.presentation.dialog.SettingsDialog
import com.Lirvoke2.Wezhuk.presentation.progress.ProgressPanel
import com.Lirvoke2.Wezhuk.presentation.rewards.RewardsPanel

@Composable
fun MenuScreen(
    token: Int,
    onPlay: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(
        key = "menu-$token",
        factory = ServiceLocator.factory
    )
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    BackHandler(enabled = state.overlay != MenuOverlay.NONE) { menuViewModel.closeOverlay() }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = stringResource(R.string.arena_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            BackgroundDeep.copy(alpha = 0.78f),
                            BackgroundLifted.copy(alpha = 0.70f),
                            BackgroundDarker.copy(alpha = 0.88f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            MenuHeader(
                points = state.profile.strikePoints,
                pulseKey = state.pulseKey
            )

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                MenuTileRow(
                    index = 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    BentoTile(
                        title = stringResource(R.string.tile_daily),
                        subtitle = if (state.daily.available) {
                            stringResource(R.string.tile_daily_ready)
                        } else {
                            stringResource(
                                R.string.tile_daily_next,
                                formatCountdown(state.daily.remainingMs)
                            )
                        },
                        spriteRes = R.drawable.sprite_5,
                        accent = AccentGold,
                        spriteSize = 48.dp,
                        onClick = { menuViewModel.openOverlay(MenuOverlay.DAILY) },
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1.3f)
                    )
                    BentoTile(
                        title = stringResource(R.string.tile_streak),
                        subtitle = stringResource(R.string.stat_streak),
                        value = state.profile.bestStreak.toString(),
                        spriteRes = R.drawable.sprite_4,
                        accent = SecondaryAqua,
                        spriteSize = 34.dp,
                        onClick = { menuViewModel.openOverlay(MenuOverlay.PROGRESS) },
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    )
                }

                MenuTileRow(
                    index = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    BentoTile(
                        title = stringResource(R.string.tile_vault),
                        subtitle = stringResource(
                            R.string.tile_vault_sub,
                            state.unlockedCoins,
                            state.totalCoins
                        ),
                        spriteRes = R.drawable.sprite_2,
                        accent = PrimaryTeal,
                        spriteSize = 44.dp,
                        onClick = { menuViewModel.openOverlay(MenuOverlay.COLLECTION) },
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    )
                    BentoTile(
                        title = stringResource(R.string.tile_rewards),
                        subtitle = if (state.readyRewards > 0) {
                            stringResource(R.string.tile_rewards_sub, state.readyRewards)
                        } else {
                            stringResource(R.string.tile_rewards_none)
                        },
                        spriteRes = R.drawable.sprite_3,
                        accent = AccentGold,
                        spriteSize = 40.dp,
                        onClick = { menuViewModel.openOverlay(MenuOverlay.REWARDS) },
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    )
                }

                MenuTileRow(
                    index = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.86f)
                ) {
                    ProgressTile(
                        rankIndex = state.rank.index,
                        rankLabel = stringResource(state.rank.labelRes),
                        current = state.rank.currentXp,
                        target = state.rank.targetXp,
                        fraction = state.rank.fraction,
                        onClick = { menuViewModel.openOverlay(MenuOverlay.PROGRESS) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            SectionHint(text = stringResource(R.string.menu_hint))

            PrimaryButton(
                text = stringResource(R.string.play),
                icon = R.drawable.ic_play,
                onClick = onPlay,
                height = 72.dp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SecondaryButton(
                    text = stringResource(R.string.rules_cta),
                    glyphRes = R.drawable.ic_info,
                    onClick = { menuViewModel.openOverlay(MenuOverlay.RULES) },
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = stringResource(R.string.settings_cta),
                    glyphRes = R.drawable.ic_settings,
                    onClick = { menuViewModel.openOverlay(MenuOverlay.SETTINGS) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.menu_disclaimer),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        when (state.overlay) {
            MenuOverlay.COLLECTION -> CollectionPanel(
                coins = state.coins,
                onEquip = { menuViewModel.equipSkin(it) },
                onClose = { menuViewModel.closeOverlay() }
            )

            MenuOverlay.REWARDS -> RewardsPanel(
                rewards = state.rewards,
                onClaim = { menuViewModel.claimReward(it) },
                onClose = { menuViewModel.closeOverlay() }
            )

            MenuOverlay.PROGRESS -> ProgressPanel(
                profile = state.profile,
                rank = state.rank,
                onClose = { menuViewModel.closeOverlay() }
            )

            MenuOverlay.DAILY -> DailyBonusDialog(
                daily = state.daily,
                ladder = state.ladder,
                onClaim = { menuViewModel.claimDaily() },
                onClose = { menuViewModel.closeOverlay() }
            )

            MenuOverlay.SETTINGS -> SettingsDialog(
                profile = state.profile,
                resetArmed = state.resetArmed,
                onSound = { menuViewModel.setSound(it) },
                onHaptics = { menuViewModel.setHaptics(it) },
                onReducedMotion = { menuViewModel.setReducedMotion(it) },
                onArmReset = { menuViewModel.armReset() },
                onConfirmReset = { menuViewModel.confirmReset() },
                onClose = { menuViewModel.closeOverlay() }
            )

            MenuOverlay.RULES -> RulesDialog(onClose = { menuViewModel.closeOverlay() })

            MenuOverlay.NONE -> Unit
        }
    }
}

@Composable
private fun MenuHeader(points: Int, pulseKey: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.logo_description),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = stringResource(R.string.brand_title),
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.weight(1f))
        ScorePill(
            value = formatPoints(points),
            spriteRes = R.drawable.sprite_1,
            stateLabel = stringResource(R.string.points_state, points),
            pulseKey = pulseKey
        )
    }
}

@Composable
private fun MenuTileRow(
    index: Int,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val visibleState = remember { MutableTransitionState(false) }
    visibleState.targetState = true
    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(animationSpec = tween(durationMillis = 260, delayMillis = index * 70)) +
            slideInVertically(
                animationSpec = tween(durationMillis = 300, delayMillis = index * 70),
                initialOffsetY = { it / 6 }
            ),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}

@Composable
private fun ProgressTile(
    rankIndex: Int,
    rankLabel: String,
    current: Int,
    target: Int,
    fraction: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)
    Card(
        onClick = onClick,
        shape = shape,
        border = BorderStroke(width = 1.dp, color = AccentGold.copy(alpha = 0.4f)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = modifier.shadow(
            elevation = 6.dp,
            shape = shape,
            ambientColor = PrimaryTeal,
            spotColor = PrimaryTeal
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            SurfaceCard.copy(alpha = 0.92f),
                            AccentGold.copy(alpha = 0.16f)
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.tile_progress),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Image(
                    painter = painterResource(R.drawable.sprite_4),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(30.dp)
                )
            }
            Text(
                text = stringResource(R.string.rank_line, rankIndex, rankLabel),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(PrimaryTeal.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction.coerceIn(0.02f, 1f))
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(AccentGold, AccentLight)
                            )
                        )
                )
            }
            Text(
                text = stringResource(
                    R.string.xp_line,
                    formatPoints(current),
                    formatPoints(target)
                ),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}
