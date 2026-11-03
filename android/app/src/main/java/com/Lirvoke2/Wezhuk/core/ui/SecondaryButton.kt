package com.Lirvoke2.Wezhuk.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ButtonDefaults
import com.Lirvoke2.Wezhuk.core.theme.SecondaryAqua
import com.Lirvoke2.Wezhuk.core.theme.SurfaceCard
import com.Lirvoke2.Wezhuk.core.theme.TextPrimary

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    glyphRes: Int? = null,
    iconRes: Int? = null,
    enabled: Boolean = true,
    height: Dp = 52.dp
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "secondaryPress"
    )

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = SecondaryAqua.copy(alpha = if (enabled) 0.5f else 0.2f)
        ),
        contentPadding = PaddingValues(horizontal = 16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SurfaceCard.copy(alpha = if (enabled) 0.8f else 0.35f),
            contentColor = TextPrimary,
            disabledContainerColor = SurfaceCard.copy(alpha = 0.3f),
            disabledContentColor = TextPrimary.copy(alpha = 0.45f)
        ),
        modifier = modifier
            .height(height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (iconRes != null) {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
            } else if (glyphRes != null) {
                Icon(
                    painter = painterResource(glyphRes),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
