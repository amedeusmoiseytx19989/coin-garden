package com.Lirvoke2.Wezhuk.presentation.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Lirvoke2.Wezhuk.R
import com.Lirvoke2.Wezhuk.core.theme.AccentGold
import com.Lirvoke2.Wezhuk.core.theme.PrimaryTeal
import com.Lirvoke2.Wezhuk.core.theme.SecondaryAqua
import com.Lirvoke2.Wezhuk.core.theme.SurfaceCard
import com.Lirvoke2.Wezhuk.core.theme.SurfaceDeep
import com.Lirvoke2.Wezhuk.core.theme.TextPrimary
import com.Lirvoke2.Wezhuk.core.theme.TextSecondary
import com.Lirvoke2.Wezhuk.core.theme.WarnMiss
import com.Lirvoke2.Wezhuk.core.ui.GlassPanel
import com.Lirvoke2.Wezhuk.core.ui.SecondaryButton
import com.Lirvoke2.Wezhuk.domain.model.PlayerProfile

@Composable
fun SettingsDialog(
    profile: PlayerProfile,
    resetArmed: Boolean,
    onSound: (Boolean) -> Unit,
    onHaptics: (Boolean) -> Unit,
    onReducedMotion: (Boolean) -> Unit,
    onArmReset: () -> Unit,
    onConfirmReset: () -> Unit,
    onClose: () -> Unit
) {
    GlassPanel(
        title = stringResource(R.string.settings_title),
        closeLabel = stringResource(R.string.close),
        onClose = onClose
    ) {
        ToggleRow(
            label = stringResource(R.string.settings_sound),
            checked = profile.soundEnabled,
            onCheckedChange = onSound
        )
        ToggleRow(
            label = stringResource(R.string.settings_haptics),
            checked = profile.hapticsEnabled,
            onCheckedChange = onHaptics
        )
        ToggleRow(
            label = stringResource(R.string.settings_motion),
            checked = profile.reducedMotion,
            onCheckedChange = onReducedMotion
        )

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.Transparent,
            border = BorderStroke(width = 1.dp, color = WarnMiss.copy(alpha = 0.45f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(SurfaceDeep.copy(alpha = 0.7f))
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.settings_reset_warning),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                SecondaryButton(
                    text = if (resetArmed) {
                        stringResource(R.string.settings_reset_confirm)
                    } else {
                        stringResource(R.string.settings_reset)
                    },
                    onClick = if (resetArmed) onConfirmReset else onArmReset,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Text(
            text = stringResource(R.string.settings_version),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}

@Composable
private fun ToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val stateLabel = if (checked) {
        stringResource(R.string.state_on)
    } else {
        stringResource(R.string.state_off)
    }
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        border = BorderStroke(width = 1.dp, color = SecondaryAqua.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .background(SurfaceCard.copy(alpha = 0.7f))
                .heightIn(min = 56.dp)
                .padding(horizontal = 14.dp, vertical = 6.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = SurfaceDeep,
                    checkedTrackColor = AccentGold,
                    uncheckedThumbColor = TextSecondary,
                    uncheckedTrackColor = PrimaryTeal.copy(alpha = 0.4f)
                ),
                modifier = Modifier.semantics { stateDescription = stateLabel }
            )
        }
    }
}
