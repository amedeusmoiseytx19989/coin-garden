package com.Lirvoke2.Wezhuk.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Lirvoke2.Wezhuk.core.theme.AccentDeep
import com.Lirvoke2.Wezhuk.core.theme.AccentGold
import com.Lirvoke2.Wezhuk.core.theme.AccentLight
import com.Lirvoke2.Wezhuk.core.theme.SurfaceDeep

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Int? = null,
    enabled: Boolean = true,
    height: Dp = 64.dp,
    stateLabel: String? = null
) {
    val shape = RoundedCornerShape(20.dp)
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "primaryPress"
    )
    val gloss = rememberInfiniteTransition(label = "primaryGloss")
    val shift by gloss.animateFloat(
        initialValue = -0.45f,
        targetValue = 1.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "primaryGlossShift"
    )
    val faceBrush = Brush.horizontalGradient(
        colors = if (enabled) {
            listOf(AccentDeep, AccentGold, AccentLight, AccentDeep)
        } else {
            listOf(AccentDeep.copy(alpha = 0.45f), AccentGold.copy(alpha = 0.45f))
        }
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        shape = shape,
        contentPadding = PaddingValues(horizontal = 18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = SurfaceDeep,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = SurfaceDeep.copy(alpha = 0.55f)
        ),
        modifier = modifier
            .height(height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (enabled) 10.dp else 0.dp,
                shape = shape,
                ambientColor = AccentGold,
                spotColor = AccentGold
            )
            .clip(shape)
            .background(faceBrush)
            .drawBehind {
                if (!enabled) {
                    return@drawBehind
                }
                val band = size.width * 0.3f
                val start = shift * (size.width + band) - band
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.18f),
                            Color.Transparent
                        ),
                        startX = start,
                        endX = start + band
                    )
                )
            }
            .then(
                if (stateLabel == null) {
                    Modifier
                } else {
                    Modifier.semantics { stateDescription = stateLabel }
                }
            )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}
