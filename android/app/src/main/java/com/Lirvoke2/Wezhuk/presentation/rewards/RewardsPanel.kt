package com.Lirvoke2.Wezhuk.presentation.rewards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.Lirvoke2.Wezhuk.core.theme.SurfaceCard
import com.Lirvoke2.Wezhuk.core.theme.TextPrimary
import com.Lirvoke2.Wezhuk.core.theme.TextSecondary
import com.Lirvoke2.Wezhuk.core.ui.GlassPanel
import com.Lirvoke2.Wezhuk.core.ui.SecondaryButton
import com.Lirvoke2.Wezhuk.core.ui.formatPoints
import com.Lirvoke2.Wezhuk.domain.model.RewardProgress

@Composable
fun RewardsPanel(
    rewards: List<RewardProgress>,
    onClaim: (String) -> Unit,
    onClose: () -> Unit
) {
    GlassPanel(
        title = stringResource(R.string.rewards_title),
        closeLabel = stringResource(R.string.close),
        onClose = onClose
    ) {
        if (rewards.isEmpty()) {
            Text(
                text = stringResource(R.string.rewards_empty),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        }
        rewards.forEach { progress ->
            RewardRow(progress = progress, onClaim = onClaim)
        }
    }
}

@Composable
private fun RewardRow(
    progress: RewardProgress,
    onClaim: (String) -> Unit
) {
    val accent = when {
        progress.claimed -> SecondaryAqua
        progress.ready -> AccentGold
        else -> PrimaryTeal
    }
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        border = BorderStroke(width = 1.dp, color = accent.copy(alpha = 0.38f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(SurfaceCard.copy(alpha = 0.76f))
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(progress.reward.labelRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                Text(
                    text = stringResource(
                        R.string.reward_value,
                        formatPoints(progress.reward.points)
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = accent
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(PrimaryTeal.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.fraction.coerceIn(0.02f, 1f))
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            Brush.horizontalGradient(colors = listOf(accent, AccentLight))
                        )
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        R.string.reward_progress,
                        progress.current.coerceAtMost(progress.reward.target),
                        progress.reward.target
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.width(12.dp))
                if (progress.claimed) {
                    Text(
                        text = stringResource(R.string.claimed),
                        style = MaterialTheme.typography.labelSmall,
                        color = SecondaryAqua
                    )
                } else {
                    SecondaryButton(
                        text = stringResource(R.string.claim),
                        onClick = { onClaim(progress.reward.id) },
                        enabled = progress.ready,
                        height = 48.dp
                    )
                }
            }
        }
    }
}
