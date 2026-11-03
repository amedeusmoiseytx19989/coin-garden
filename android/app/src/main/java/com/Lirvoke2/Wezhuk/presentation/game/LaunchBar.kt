package com.Lirvoke2.Wezhuk.presentation.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.Lirvoke2.Wezhuk.R
import com.Lirvoke2.Wezhuk.core.theme.AccentGold
import com.Lirvoke2.Wezhuk.core.theme.SurfaceDeep
import com.Lirvoke2.Wezhuk.core.theme.TextPrimary
import com.Lirvoke2.Wezhuk.core.theme.TextSecondary
import com.Lirvoke2.Wezhuk.core.ui.PrimaryButton
import com.Lirvoke2.Wezhuk.domain.model.CoinSkin

@Composable
fun LaunchBar(
    coinsLeft: Int,
    coinBank: Int,
    skin: CoinSkin,
    enabled: Boolean,
    onLaunch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(26.dp),
        color = Color.Transparent,
        border = BorderStroke(width = 1.dp, color = AccentGold.copy(alpha = 0.4f)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .background(SurfaceDeep.copy(alpha = 0.88f))
                .height(88.dp)
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.width(58.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = stringResource(R.string.bank_format, coinsLeft, coinBank),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextPrimary,
                    maxLines = 1
                )
            }

            PrimaryButton(
                text = stringResource(R.string.fire),
                icon = R.drawable.ic_play,
                onClick = onLaunch,
                enabled = enabled,
                height = 64.dp,
                stateLabel = if (enabled) {
                    stringResource(R.string.fire_state_ready)
                } else {
                    stringResource(R.string.fire_state_busy)
                },
                modifier = Modifier.weight(1f)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.width(58.dp)
            ) {
                Image(
                    painter = painterResource(skin.drawableRes),
                    contentDescription = stringResource(skin.labelRes),
                    contentScale = ContentScale.Fit,
                    colorFilter = if (skin.tinted) {
                        ColorFilter.tint(SurfaceDeep.copy(alpha = 0.55f))
                    } else {
                        null
                    },
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(skin.labelRes),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
