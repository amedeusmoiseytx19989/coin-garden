package com.Lirvoke2.Wezhuk.presentation.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Lirvoke2.Wezhuk.R
import com.Lirvoke2.Wezhuk.core.theme.AccentGold
import com.Lirvoke2.Wezhuk.core.theme.SecondaryAqua
import com.Lirvoke2.Wezhuk.core.theme.SurfaceCard
import com.Lirvoke2.Wezhuk.core.theme.TextPrimary
import com.Lirvoke2.Wezhuk.core.theme.TextSecondary
import com.Lirvoke2.Wezhuk.core.ui.GlassPanel

@Composable
fun RulesDialog(onClose: () -> Unit) {
    GlassPanel(
        title = stringResource(R.string.rules_title),
        closeLabel = stringResource(R.string.close),
        onClose = onClose
    ) {
        RuleLine(text = stringResource(R.string.rule_one))
        RuleLine(text = stringResource(R.string.rule_two))
        RuleLine(text = stringResource(R.string.rule_three))
        RuleLine(text = stringResource(R.string.rule_four))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.Transparent,
            border = BorderStroke(width = 1.dp, color = AccentGold.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .background(SurfaceCard.copy(alpha = 0.7f))
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.rules_table),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Text(
                    text = stringResource(R.string.rules_ring_wide),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                Text(
                    text = stringResource(R.string.rules_ring_mid),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                Text(
                    text = stringResource(R.string.rules_ring_tight),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AccentGold
                )
            }
        }
    }
}

@Composable
private fun RuleLine(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color = SecondaryAqua, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary
        )
    }
}
