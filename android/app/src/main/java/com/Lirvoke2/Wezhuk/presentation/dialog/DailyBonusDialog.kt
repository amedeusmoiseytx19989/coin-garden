package com.Lirvoke2.Wezhuk.presentation.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Lirvoke2.Wezhuk.R
import com.Lirvoke2.Wezhuk.core.theme.AccentGold
import com.Lirvoke2.Wezhuk.core.theme.SecondaryAqua
import com.Lirvoke2.Wezhuk.core.theme.SurfaceCard
import com.Lirvoke2.Wezhuk.core.theme.TextPrimary
import com.Lirvoke2.Wezhuk.core.theme.TextSecondary
import com.Lirvoke2.Wezhuk.core.ui.GlassPanel
import com.Lirvoke2.Wezhuk.core.ui.PrimaryButton
import com.Lirvoke2.Wezhuk.core.ui.formatCountdown
import com.Lirvoke2.Wezhuk.core.ui.formatPoints
import com.Lirvoke2.Wezhuk.domain.model.DailyBonus
import com.Lirvoke2.Wezhuk.domain.model.DailyBonusState

@Composable
fun DailyBonusDialog(
    daily: DailyBonusState,
    ladder: List<DailyBonus>,
    onClaim: () -> Unit,
    onClose: () -> Unit
) {
    GlassPanel(
        title = stringResource(R.string.daily_title),
        closeLabel = stringResource(R.string.close),
        onClose = onClose
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = stringResource(R.string.trophy_description),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(56.dp)
            )
            Text(
                text = if (daily.available) {
                    stringResource(R.string.daily_ready, daily.day)
                } else {
                    stringResource(R.string.daily_cooldown, formatCountdown(daily.remainingMs))
                },
                style = MaterialTheme.typography.bodyLarge,
                color = if (daily.available) AccentGold else TextSecondary
            )
        }

        ladder.chunked(4).forEach { group ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                group.forEach { tier ->
                    DayTile(
                        tier = tier,
                        current = tier.day == daily.day,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (group.size < 4) {
                    repeat(4 - group.size) {
                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        PrimaryButton(
            text = stringResource(R.string.claim),
            icon = R.drawable.ic_refresh,
            onClick = onClaim,
            enabled = daily.available,
            height = 56.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DayTile(
    tier: DailyBonus,
    current: Boolean,
    modifier: Modifier = Modifier
) {
    val accent = if (current) AccentGold else SecondaryAqua
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent,
        border = BorderStroke(width = if (current) 2.dp else 1.dp, color = accent.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .background(SurfaceCard.copy(alpha = if (current) 0.9f else 0.6f))
                .padding(vertical = 10.dp, horizontal = 4.dp)
        ) {
            Text(
                text = stringResource(R.string.daily_day, tier.day),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Text(
                text = formatPoints(tier.points),
                style = MaterialTheme.typography.bodyMedium,
                color = if (current) AccentGold else TextPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}
