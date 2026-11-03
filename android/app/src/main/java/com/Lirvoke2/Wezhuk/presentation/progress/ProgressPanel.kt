package com.Lirvoke2.Wezhuk.presentation.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Lirvoke2.Wezhuk.R
import com.Lirvoke2.Wezhuk.core.theme.AccentGold
import com.Lirvoke2.Wezhuk.core.theme.AccentLight
import com.Lirvoke2.Wezhuk.core.theme.PrimaryTeal
import com.Lirvoke2.Wezhuk.core.theme.SecondaryAqua
import com.Lirvoke2.Wezhuk.core.theme.TextPrimary
import com.Lirvoke2.Wezhuk.core.theme.TextSecondary
import com.Lirvoke2.Wezhuk.core.ui.GlassPanel
import com.Lirvoke2.Wezhuk.core.ui.StatCard
import com.Lirvoke2.Wezhuk.core.ui.formatPoints
import com.Lirvoke2.Wezhuk.domain.model.PlayerProfile
import com.Lirvoke2.Wezhuk.domain.model.RankState

@Composable
fun ProgressPanel(
    profile: PlayerProfile,
    rank: RankState,
    onClose: () -> Unit
) {
    GlassPanel(
        title = stringResource(R.string.progress_title),
        closeLabel = stringResource(R.string.close),
        onClose = onClose
    ) {
        Text(
            text = stringResource(R.string.rank_line, rank.index, stringResource(rank.labelRes)),
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
                    .fillMaxWidth(rank.fraction.coerceIn(0.02f, 1f))
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Brush.horizontalGradient(colors = listOf(AccentGold, AccentLight)))
            )
        }
        Text(
            text = stringResource(
                R.string.xp_line,
                formatPoints(rank.currentXp),
                formatPoints(rank.targetXp)
            ),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )

        val labelRounds = stringResource(R.string.stat_rounds)
        val labelRings = stringResource(R.string.stat_rings)
        val labelStreak = stringResource(R.string.stat_streak)
        val labelBest = stringResource(R.string.stat_best)
        val labelShots = stringResource(R.string.stat_shots)
        val labelAccuracy = stringResource(R.string.stat_accuracy)
        val accuracyText = stringResource(R.string.percent_format, profile.accuracy)

        val cards = ArrayList<Triple<String, String, Color>>()
        if (profile.roundsPlayed > 0) {
            cards.add(Triple(labelRounds, profile.roundsPlayed.toString(), SecondaryAqua))
        }
        if (profile.ringsHit > 0) {
            cards.add(Triple(labelRings, profile.ringsHit.toString(), AccentGold))
        }
        if (profile.bestStreak > 0) {
            cards.add(Triple(labelStreak, profile.bestStreak.toString(), AccentLight))
        }
        if (profile.bestScore > 0) {
            cards.add(Triple(labelBest, formatPoints(profile.bestScore), AccentGold))
        }
        if (profile.shotsFired > 0) {
            cards.add(Triple(labelShots, profile.shotsFired.toString(), SecondaryAqua))
        }
        if (profile.accuracy > 0) {
            cards.add(Triple(labelAccuracy, accuracyText, TextPrimary))
        }

        if (cards.size >= 2) {
            cards.chunked(2).forEach { pair ->
                if (pair.size == 2) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        pair.forEach { card ->
                            StatCard(
                                label = card.first,
                                value = card.second,
                                accent = card.third,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        } else {
            Text(
                text = stringResource(R.string.rewards_empty),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        }
    }
}
