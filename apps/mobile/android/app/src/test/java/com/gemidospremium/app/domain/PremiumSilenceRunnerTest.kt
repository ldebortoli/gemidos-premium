package com.gemidospremium.app.domain

import com.gemidospremium.app.model.AudioResult
import com.gemidospremium.app.ports.PrankAudioPort
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PremiumSilenceRunnerTest {
    @Test
    fun `sequence fades down and always restores the previous volume`() = runBlocking {
        val audio = SequenceAudio()
        val pauses = mutableListOf<Long>()

        val result = PremiumSilenceRunner(audio, pause = { pauses += it }).run()

        assertEquals(AudioResult.Success, result)
        assertEquals(listOf(0.82f, 0.60f, 0.38f, 0.18f, 0.00f), audio.volumes)
        assertEquals(listOf(220L, 260L, 300L, 340L, 380L), pauses)
        assertEquals(1, audio.stopCalls)
    }

    @Test
    fun `volume failure stops the sequence and preserves its error`() = runBlocking {
        val audio = SequenceAudio(
            failVolumeAt = 2,
            stopResult = AudioResult.Failure("fallo final"),
        )

        val result = PremiumSilenceRunner(audio, pause = {}).run()

        assertEquals(AudioResult.Failure("fallo de volumen"), result)
        assertEquals(2, audio.volumes.size)
        assertEquals(1, audio.stopCalls)
    }

    @Test
    fun `successful curve reports a final restore failure`() = runBlocking {
        val audio = SequenceAudio(stopResult = AudioResult.Failure("no restauró"))

        val result = PremiumSilenceRunner(audio, pause = {}).run()

        assertEquals(AudioResult.Failure("no restauró"), result)
        assertEquals(1, audio.stopCalls)
    }

    @Test
    fun `cancellation still restores volume`() = runBlocking {
        val audio = SequenceAudio()
        var cancelled = false

        try {
            PremiumSilenceRunner(audio) { throw CancellationException("salida") }.run()
        } catch (_: CancellationException) {
            cancelled = true
        }

        assertTrue(cancelled)
        assertEquals(1, audio.volumes.size)
        assertEquals(1, audio.stopCalls)
    }

    private class SequenceAudio(
        private val failVolumeAt: Int? = null,
        private val stopResult: AudioResult = AudioResult.Success,
    ) : PrankAudioPort {
        val volumes = mutableListOf<Float>()
        var stopCalls = 0

        override fun playAtMaximum(onCompleted: () -> Unit, onError: (String) -> Unit): AudioResult =
            AudioResult.Success

        override fun setRelativeVolume(relativeVolume: Float): AudioResult {
            volumes += relativeVolume
            return if (volumes.size == failVolumeAt) {
                AudioResult.Failure("fallo de volumen")
            } else {
                AudioResult.Success
            }
        }

        override fun stopAndRestore(): AudioResult {
            stopCalls += 1
            return stopResult
        }
    }
}
