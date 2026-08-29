package com.gemidospremium.app.ports

import com.gemidospremium.app.model.AudioResult

interface PrankAudioPort {
    fun playAtMaximum(onCompleted: () -> Unit, onError: (String) -> Unit): AudioResult
    fun setRelativeVolume(relativeVolume: Float): AudioResult
    fun stopAndRestore(): AudioResult
}

interface PremiumStore {
    fun isPremiumOwned(): Boolean
    fun setPremiumOwned(owned: Boolean)
}
