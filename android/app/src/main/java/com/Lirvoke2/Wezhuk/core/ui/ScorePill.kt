package com.Lirvoke2.Wezhuk.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Lirvoke2.Wezhuk.core.theme.AccentGold
import com.Lirvoke2.Wezhuk.core.theme.SurfaceCard

@Composable
fun ScorePill(
    value: String,
    spriteRes: Int,
    stateLabel: String,
    modifier: Modifier = Modifier,
    accent: Color = AccentGold,
    spriteSize: Dp = 20.dp,
    pulseKey: Int = 0
) {
    val pulse by animateFloatAsState(
        targetValue = 1f + (pulseKey % 2) * 0.06f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 320f),
        label = "pillPulse"
    )
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.Transparent,
        border = BorderStroke(width = 1.dp, color = accent.copy(alpha = 0.45f)),
        modifier = modifier.graphicsLayer {
            scaleX = pulse
            scaleY = pulse
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(SurfaceCard.copy(alpha = 0.82f))
                .padding(horizontal = 12.dp, vertical = 7.dp)
                .semantics {
                    liveRegion = LiveRegionMode.Polite
                    stateDescription = stateLabel
                }
        ) {
            Image(
                painter = painterResource(spriteRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(spriteSize)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = accent
            )
        }
    }
}
