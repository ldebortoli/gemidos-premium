package com.gemidospremium.app.domain

import com.gemidospremium.app.model.AudioResult
import com.gemidospremium.app.ports.PrankAudioPort

class PremiumSilenceRunner(
    private val audio: PrankAudioPort,
    private val pause: suspend (Long) -> Unit,
) {
    private val steps = listOf(
        0.82f to 220L,
        0.60f to 260L,
        0.38f to 300L,
        0.18f to 340L,
        0.00f to 380L,
    )

    suspend fun run(): AudioResult {
        var sequenceResult: AudioResult = AudioResult.Success
        try {
            for ((volume, durationMillis) in steps) {
                val stepResult = audio.setRelativeVolume(volume)
                if (stepResult is AudioResult.Failure) {
                    sequenceResult = stepResult
                    break
                }
                pause(durationMillis)
            }
        } finally {
            val stopResult = audio.stopAndRestore()
            if (sequenceResult is AudioResult.Success) sequenceResult = stopResult
        }
        return sequenceResult
    }
}
