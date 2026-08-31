package com.gemidospremium.app.platform

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import com.gemidospremium.app.R
import com.gemidospremium.app.localization.GemidosText
import com.gemidospremium.app.localization.TextKey
import com.gemidospremium.app.model.AudioResult
import com.gemidospremium.app.ports.PrankAudioPort

class AndroidPrankAudioPort(
    context: Context,
    private val text: () -> GemidosText,
) : PrankAudioPort {
    private val appContext = context.applicationContext
    private val audioManager = appContext.getSystemService(AudioManager::class.java)
    private val attributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
        .build()
    private val focusListener = AudioManager.OnAudioFocusChangeListener { change ->
        if (change == AudioManager.AUDIOFOCUS_LOSS || change == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            val completion = completionCallback
            val failure = errorCallback
            val result = stopAndRestore()
            if (result is AudioResult.Failure) failure?.invoke(result.message) else completion?.invoke()
        }
    }
    private val focusRequest: AudioFocusRequest? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
            .setAudioAttributes(attributes)
            .setOnAudioFocusChangeListener(focusListener)
            .setWillPauseWhenDucked(true)
            .build()
    } else {
        null
    }
    private var player: MediaPlayer? = null
    private var previousVolume: Int? = null
    private var hasAudioFocus = false
    private var completionCallback: (() -> Unit)? = null
    private var errorCallback: ((String) -> Unit)? = null

    override fun playAtMaximum(onCompleted: () -> Unit, onError: (String) -> Unit): AudioResult {
        stopAndRestore()
        completionCallback = onCompleted
        errorCallback = onError
        return try {
            if (!requestAudioFocus()) {
                clearCallbacks()
                return AudioResult.Failure(text()[TextKey.TORCH_ACCESS_ERROR])
            }
            previousVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0,
            )
            val descriptor = appContext.resources.openRawResourceFd(R.raw.prank_moans)
                ?: throw IllegalStateException(text()[TextKey.TORCH_UNAVAILABLE])
            player = MediaPlayer().apply {
                setAudioAttributes(attributes)
                setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
                descriptor.close()
                isLooping = true
                setOnCompletionListener {
                    val callback = completionCallback
                    stopAndRestore()
                    callback?.invoke()
                }
                setOnErrorListener { _, _, _ ->
                    val callback = errorCallback
                    stopAndRestore()
                    callback?.invoke(text()[TextKey.TORCH_ACCESS_ERROR])
                    true
                }
                prepare()
                setVolume(1f, 1f)
                start()
            }
            AudioResult.Success
        } catch (_: Exception) {
            stopAndRestore()
            AudioResult.Failure(text()[TextKey.TORCH_ACCESS_ERROR])
        }
    }

    override fun setRelativeVolume(relativeVolume: Float): AudioResult = try {
        val activePlayer = player ?: return AudioResult.Failure(text()[TextKey.TORCH_UNAVAILABLE])
        val clamped = relativeVolume.coerceIn(0f, 1f)
        activePlayer.setVolume(clamped, clamped)
        AudioResult.Success
    } catch (_: Exception) {
        AudioResult.Failure(text()[TextKey.TORCH_ACCESS_ERROR])
    }

    override fun stopAndRestore(): AudioResult {
        var failure: String? = null
        val activePlayer = player
        player = null
        clearCallbacks()
        try {
            activePlayer?.setOnCompletionListener(null)
            activePlayer?.setOnErrorListener(null)
            if (activePlayer?.isPlaying == true) activePlayer.stop()
            activePlayer?.release()
        } catch (_: Exception) {
            failure = text()[TextKey.TORCH_ACCESS_ERROR]
            runCatching { activePlayer?.release() }
        }
        previousVolume?.let { savedVolume ->
            try {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, savedVolume, 0)
            } catch (_: Exception) {
                failure = text()[TextKey.TORCH_ACCESS_ERROR]
            }
        }
        previousVolume = null
        abandonAudioFocus()
        return failure?.let(AudioResult::Failure) ?: AudioResult.Success
    }

    @Suppress("DEPRECATION")
    private fun requestAudioFocus(): Boolean {
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.requestAudioFocus(checkNotNull(focusRequest))
        } else {
            audioManager.requestAudioFocus(
                focusListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT,
            )
        }
        hasAudioFocus = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        return hasAudioFocus
    }

    @Suppress("DEPRECATION")
    private fun abandonAudioFocus() {
        if (!hasAudioFocus) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusRequest?.let(audioManager::abandonAudioFocusRequest)
        } else {
            audioManager.abandonAudioFocus(focusListener)
        }
        hasAudioFocus = false
    }

    private fun clearCallbacks() {
        completionCallback = null
        errorCallback = null
    }
}
