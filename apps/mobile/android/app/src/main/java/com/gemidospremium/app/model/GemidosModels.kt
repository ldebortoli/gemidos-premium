package com.gemidospremium.app.model

enum class ErrorTarget {
    AUDIO,
    PREMIUM,
    NORMAL,
}

enum class PrankPhase {
    COUNTDOWN,
    PLAYING,
    PREMIUM_SILENCING,
    SILENCED,
    ERROR,
}

data class GemidosState(
    val countdown: Int = 10,
    val phase: PrankPhase = PrankPhase.COUNTDOWN,
    val isPremiumOwned: Boolean = false,
    val showPurchaseDialog: Boolean = false,
    val priceLabel: String? = null,
    val notice: String? = null,
    val error: String? = null,
    val errorTarget: ErrorTarget? = null,
    val celebrationSequence: Int = 0,
    val dismissedCelebrationSequence: Int = 0,
)

sealed interface AudioResult {
    data object Success : AudioResult
    data class Failure(val message: String) : AudioResult
}

sealed interface GemidosEffect {
    data object None : GemidosEffect
    data object StartAudio : GemidosEffect
    data object StopAudio : GemidosEffect
    data object LaunchGooglePlay : GemidosEffect
    data object RunPremiumSilence : GemidosEffect
}

data class EngineResult(
    val state: GemidosState,
    val effect: GemidosEffect = GemidosEffect.None,
)
