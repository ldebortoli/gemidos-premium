package com.gemidospremium.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.gemidospremium.app.ads.AdsCoordinator
import com.gemidospremium.app.billing.BillingEvents
import com.gemidospremium.app.billing.BillingGateway
import com.gemidospremium.app.billing.GooglePlayBillingGateway
import com.gemidospremium.app.domain.GemidosEngine
import com.gemidospremium.app.domain.PremiumSilenceRunner
import com.gemidospremium.app.localization.AppLanguage
import com.gemidospremium.app.localization.GemidosText
import com.gemidospremium.app.localization.GemidosTextCatalog
import com.gemidospremium.app.localization.PreferencesLanguageStore
import com.gemidospremium.app.localization.TextKey
import com.gemidospremium.app.model.AudioResult
import com.gemidospremium.app.model.EngineResult
import com.gemidospremium.app.model.GemidosEffect
import com.gemidospremium.app.model.GemidosState
import com.gemidospremium.app.platform.AndroidPremiumCelebrationAudio
import com.gemidospremium.app.platform.AndroidPrankAudioPort
import com.gemidospremium.app.platform.PreferencesPremiumStore
import com.gemidospremium.app.ports.PrankAudioPort
import com.gemidospremium.app.ui.GemidosPremiumScreen
import com.gemidospremium.app.ui.theme.GemidosPremiumTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), BillingEvents {
    private lateinit var engine: GemidosEngine
    private lateinit var audioPort: PrankAudioPort
    private lateinit var premiumCelebrationAudio: AndroidPremiumCelebrationAudio
    private lateinit var premiumSilenceRunner: PremiumSilenceRunner
    private lateinit var languageStore: PreferencesLanguageStore
    private var billingGateway: BillingGateway? = null
    private var premiumSilenceJob: Job? = null
    private var uiState by mutableStateOf(GemidosState())
    private var adsReady by mutableStateOf(false)
    private var selectedLanguage by mutableStateOf(AppLanguage.SPANISH_ARGENTINA)
    private var shouldRestartOnResume = false
    private val currentText: GemidosText
        get() = GemidosTextCatalog.forLanguage(selectedLanguage)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        languageStore = PreferencesLanguageStore(this)
        selectedLanguage = languageStore.current()
        audioPort = AndroidPrankAudioPort(this) { currentText }
        premiumCelebrationAudio = AndroidPremiumCelebrationAudio(this)
        premiumSilenceRunner = PremiumSilenceRunner(audioPort, pause = { delay(it) })
        engine = GemidosEngine(
            premiumStore = PreferencesPremiumStore(this),
            text = { currentText },
        )
        uiState = engine.state

        if (!BuildConfig.DEMO_BILLING) {
            billingGateway = GooglePlayBillingGateway(
                context = this,
                productId = BuildConfig.PREMIUM_PRODUCT_ID,
                events = this,
                text = { currentText },
            ).also(BillingGateway::start)
        }

        setContent {
            GemidosPremiumTheme {
                GemidosPremiumScreen(
                    state = uiState,
                    text = currentText,
                    selectedLanguage = selectedLanguage,
                    adsReady = adsReady,
                    adUnitId = BuildConfig.ADMOB_BANNER_ID,
                    isDemo = BuildConfig.DEMO_BILLING,
                    onCountdownTick = { applyResult(engine.tickCountdown()) },
                    onRestart = ::restartCountdown,
                    onPremium = { applyResult(engine.pressPremium()) },
                    onNormalOff = {
                        premiumCelebrationAudio.stop()
                        applyResult(engine.turnOffNormally())
                    },
                    onConfirmPurchase = { applyResult(engine.confirmPremiumPurchase(BuildConfig.DEMO_BILLING)) },
                    onDismissPurchase = { uiState = engine.dismissPurchase() },
                    onResetDemoPremium = ::resetDemoPremium,
                    onLanguageSelected = ::selectLanguage,
                )
            }
        }

        initializeAdsIfEligible()
    }

    override fun onResume() {
        super.onResume()
        if (shouldRestartOnResume && ::engine.isInitialized) {
            uiState = engine.restartCountdown()
            shouldRestartOnResume = false
        }
        billingGateway?.start()
    }

    override fun onPause() {
        if (::engine.isInitialized) {
            premiumSilenceJob?.cancel()
            premiumCelebrationAudio.stop()
            applyResult(engine.pause())
            shouldRestartOnResume = true
        }
        super.onPause()
    }

    override fun onDestroy() {
        premiumSilenceJob?.cancel()
        if (::premiumCelebrationAudio.isInitialized) premiumCelebrationAudio.stop()
        if (::audioPort.isInitialized) audioPort.stopAndRestore()
        billingGateway?.close()
        super.onDestroy()
    }

    override fun onPriceAvailable(formattedPrice: String) {
        runOnUiThread { uiState = engine.updatePrice(formattedPrice) }
    }

    override fun onPremiumPurchased() {
        runOnUiThread {
            applyResult(engine.billingPurchased())
            adsReady = false
        }
    }

    override fun onBillingMessage(message: String) {
        runOnUiThread { uiState = engine.billingFailed(message) }
    }

    private fun resetDemoPremium() {
        if (!BuildConfig.DEMO_BILLING) return
        premiumSilenceJob?.cancel()
        premiumCelebrationAudio.stop()
        adsReady = false
        applyResult(engine.resetPremiumForTesting())
        initializeAdsIfEligible()
    }

    private fun initializeAdsIfEligible() {
        if (adsReady || uiState.isPremiumOwned || BuildConfig.ADMOB_BANNER_ID.isBlank()) return
        AdsCoordinator(BuildConfig.DEMO_BILLING).requestPermissionAndInitialize(this) {
            runOnUiThread { adsReady = true }
        }
    }

    private fun applyResult(result: EngineResult) {
        uiState = result.state
        when (result.effect) {
            GemidosEffect.None -> Unit
            GemidosEffect.StartAudio -> startAudio()
            GemidosEffect.StopAudio -> audioPort.stopAndRestore()
            GemidosEffect.LaunchGooglePlay -> {
                billingGateway?.launchPurchase(this)
                    ?: onBillingMessage(currentText[TextKey.BILLING_NOT_CONFIGURED])
            }

            GemidosEffect.RunPremiumSilence -> runPremiumSilence()
        }
    }

    private fun startAudio() {
        when (
            val result = audioPort.playAtMaximum(
                onCompleted = { runOnUiThread { uiState = engine.audioCompleted() } },
                onError = { message -> runOnUiThread { uiState = engine.audioFailed(message) } },
            )
        ) {
            AudioResult.Success -> Unit
            is AudioResult.Failure -> uiState = engine.audioFailed(result.message)
        }
    }

    private fun runPremiumSilence() {
        premiumSilenceJob?.cancel()
        premiumCelebrationAudio.play()
        premiumSilenceJob = lifecycleScope.launch {
            uiState = when (val result = premiumSilenceRunner.run()) {
                AudioResult.Success -> engine.premiumSilenceCompleted()
                is AudioResult.Failure -> engine.premiumSilenceFailed(result.message)
            }
        }
    }

    private fun restartCountdown() {
        premiumCelebrationAudio.stop()
        uiState = engine.restartCountdown()
    }

    private fun selectLanguage(language: AppLanguage) {
        if (language == selectedLanguage) return
        languageStore.save(language)
        selectedLanguage = language
        uiState = engine.languageChanged()
    }
}
