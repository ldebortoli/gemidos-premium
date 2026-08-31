package com.gemidospremium.app.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gemidospremium.app.localization.AppLanguage
import com.gemidospremium.app.localization.GemidosText
import com.gemidospremium.app.localization.GemidosTextCatalog
import com.gemidospremium.app.localization.TextKey
import com.gemidospremium.app.model.ErrorTarget
import com.gemidospremium.app.model.GemidosState
import com.gemidospremium.app.model.PrankPhase
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val PremiumGold = Color(0xFFE7C978)
private val MutedRose = Color(0xFFB5798D)
private val DeepRose = Color(0xFF38222A)
private val PremiumGradient = Brush.horizontalGradient(
    listOf(Color(0xFF8F6877), Color(0xFFB58A77), Color(0xFFD1B66D)),
)

@Composable
fun GemidosPremiumScreen(
    state: GemidosState,
    text: GemidosText,
    selectedLanguage: AppLanguage,
    adsReady: Boolean,
    adUnitId: String,
    isDemo: Boolean,
    onCountdownTick: () -> Unit,
    onRestart: () -> Unit,
    onPremium: () -> Unit,
    onNormalOff: () -> Unit,
    onConfirmPurchase: () -> Unit,
    onDismissPurchase: () -> Unit,
    onResetDemoPremium: () -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit,
) {
    LaunchedEffect(state.phase, state.countdown) {
        if (state.phase == PrankPhase.COUNTDOWN && state.countdown > 0) {
            delay(1_000)
            onCountdownTick()
        }
    }

    val celebration = remember { Animatable(0f) }
    var celebrationVisible by remember { mutableStateOf(false) }
    LaunchedEffect(state.celebrationSequence, state.dismissedCelebrationSequence) {
        celebrationVisible = false
        if (state.celebrationSequence > state.dismissedCelebrationSequence) {
            try {
                celebrationVisible = true
                celebration.snapTo(0f)
                celebration.animateTo(1f, tween(3_200, easing = LinearEasing))
            } finally {
                celebrationVisible = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0B0B0D), Color(0xFF151014), Color(0xFF09090B)),
                ),
            ),
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                if (adsReady && !state.isPremiumOwned && adUnitId.isNotBlank()) {
                    GemidosAdBanner(
                        adUnitId = adUnitId,
                        isDemo = isDemo,
                        testAdLabel = text[TextKey.TEST_AD],
                        modifier = Modifier.navigationBarsPadding(),
                    )
                }
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AppHeader(state.isPremiumOwned, text)
                LanguageSelector(text, selectedLanguage, onLanguageSelected)
                Spacer(Modifier.height(18.dp))
                AudioHero(state, text)
                Spacer(Modifier.height(22.dp))

                AnimatedContent(targetState = state.phase, label = "estado-gemidos") { phase ->
                    when (phase) {
                        PrankPhase.PLAYING -> PlayingActions(
                            state = state,
                            text = text,
                            onPremium = onPremium,
                            onNormalOff = onNormalOff,
                        )

                        PrankPhase.SILENCED,
                        PrankPhase.ERROR -> IdleActions(
                            state = state,
                            text = text,
                            isDemo = isDemo,
                            onRestart = onRestart,
                            onResetDemoPremium = onResetDemoPremium,
                        )

                        PrankPhase.COUNTDOWN,
                        PrankPhase.PREMIUM_SILENCING -> Unit
                    }
                }
                Spacer(Modifier.height(18.dp))
            }
        }

        if (celebrationVisible) {
            PremiumDopamineOverlay(
                progress = celebration.value,
                contentDescription = text[TextKey.FIREWORKS_A11Y],
                modifier = Modifier.matchParentSize(),
            )
        }
    }

    if (state.showPurchaseDialog) {
        PremiumPurchaseDialog(
            priceLabel = state.priceLabel,
            text = text,
            isDemo = isDemo,
            onConfirm = onConfirmPurchase,
            onDismiss = onDismissPurchase,
        )
    }
}

