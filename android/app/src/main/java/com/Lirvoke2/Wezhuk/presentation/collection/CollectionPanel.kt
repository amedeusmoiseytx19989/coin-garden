package com.Lirvoke2.Wezhuk.presentation.collection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Lirvoke2.Wezhuk.R
import com.Lirvoke2.Wezhuk.core.theme.AccentGold
import com.Lirvoke2.Wezhuk.core.theme.SecondaryAqua
import com.Lirvoke2.Wezhuk.core.theme.SurfaceCard
import com.Lirvoke2.Wezhuk.core.theme.SurfaceDeep
import com.Lirvoke2.Wezhuk.core.theme.TextPrimary
import com.Lirvoke2.Wezhuk.core.theme.TextSecondary
import com.Lirvoke2.Wezhuk.core.ui.GlassPanel
import com.Lirvoke2.Wezhuk.core.ui.SecondaryButton
import com.Lirvoke2.Wezhuk.core.ui.formatPoints
import com.Lirvoke2.Wezhuk.presentation.menu.CoinEntry

@Composable
fun CollectionPanel(
    coins: List<CoinEntry>,
    onEquip: (String) -> Unit,
    onClose: () -> Unit
) {
    GlassPanel(
        title = stringResource(R.string.collection_title),
        closeLabel = stringResource(R.string.close),
        onClose = onClose
    ) {
        coins.chunked(2).forEach { pair ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                pair.forEach { entry ->
                    CoinCell(entry = entry, onEquip = onEquip, modifier = Modifier.weight(1f))
                }
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun RowScope.CoinCell(
    entry: CoinEntry,
    onEquip: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = if (entry.equipped) AccentGold else SecondaryAqua
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.Transparent,
        border = BorderStroke(width = 1.dp, color = accent.copy(alpha = 0.4f)),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .background(SurfaceCard.copy(alpha = 0.78f))
                .padding(horizontal = 10.dp, vertical = 14.dp)
        ) {
            Image(
                painter = painterResource(entry.skin.drawableRes),
                contentDescription = stringResource(entry.skin.labelRes),
                contentScale = ContentScale.Fit,
                colorFilter = if (entry.skin.tinted) {
                    ColorFilter.tint(SurfaceDeep.copy(alpha = 0.55f))
                } else {
                    null
                },
                modifier = Modifier.size(56.dp)
            )
            Text(
                text = stringResource(entry.skin.labelRes),
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = if (entry.skin.bonusPercent > 0) {
                    stringResource(R.string.coin_bonus, entry.skin.bonusPercent)
                } else {
                    stringResource(R.string.coin_bonus_none)
                },
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            when {
                entry.equipped -> Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = AccentGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.equipped),
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentGold
                    )
                }

                entry.unlocked -> SecondaryButton(
                    text = stringResource(R.string.equip),
                    onClick = { onEquip(entry.skin.id) },
                    height = 48.dp,
                    modifier = Modifier.fillMaxWidth()
                )

                else -> Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_lock),
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(
                            R.string.coin_locked,
                            formatPoints(entry.skin.unlockPoints)
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
