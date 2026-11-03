package com.Lirvoke2.Wezhuk.presentation.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.Lirvoke2.Wezhuk.core.theme.AccentDeep
import com.Lirvoke2.Wezhuk.core.theme.AccentGold
import com.Lirvoke2.Wezhuk.core.theme.AccentLight
import com.Lirvoke2.Wezhuk.core.theme.SurfaceDeep

@Composable
fun Launcher(
    angleFraction: Float,
    recoil: Boolean,
    modifier: Modifier = Modifier
) {
    val angle by animateFloatAsState(
        targetValue = (angleFraction - 0.5f) * 64f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 260f),
        label = "barrelAngle"
    )
    val kick by animateFloatAsState(
        targetValue = if (recoil) 6f else 0f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 500f),
        label = "barrelRecoil"
    )

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.size(width = 76.dp, height = 70.dp)
    ) {
        Box(
            modifier = Modifier
                .width(18.dp)
                .height(48.dp)
                .offset(y = (-14).dp)
                .graphicsLayer {
                    rotationZ = angle
                    translationY = kick * this.density
                    transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 1f)
                }
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(AccentLight, AccentGold, AccentDeep)
                    ),
                    shape = RoundedCornerShape(topStart = 9.dp, topEnd = 9.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(40.dp)
                    .offset(x = 4.dp, y = 4.dp)
                    .background(Color.White.copy(alpha = 0.35f), RoundedCornerShape(2.dp))
            )
        }
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(AccentGold, AccentDeep, SurfaceDeep)
                    ),
                    shape = CircleShape
                )
        )
    }
}
