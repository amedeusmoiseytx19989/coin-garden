package com.Lirvoke2.Wezhuk.presentation.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Lirvoke2.Wezhuk.R
import com.Lirvoke2.Wezhuk.core.theme.AccentGold
import com.Lirvoke2.Wezhuk.core.theme.PrimaryTeal
import com.Lirvoke2.Wezhuk.core.theme.SecondaryAqua
import com.Lirvoke2.Wezhuk.core.theme.SurfaceDeep
import com.Lirvoke2.Wezhuk.core.theme.TextSecondary

@Composable
fun StreakMeter(
    streak: Int,
    multiplier: Int,
    modifier: Modifier = Modifier
) {
    val fill by animateFloatAsState(
        targetValue = (streak / 9f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 320),
        label = "streakFill"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceDeep.copy(alpha = 0.85f))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                text = stringResource(R.string.multiplier_format, multiplier),
                style = MaterialTheme.typography.labelSmall,
                color = AccentGold
            )
        }
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .width(8.dp)
                .weight(1f)
                .clip(RoundedCornerShape(4.dp))
                .background(PrimaryTeal.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight(fill.coerceAtLeast(0.02f))
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.verticalGradient(colors = listOf(AccentGold, SecondaryAqua))
                    )
            )
        }
        Text(
            text = stringResource(R.string.streak_label),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}
