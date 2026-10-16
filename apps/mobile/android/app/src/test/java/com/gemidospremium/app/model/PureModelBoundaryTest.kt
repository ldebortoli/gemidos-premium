package com.gemidospremium.app.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class PureModelBoundaryTest {
    @Test
    fun `a fresh countdown and its celebration acknowledgement remain independent`() {
        val idle = GemidosState()
        assertEquals(10, idle.countdown)
        assertEquals(PrankPhase.COUNTDOWN, idle.phase)
        assertFalse(idle.isPremiumOwned)
        val dismissed = idle.copy(celebrationSequence = 2, dismissedCelebrationSequence = 2)
        assertEquals(dismissed.celebrationSequence, dismissed.dismissedCelebrationSequence)
        assertEquals(PrankPhase.COUNTDOWN, dismissed.phase)
    }
}
