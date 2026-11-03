package com.Lirvoke2.Wezhuk.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.Lirvoke2.Wezhuk.core.theme.AccentGold
import com.Lirvoke2.Wezhuk.core.theme.BackgroundDarker
import com.Lirvoke2.Wezhuk.core.theme.BackgroundDeep
import com.Lirvoke2.Wezhuk.core.theme.SurfaceDeep
import com.Lirvoke2.Wezhuk.core.theme.TextPrimary

@Composable
fun GlassPanel(
    title: String,
    closeLabel: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrimInteraction = remember { MutableInteractionSource() }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDarker.copy(alpha = 0.72f))
            .clickable(
                interactionSource = scrimInteraction,
                indication = null,
                onClick = onClose
            )
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.Transparent,
            border = BorderStroke(width = 1.dp, color = AccentGold.copy(alpha = 0.35f)),
            modifier = Modifier
                .safeDrawingPadding()
                .padding(horizontal = 18.dp, vertical = 24.dp)
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                BackgroundDeep.copy(alpha = 0.97f),
                                SurfaceDeep.copy(alpha = 0.97f)
                            )
                        )
                    )
                    .padding(horizontal = 18.dp, vertical = 20.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .heightIn(max = 440.dp)
                        .verticalScroll(rememberScrollState()),
                    content = content
                )
                SecondaryButton(
                    text = closeLabel,
                    onClick = onClose,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
