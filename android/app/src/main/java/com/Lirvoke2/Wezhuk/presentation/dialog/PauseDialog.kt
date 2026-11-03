package com.Lirvoke2.Wezhuk.presentation.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Lirvoke2.Wezhuk.R
import com.Lirvoke2.Wezhuk.core.theme.TextSecondary
import com.Lirvoke2.Wezhuk.core.ui.GlassPanel
import com.Lirvoke2.Wezhuk.core.ui.PrimaryButton
import com.Lirvoke2.Wezhuk.core.ui.SecondaryButton

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onMenu: () -> Unit
) {
    GlassPanel(
        title = stringResource(R.string.pause_title),
        closeLabel = stringResource(R.string.close),
        onClose = onResume
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(44.dp)
            )
            Text(
                text = stringResource(R.string.pause_body),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        }
        PrimaryButton(
            text = stringResource(R.string.resume),
            icon = R.drawable.ic_play,
            onClick = onResume,
            height = 60.dp,
            modifier = Modifier.fillMaxWidth()
        )
        SecondaryButton(
            text = stringResource(R.string.menu),
            iconRes = R.drawable.icon_home,
            onClick = onMenu,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
