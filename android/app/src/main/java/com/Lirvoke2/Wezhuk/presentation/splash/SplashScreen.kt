package com.Lirvoke2.Wezhuk.presentation.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Lirvoke2.Wezhuk.R
import com.Lirvoke2.Wezhuk.core.theme.AccentGold
import com.Lirvoke2.Wezhuk.core.theme.BackgroundDarker
import com.Lirvoke2.Wezhuk.core.theme.BackgroundDeep
import com.Lirvoke2.Wezhuk.core.theme.PrimaryTeal
import com.Lirvoke2.Wezhuk.core.theme.SecondaryAqua
import com.Lirvoke2.Wezhuk.core.theme.SplashTop
import com.Lirvoke2.Wezhuk.core.theme.TextPrimary
import com.Lirvoke2.Wezhuk.core.theme.TextSecondary
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Lirvoke2.Wezhuk.core.di.ServiceLocator
import kotlin.random.Random

private data class Mote(val x: Float, val y: Float, val radius: Float, val alpha: Float, val drift: Float)

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = ServiceLocator.factory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()
    var handedOff by remember { mutableStateOf(false) }

    LaunchedEffect(finished) {
        if (finished && !handedOff) {
            handedOff = true
            onFinished()
        }
    }

    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { revealed = true }
    val reveal by animateFloatAsState(
        targetValue = if (revealed) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "splashReveal"
    )

    val motes = remember {
        val random = Random(seed = 4821)
        List(26) {
            Mote(
                x = random.nextFloat(),
                y = random.nextFloat(),
                radius = 1.5f + random.nextFloat() * 2f,
                alpha = 0.10f + random.nextFloat() * 0.16f,
                drift = 0.4f + random.nextFloat() * 0.6f
            )
        }
    }

    val decor = rememberInfiniteTransition(label = "splashDecor")
    val drift by decor.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "splashDrift"
    )
    val ring by decor.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "splashRing"
    )
    val breathe by decor.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "splashBreathe"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SplashTop.copy(alpha = 0.94f),
                            BackgroundDeep.copy(alpha = 0.90f),
                            BackgroundDarker.copy(alpha = 0.97f)
                        )
                    )
                )
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = AccentGold.copy(alpha = 0.12f),
                radius = size.minDimension * 0.42f,
                center = Offset(size.width * 0.5f, size.height * 0.40f)
            )
            drawCircle(
                color = PrimaryTeal.copy(alpha = 0.14f),
                radius = size.minDimension * 0.30f,
                center = Offset(size.width * 0.5f, size.height * 0.40f)
            )
            motes.forEach { mote ->
                val y = (mote.y - drift * mote.drift + 1f) % 1f
                drawCircle(
                    color = AccentGold.copy(alpha = mote.alpha),
                    radius = mote.radius * density,
                    center = Offset(mote.x * size.width, y * size.height)
                )
            }
            val ringRadius = (96f + 54f * ring) * density
            drawCircle(
                color = AccentGold.copy(alpha = 0.5f * (1f - ring)),
                radius = ringRadius,
                center = Offset(size.width * 0.5f, size.height * 0.40f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f * density)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.logo_description),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(168.dp)
                    .graphicsLayer {
                        val entry = 0.86f + 0.14f * reveal
                        scaleX = entry * breathe
                        scaleY = entry * breathe
                        alpha = reveal
                    }
            )
            Spacer(modifier = Modifier.height(26.dp))
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.brand_title),
                    style = MaterialTheme.typography.displaySmall,
                    color = AccentGold.copy(alpha = 0.35f * reveal),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.offset(x = 2.dp, y = 2.dp)
                )
                Text(
                    text = stringResource(R.string.brand_title),
                    style = MaterialTheme.typography.displaySmall,
                    color = TextPrimary.copy(alpha = reveal),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.brand_tagline),
                style = MaterialTheme.typography.labelLarge,
                color = SecondaryAqua.copy(alpha = reveal),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(44.dp))
            LinearProgressIndicator(
                color = AccentGold,
                trackColor = PrimaryTeal.copy(alpha = 0.30f),
                modifier = Modifier
                    .width(180.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}
