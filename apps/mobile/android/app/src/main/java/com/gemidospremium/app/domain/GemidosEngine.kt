package com.gemidospremium.app.domain

import com.gemidospremium.app.localization.AppLanguage
import com.gemidospremium.app.localization.GemidosText
import com.gemidospremium.app.localization.GemidosTextCatalog
import com.gemidospremium.app.localization.TextKey
import com.gemidospremium.app.model.EngineResult
import com.gemidospremium.app.model.ErrorTarget
import com.gemidospremium.app.model.GemidosEffect
import com.gemidospremium.app.model.GemidosState
import com.gemidospremium.app.model.PrankPhase
import com.gemidospremium.app.ports.PremiumStore

class GemidosEngine(
    private val premiumStore: PremiumStore,
    private val text: () -> GemidosText = {
        GemidosTextCatalog.forLanguage(AppLanguage.SPANISH_ARGENTINA)
    },
) {
    var state = GemidosState(isPremiumOwned = premiumStore.isPremiumOwned())
        private set

    fun tickCountdown(): EngineResult {
        if (state.phase != PrankPhase.COUNTDOWN) return EngineResult(state)
        val next = state.countdown - 1
        state = state.copy(
            countdown = next,
            phase = if (next == 0) PrankPhase.PLAYING else PrankPhase.COUNTDOWN,
            notice = null,
            error = null,
            errorTarget = null,
        )
        return EngineResult(
            state = state,
            effect = if (next == 0) GemidosEffect.StartAudio else GemidosEffect.None,
        )
    }

    fun restartCountdown(): GemidosState {
        state = state.copy(
            countdown = 10,
            phase = PrankPhase.COUNTDOWN,
            showPurchaseDialog = false,
            notice = null,
            error = null,
            errorTarget = null,
            dismissedCelebrationSequence = state.celebrationSequence,
        )
        return state
    }

    fun audioCompleted(): GemidosState {
        if (state.phase != PrankPhase.PLAYING) return state
        state = state.copy(
            phase = PrankPhase.SILENCED,
            notice = text()[TextKey.TORCH_ALREADY_OFF],
            error = null,
            errorTarget = null,
        )
        return state
    }

    fun audioFailed(message: String): GemidosState {
        state = state.copy(
            phase = PrankPhase.ERROR,
            error = message,
            errorTarget = ErrorTarget.AUDIO,
            notice = null,
        )
        return state
    }

    fun pause(): EngineResult {
        state = state.copy(
            phase = PrankPhase.SILENCED,
            showPurchaseDialog = false,
            notice = null,
            error = null,
            errorTarget = null,
            dismissedCelebrationSequence = state.celebrationSequence,
        )
        return EngineResult(state, GemidosEffect.StopAudio)
    }

    fun turnOffNormally(): EngineResult {
        state = state.copy(
            phase = PrankPhase.SILENCED,
            showPurchaseDialog = false,
            notice = text()[TextKey.NORMAL_OFF_NOTICE],
            error = null,
            errorTarget = null,
            dismissedCelebrationSequence = state.celebrationSequence,
        )
        return EngineResult(state, GemidosEffect.StopAudio)
    }

    fun pressPremium(): EngineResult {
        if (state.phase != PrankPhase.PLAYING) return EngineResult(state)
        if (state.isPremiumOwned) {
            state = state.copy(
                phase = PrankPhase.PREMIUM_SILENCING,
                showPurchaseDialog = false,
                notice = null,
                error = null,
                errorTarget = null,
                celebrationSequence = state.celebrationSequence + 1,
            )
            return EngineResult(state, GemidosEffect.RunPremiumSilence)
        }
        state = state.copy(showPurchaseDialog = true, error = null, errorTarget = null)
        return EngineResult(state)
    }

    fun premiumSilenceCompleted(): GemidosState {
        state = state.copy(
            phase = PrankPhase.SILENCED,
            showPurchaseDialog = false,
            notice = text()[TextKey.PREMIUM_OFF_NOTICE],
            error = null,
            errorTarget = null,
        )
        return state
    }

    fun premiumSilenceFailed(message: String): GemidosState {
        state = state.copy(
            phase = PrankPhase.SILENCED,
            showPurchaseDialog = false,
            error = message,
            errorTarget = ErrorTarget.PREMIUM,
            notice = null,
        )
        return state
    }

    fun confirmPremiumPurchase(isDemo: Boolean): EngineResult {
        if (!state.showPurchaseDialog) return EngineResult(state)
        if (!isDemo) {
            state = state.copy(showPurchaseDialog = false)
            return EngineResult(state, GemidosEffect.LaunchGooglePlay)
        }
        premiumStore.setPremiumOwned(true)
        val shouldSilence = state.phase == PrankPhase.PLAYING
        state = state.copy(
            isPremiumOwned = true,
            showPurchaseDialog = false,
            phase = if (shouldSilence) PrankPhase.PREMIUM_SILENCING else state.phase,
            notice = text()[TextKey.DEMO_PREMIUM_ACTIVATED],
            error = null,
            errorTarget = null,
            celebrationSequence = if (shouldSilence) state.celebrationSequence + 1 else state.celebrationSequence,
        )
        return EngineResult(
            state,
            if (shouldSilence) GemidosEffect.RunPremiumSilence else GemidosEffect.None,
        )
    }

    fun resetPremiumForTesting(): EngineResult {
        premiumStore.setPremiumOwned(false)
        state = state.copy(
            isPremiumOwned = false,
            phase = PrankPhase.SILENCED,
            showPurchaseDialog = false,
            notice = text()[TextKey.DEMO_PREMIUM_RESET],
            error = null,
            errorTarget = null,
            dismissedCelebrationSequence = state.celebrationSequence,
        )
        return EngineResult(state, GemidosEffect.StopAudio)
    }

    fun dismissPurchase(): GemidosState {
        state = state.copy(showPurchaseDialog = false, error = null, errorTarget = null)
        return state
    }

    fun billingPurchased(): EngineResult {
        premiumStore.setPremiumOwned(true)
        val shouldSilence = state.phase == PrankPhase.PLAYING
        state = state.copy(
            isPremiumOwned = true,
            phase = if (shouldSilence) PrankPhase.PREMIUM_SILENCING else state.phase,
            showPurchaseDialog = false,
            notice = text()[TextKey.PREMIUM_ACTIVATED],
            error = null,
            errorTarget = null,
            celebrationSequence = if (shouldSilence) state.celebrationSequence + 1 else state.celebrationSequence,
        )
        return EngineResult(
            state,
            if (shouldSilence) GemidosEffect.RunPremiumSilence else GemidosEffect.None,
        )
    }

    fun billingFailed(message: String): GemidosState {
        state = state.copy(
            showPurchaseDialog = false,
            error = message,
            errorTarget = ErrorTarget.PREMIUM,
        )
        return state
    }

    fun updatePrice(price: String): GemidosState {
        state = state.copy(priceLabel = price)
        return state
    }

    fun languageChanged(): GemidosState {
        state = state.copy(notice = null, error = null, errorTarget = null)
        return state
    }
}
