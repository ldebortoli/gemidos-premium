package com.gemidospremium.app.platform

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.gemidospremium.app.R

class AndroidPremiumCelebrationAudio(context: Context) {
    private val appContext = context.applicationContext
    private val attributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()
    private var player: MediaPlayer? = null

    fun play() {
        stop()
        runCatching {
            val newPlayer = MediaPlayer()
            try {
                checkNotNull(
                    appContext.resources.openRawResourceFd(R.raw.premium_slot_celebration),
                ).use { descriptor ->
                    newPlayer.setAudioAttributes(attributes)
                    newPlayer.setDataSource(
                        descriptor.fileDescriptor,
                        descriptor.startOffset,
                        descriptor.length,
                    )
                }
                newPlayer.isLooping = false
                newPlayer.setOnCompletionListener(::releaseCompletedPlayer)
                newPlayer.setOnErrorListener { failedPlayer, _, _ ->
                    if (player === failedPlayer) player = null
                    releasePlayer(failedPlayer)
                    true
                }
                newPlayer.prepare()
                newPlayer.setVolume(0.85f, 0.85f)
                newPlayer.start()
                player = newPlayer
            } catch (error: Exception) {
                releasePlayer(newPlayer)
                throw error
            }
        }.onFailure {
            stop()
        }
    }

    fun stop() {
        val activePlayer = player
        player = null
        releasePlayer(activePlayer)
    }

    private fun releaseCompletedPlayer(completedPlayer: MediaPlayer) {
        if (player === completedPlayer) player = null
        releasePlayer(completedPlayer)
    }

    private fun releasePlayer(activePlayer: MediaPlayer?) {
        runCatching {
            activePlayer?.setOnCompletionListener(null)
            activePlayer?.setOnErrorListener(null)
            if (activePlayer?.isPlaying == true) activePlayer.stop()
            activePlayer?.release()
        }.onFailure {
            runCatching { activePlayer?.release() }
        }
    }
}
