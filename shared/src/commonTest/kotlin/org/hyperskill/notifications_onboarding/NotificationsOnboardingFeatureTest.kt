package org.hyperskill.notifications_onboarding

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature

class NotificationsOnboardingFeatureTest {
    @Test
    fun `initialState should set dailyStudyRemindersStartHour to current hour`() {
        val currentHour = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour
        val state = NotificationsOnboardingFeature.initialState()
        assertEquals(currentHour, state.dailyStudyRemindersStartHour)
    }
}