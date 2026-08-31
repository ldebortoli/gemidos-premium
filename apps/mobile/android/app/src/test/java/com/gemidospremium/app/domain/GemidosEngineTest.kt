package com.gemidospremium.app.domain

import com.gemidospremium.app.localization.AppLanguage
import com.gemidospremium.app.localization.GemidosTextCatalog
import com.gemidospremium.app.model.ErrorTarget
import com.gemidospremium.app.model.GemidosEffect
import com.gemidospremium.app.model.PrankPhase
import com.gemidospremium.app.ports.PremiumStore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GemidosEngineTest {
    @Test
    fun `countdown starts at ten and launches audio exactly at zero`() {
        val engine = GemidosEngine(FakePremiumStore())

        assertEquals(10, engine.state.countdown)
        repeat(9) { assertEquals(GemidosEffect.None, engine.tickCountdown().effect) }

        val zero = engine.tickCountdown()
        assertEquals(0, zero.state.countdown)
        assertEquals(PrankPhase.PLAYING, zero.state.phase)
        assertEquals(GemidosEffect.StartAudio, zero.effect)
        assertEquals(GemidosEffect.None, engine.tickCountdown().effect)
    }

    @Test
    fun `restart and pause reset transient state and request a safe stop`() {
        val engine = playingEngine()
        engine.pressPremium()

        val paused = engine.pause()
        assertEquals(PrankPhase.SILENCED, paused.state.phase)
        assertFalse(paused.state.showPurchaseDialog)
        assertEquals(GemidosEffect.StopAudio, paused.effect)

        val restarted = engine.restartCountdown()
        assertEquals(10, restarted.countdown)
        assertEquals(PrankPhase.COUNTDOWN, restarted.phase)
        assertNull(restarted.notice)
        assertNull(restarted.error)
    }

    @Test
    fun `audio completion and playback errors select the matching idle state`() {
        val idle = GemidosEngine(FakePremiumStore())
        assertEquals(PrankPhase.COUNTDOWN, idle.audioCompleted().phase)

        val engine = playingEngine()
        val completed = engine.audioCompleted()
        assertEquals(PrankPhase.SILENCED, completed.phase)
        assertTrue(completed.notice!!.contains("audio"))

        val failed = engine.audioFailed("fallo de reproducción")
        assertEquals(PrankPhase.ERROR, failed.phase)
        assertEquals("fallo de reproducción", failed.error)
        assertEquals(ErrorTarget.AUDIO, failed.errorTarget)
    }

    @Test
    fun `plebeian shutdown is immediate free and restarts without copy`() {
        val result = playingEngine().turnOffNormally()

        assertEquals(PrankPhase.COUNTDOWN, result.state.phase)
        assertEquals(10, result.state.countdown)
        assertEquals(GemidosEffect.StopAudio, result.effect)
        assertNull(result.state.notice)
        assertNull(result.state.error)
    }

    @Test
    fun `premium button ignores idle state and asks before an unowned purchase`() {
        val idle = GemidosEngine(FakePremiumStore())
        assertEquals(GemidosEffect.None, idle.pressPremium().effect)
        assertFalse(idle.state.showPurchaseDialog)

        val engine = playingEngine()
        val offer = engine.pressPremium()
        assertEquals(GemidosEffect.None, offer.effect)
        assertTrue(offer.state.showPurchaseDialog)

        val dismissed = engine.dismissPurchase()
        assertFalse(dismissed.showPurchaseDialog)
        assertNull(dismissed.error)
    }

    @Test
    fun `owned premium runs a ceremonial fade and reports both outcomes`() {
        val engine = playingEngine(owned = true)
        val result = engine.pressPremium()

        assertEquals(GemidosEffect.RunPremiumSilence, result.effect)
        assertEquals(PrankPhase.PREMIUM_SILENCING, result.state.phase)
        assertEquals(1, result.state.celebrationSequence)

        val completed = engine.premiumSilenceCompleted()
        assertEquals(PrankPhase.SILENCED, completed.phase)
        assertTrue(completed.notice!!.contains("cinco estrellas"))

        val failed = engine.premiumSilenceFailed("fallo ceremonial")
        assertEquals(PrankPhase.SILENCED, failed.phase)
        assertEquals(ErrorTarget.PREMIUM, failed.errorTarget)
    }

    @Test
    fun `purchase confirmation requires its dialog and routes demo or Play`() {
        val store = FakePremiumStore()
        val engine = GemidosEngine(store)
        assertEquals(GemidosEffect.None, engine.confirmPremiumPurchase(isDemo = false).effect)

        runCountdown(engine)
        engine.pressPremium()
        val play = engine.confirmPremiumPurchase(isDemo = false)
        assertEquals(GemidosEffect.LaunchGooglePlay, play.effect)
        assertFalse(play.state.showPurchaseDialog)

        engine.pressPremium()
        val demo = engine.confirmPremiumPurchase(isDemo = true)
        assertTrue(store.owned)
        assertTrue(demo.state.isPremiumOwned)
        assertEquals(PrankPhase.PREMIUM_SILENCING, demo.state.phase)
        assertEquals(GemidosEffect.RunPremiumSilence, demo.effect)
    }

    @Test
    fun `demo purchase after the clip ends grants Premium without restarting audio`() {
        val store = FakePremiumStore()
        val engine = playingEngine(store = store)
        engine.pressPremium()
        engine.audioCompleted()

        val result = engine.confirmPremiumPurchase(isDemo = true)
        assertTrue(result.state.isPremiumOwned)
        assertEquals(PrankPhase.SILENCED, result.state.phase)
        assertEquals(GemidosEffect.None, result.effect)
    }

    @Test
    fun `demo reset clears ownership and requests audio restoration`() {
        val store = FakePremiumStore(owned = true)
        val engine = GemidosEngine(store)

        val result = engine.resetPremiumForTesting()
        assertFalse(store.owned)
        assertFalse(result.state.isPremiumOwned)
        assertEquals(GemidosEffect.StopAudio, result.effect)
        assertTrue(result.state.notice!!.contains("plebeya"))
    }

    @Test
    fun `billing purchase silences active audio but leaves idle audio stopped`() {
        val activeStore = FakePremiumStore()
        val active = playingEngine(store = activeStore)
        val activeResult = active.billingPurchased()
        assertTrue(activeStore.owned)
        assertEquals(GemidosEffect.RunPremiumSilence, activeResult.effect)
        assertEquals(PrankPhase.PREMIUM_SILENCING, activeResult.state.phase)

        val idle = GemidosEngine(FakePremiumStore())
        val idleResult = idle.billingPurchased()
        assertEquals(GemidosEffect.None, idleResult.effect)
        assertEquals(PrankPhase.COUNTDOWN, idleResult.state.phase)
    }

    @Test
    fun `billing feedback price and language changes clear stale copy`() {
        var language = AppLanguage.SPANISH_ARGENTINA
        val engine = GemidosEngine(
            premiumStore = FakePremiumStore(),
            text = { GemidosTextCatalog.forLanguage(language) },
        )

        assertEquals("ARS 999", engine.updatePrice("ARS 999").priceLabel)
        val failed = engine.billingFailed("Google Play sin conexión")
        assertEquals(ErrorTarget.PREMIUM, failed.errorTarget)
        assertEquals("Google Play sin conexión", failed.error)

        language = AppLanguage.ENGLISH
        val changed = engine.languageChanged()
        assertNull(changed.notice)
        assertNull(changed.error)
        assertNull(changed.errorTarget)
    }

    private fun playingEngine(
        owned: Boolean = false,
        store: FakePremiumStore = FakePremiumStore(owned),
    ): GemidosEngine = GemidosEngine(store).also(::runCountdown)

    private fun runCountdown(engine: GemidosEngine) {
        repeat(10) { engine.tickCountdown() }
    }

    private class FakePremiumStore(var owned: Boolean = false) : PremiumStore {
        override fun isPremiumOwned(): Boolean = owned

        override fun setPremiumOwned(owned: Boolean) {
            this.owned = owned
        }
    }
}