@Composable
private fun AppHeader(isPremiumOwned: Boolean, text: GemidosText) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = "GEMIDOS",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.7.sp,
            )
            Text(
                text = "PREMIUM",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
            )
        }
        Surface(
            color = if (isPremiumOwned) Color(0xFF30291A) else Color(0xFF211A1E),
            shape = RoundedCornerShape(999.dp),
        ) {
            Text(
                text = if (isPremiumOwned) text[TextKey.PREMIUM_ACTIVE] else text[TextKey.PLEBEIAN_EDITION],
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                color = if (isPremiumOwned) PremiumGold else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
            )
        }
    }
}

@Composable
private fun LanguageSelector(
    text: GemidosText,
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Box {
            TextButton(
                onClick = { expanded = true },
                modifier = Modifier.semantics { contentDescription = text[TextKey.APP_LANGUAGE] },
            ) {
                Text(
                    text = "🌐 ${text[TextKey.LANGUAGES]} · ${selectedLanguage.nativeLabel}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                scrollState = rememberScrollState(),
                modifier = Modifier
                    .heightIn(max = 360.dp)
                    .widthIn(min = 220.dp, max = 320.dp),
            ) {
                GemidosTextCatalog.supportedLanguages.forEach { language ->
                    DropdownMenuItem(
                        text = {
                            Text(if (language == selectedLanguage) "✓ ${language.nativeLabel}" else language.nativeLabel)
                        },
                        onClick = {
                            expanded = false
                            onLanguageSelected(language)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun AudioHero(state: GemidosState, text: GemidosText) {
    val transition = rememberInfiniteTransition(label = "pulso-audio")
    val pulse by transition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(620),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "escala-audio",
    )
    val active = state.phase == PrankPhase.PLAYING || state.phase == PrankPhase.PREMIUM_SILENCING

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF181317),
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 4.dp,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (active) {
                    Canvas(Modifier.size(154.dp)) {
                        repeat(3) { index ->
                            drawCircle(
                                color = MutedRose.copy(alpha = 0.16f - index * 0.03f),
                                radius = size.minDimension * (0.27f + index * 0.09f) * pulse,
                                style = Stroke(width = 2.dp.toPx()),
                            )
                        }
                    }
                }
                Surface(
                    modifier = Modifier.size(112.dp),
                    shape = CircleShape,
                    color = if (active) DeepRose else Color(0xFF232126),
                ) {
                    AudioGlyph(
                        isPlaying = active,
                        contentDescription = text[TextKey.FLASHLIGHT_ICON],
                        modifier = Modifier.padding(24.dp),
                    )
                }
            }
            Spacer(Modifier.height(18.dp))
            when (state.phase) {
                PrankPhase.COUNTDOWN -> {
                    Text(
                        text = text[TextKey.READY_TO_LIGHT],
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp,
                    )
                    Text(
                        text = state.countdown.toString(),
                        color = PremiumGold,
                        fontSize = 76.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 82.sp,
                    )
                }

                PrankPhase.PLAYING -> {
                    Text(
                        text = text[TextKey.TORCH_ON_MAX],
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = text[TextKey.CHOOSE_ELEGANCE],
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }

                PrankPhase.PREMIUM_SILENCING -> {
                    Text(
                        text = text[TextKey.PREMIUM_OFF_PROGRESS],
                        color = PremiumGold,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = text[TextKey.FIVE_STAR_DARKNESS],
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                PrankPhase.SILENCED,
                PrankPhase.ERROR -> Text(
                    text = text[TextKey.TORCH_ALREADY_OFF],
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun PlayingActions(
    state: GemidosState,
    text: GemidosText,
    onPremium: () -> Unit,
    onNormalOff: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        state.error
            ?.takeIf { state.errorTarget == ErrorTarget.PREMIUM }
            ?.let { ActionError(it) }
        Button(
            onClick = onPremium,
            modifier = Modifier
                .fillMaxWidth()
                .height(112.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PremiumGold,
                contentColor = Color(0xFF2D2410),
            ),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = text[TextKey.PREMIUM_OFF],
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = if (state.isPremiumOwned) text[TextKey.ALREADY_YOURS] else text[TextKey.FIVE_STAR_DARKNESS],
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Spacer(Modifier.height(14.dp))
        state.error
            ?.takeIf { state.errorTarget == ErrorTarget.NORMAL }
            ?.let { ActionError(it) }
        Text(
            text = text[TextKey.NORMAL_PLEBEIAN_OFF],
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .clickable(onClick = onNormalOff)
                .padding(vertical = 14.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun IdleActions(
    state: GemidosState,
    text: GemidosText,
    isDemo: Boolean,
    onRestart: () -> Unit,
    onResetDemoPremium: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        state.notice?.let { NoticeText(it) }
        state.error?.let { ActionError(it) }
        Button(
            onClick = onRestart,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 58.dp),
            shape = RoundedCornerShape(18.dp),
        ) {
            Text(text[TextKey.TURN_ON], fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
        }
        if (isDemo && state.isPremiumOwned) {
            TextButton(onClick = onResetDemoPremium, modifier = Modifier.padding(top = 10.dp)) {
                Text(text[TextKey.RESET_PLEBEIAN])
            }
            Text(
                text = text[TextKey.RESET_HELP],
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun PremiumPurchaseDialog(
    priceLabel: String?,
    text: GemidosText,
    isDemo: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text[TextKey.PREMIUM_OFF]) },
        text = {
            Column {
                Text(
                    text = if (isDemo) text[TextKey.LOCAL_TEST] else priceLabel ?: text[TextKey.PRICE_GOOGLE_PLAY],
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = text[TextKey.ONE_TIME_NO_SUBSCRIPTION],
                    modifier = Modifier.padding(top = 4.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = if (isDemo) text[TextKey.DEMO_PURCHASE_HELP] else text[TextKey.PLAY_PURCHASE_HELP],
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(if (isDemo) text[TextKey.SIMULATE_PURCHASE] else text[TextKey.CONTINUE_PLAY])
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text[TextKey.NOT_NOW]) }
        },
    )
}

@Composable
private fun AudioGlyph(isPlaying: Boolean, contentDescription: String, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.semantics { this.contentDescription = contentDescription }) {
        val color = if (isPlaying) Color(0xFFE4B6C4) else Color(0xFFAAA1A8)
        val center = Offset(size.width * 0.45f, size.height * 0.5f)
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.08f, size.height * 0.35f),
            size = androidx.compose.ui.geometry.Size(size.width * 0.28f, size.height * 0.30f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(5.dp.toPx()),
        )
        val speaker = androidx.compose.ui.graphics.Path().apply {
            moveTo(size.width * 0.32f, size.height * 0.36f)
            lineTo(size.width * 0.56f, size.height * 0.18f)
            lineTo(size.width * 0.56f, size.height * 0.82f)
            lineTo(size.width * 0.32f, size.height * 0.64f)
            close()
        }
        drawPath(speaker, color)
        repeat(2) { index ->
            drawArc(
                color = color,
                startAngle = -55f,
                sweepAngle = 110f,
                useCenter = false,
                topLeft = Offset(center.x - size.width * (0.02f + index * 0.08f), center.y - size.height * (0.22f + index * 0.10f)),
                size = androidx.compose.ui.geometry.Size(size.width * (0.36f + index * 0.16f), size.height * (0.44f + index * 0.20f)),
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round),
            )
        }
    }
}

@Composable
private fun PremiumDopamineOverlay(
    progress: Float,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    val colors = listOf(
        PremiumGold,
        Color(0xFFFF6B8A),
        Color(0xFF63D9FF),
        Color(0xFF91F29B),
        Color(0xFFC68CFF),
    )
    val reelSymbols = listOf("7", "★", "◆", "♥")
    val reelStep = (progress * 28).toInt()

    Box(
        modifier = modifier.semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.matchParentSize()) {
            val flash = ((sin(progress * PI * 8).toFloat() + 1f) / 2f).coerceIn(0f, 1f)
            drawRect(
                color = colors[reelStep % colors.size],
                alpha = 0.06f + flash * 0.13f,
            )

            repeat(64) { index ->
                val side = index / 16
                val position = (index % 16) / 15f
                val point = when (side) {
                    0 -> Offset(position * size.width, 12.dp.toPx())
                    1 -> Offset(size.width - 12.dp.toPx(), position * size.height)
                    2 -> Offset((1f - position) * size.width, size.height - 12.dp.toPx())
                    else -> Offset(12.dp.toPx(), (1f - position) * size.height)
                }
                val lit = (index + reelStep) % 4 == 0
                drawCircle(
                    color = colors[index % colors.size].copy(alpha = if (lit) 0.95f else 0.20f),
                    radius = (if (lit) 5f else 3f) * density,
                    center = point,
                )
            }

            repeat(120) { index ->
                val x = ((index * 37) % 101) / 100f * size.width
                val initialY = ((index * 53) % 103) / 103f
                val y = ((initialY + progress * (1.7f + index % 4 * 0.12f)) % 1.12f) * size.height
                drawCircle(
                    color = colors[index % colors.size].copy(alpha = 0.82f),
                    radius = (2.4f + index % 4) * density,
                    center = Offset(x, y),
                )
            }

            val burstCenters = listOf(
                Offset(size.width * 0.18f, size.height * 0.24f),
                Offset(size.width * 0.78f, size.height * 0.22f),
                Offset(size.width * 0.23f, size.height * 0.70f),
                Offset(size.width * 0.82f, size.height * 0.68f),
                Offset(size.width * 0.50f, size.height * 0.13f),
            )
            burstCenters.forEachIndexed { burstIndex, center ->
                val burstProgress = (progress * 2.2f + burstIndex * 0.19f) % 1f
                repeat(22) { ray ->
                    val angle = ray * (2.0 * PI / 22.0) + burstIndex * 0.31
                    val distance = size.minDimension * (0.035f + burstProgress * 0.24f)
                    val direction = Offset(cos(angle).toFloat(), sin(angle).toFloat())
                    val end = center + direction * distance
                    drawLine(
                        color = colors[(ray + burstIndex) % colors.size].copy(alpha = 1f - burstProgress),
                        start = center + direction * distance * 0.45f,
                        end = end,
                        strokeWidth = (1.2f + ray % 3) * density,
                        cap = StrokeCap.Round,
                    )
                    drawCircle(
                        color = colors[(ray + burstIndex) % colors.size].copy(alpha = 1f - burstProgress),
                        radius = (2f + ray % 2) * density,
                        center = end,
                    )
                }
            }
        }

        Surface(
            color = Color(0xED160F19),
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 12.dp,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "★ PREMIUM ×777 ★",
                    color = PremiumGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                )
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    repeat(3) { reel ->
                        Surface(
                            modifier = Modifier.size(width = 64.dp, height = 78.dp),
                            color = Color(0xFF2A2029),
                            shape = RoundedCornerShape(14.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = reelSymbols[(reelStep + reel * 2) % reelSymbols.size],
                                    color = colors[(reelStep + reel) % colors.size],
                                    fontSize = 38.sp,
                                    fontWeight = FontWeight.Black,
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "★★★★★",
                    color = colors[reelStep % colors.size],
                    letterSpacing = 5.sp,
                    fontWeight = FontWeight.Black,
                )
            }
        }
    }
}

@Composable
private fun NoticeText(message: String) {
    Text(
        text = message,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .background(Color(0xFF1C2823), RoundedCornerShape(12.dp))
            .padding(12.dp),
        color = Color(0xFFBFE7D2),
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun ActionError(message: String) {
    Text(
        text = message,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .background(Color(0xFF382025), RoundedCornerShape(12.dp))
            .padding(11.dp),
        color = MaterialTheme.colorScheme.error,
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
    )
}
