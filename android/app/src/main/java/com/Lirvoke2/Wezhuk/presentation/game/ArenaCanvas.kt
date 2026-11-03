package com.Lirvoke2.Wezhuk.presentation.game

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import com.Lirvoke2.Wezhuk.core.theme.AccentGold
import com.Lirvoke2.Wezhuk.core.theme.AccentLight
import com.Lirvoke2.Wezhuk.core.theme.PrimaryTeal
import com.Lirvoke2.Wezhuk.core.theme.SecondaryAqua
import com.Lirvoke2.Wezhuk.core.theme.WarnMiss
import com.Lirvoke2.Wezhuk.domain.model.CoinPoint
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ArenaCanvas(
    sweep: Float,
    showReticle: Boolean,
    trail: List<CoinPoint>,
    marker: HitMarker?,
    resolveProgress: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val unit = density

        drawCircle(
            color = PrimaryTeal.copy(alpha = 0.16f),
            radius = width * 0.46f,
            center = Offset(width * 0.5f, height * 0.42f)
        )
        drawCircle(
            color = SecondaryAqua.copy(alpha = 0.08f),
            radius = width * 0.26f,
            center = Offset(width * 0.5f, height * 0.42f)
        )

        if (showReticle) {
            val arcTop = height * 0.70f
            val arcSize = Size(width = width * 0.78f, height = height * 0.34f)
            drawArc(
                color = AccentGold.copy(alpha = 0.26f),
                startAngle = 200f,
                sweepAngle = 140f,
                useCenter = false,
                topLeft = Offset(width * 0.11f, arcTop),
                size = arcSize,
                style = Stroke(width = 2f * unit)
            )
            val cx = width * sweep
            val cy = height * 0.84f
            val arm = 9f * unit
            drawLine(
                color = AccentLight,
                start = Offset(cx - arm, cy),
                end = Offset(cx + arm, cy),
                strokeWidth = 2.5f * unit
            )
            drawLine(
                color = AccentLight,
                start = Offset(cx, cy - arm),
                end = Offset(cx, cy + arm),
                strokeWidth = 2.5f * unit
            )
            drawCircle(
                color = AccentGold.copy(alpha = 0.7f),
                radius = arm * 0.7f,
                center = Offset(cx, cy),
                style = Stroke(width = 1.6f * unit)
            )
        }

        trail.forEachIndexed { index, point ->
            val fade = 1f - index / (trail.size + 1f)
            drawCircle(
                color = AccentGold.copy(alpha = 0.28f * fade),
                radius = (5f - index * 0.6f).coerceAtLeast(1.5f) * unit,
                center = Offset(point.x * width, point.y * height)
            )
        }

        if (marker != null) {
            val center = Offset(marker.x * width, marker.y * height)
            if (marker.hit) {
                val reach = 60f * unit * resolveProgress
                val alpha = (1f - resolveProgress).coerceIn(0f, 1f)
                repeat(12) { index ->
                    val angle = (index * 30f) * PI.toFloat() / 180f
                    val inner = reach * 0.35f
                    drawLine(
                        color = AccentGold.copy(alpha = alpha),
                        start = Offset(
                            center.x + cos(angle) * inner,
                            center.y + sin(angle) * inner
                        ),
                        end = Offset(
                            center.x + cos(angle) * reach,
                            center.y + sin(angle) * reach
                        ),
                        strokeWidth = 2f * unit
                    )
                }
            } else {
                val radius = (10f + 32f * resolveProgress) * unit
                drawCircle(
                    color = WarnMiss.copy(alpha = (1f - resolveProgress) * 0.7f),
                    radius = radius,
                    center = center,
                    style = Stroke(width = 2f * unit)
                )
            }
        }
    }
}
