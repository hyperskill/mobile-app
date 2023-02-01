package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.hyperskill.app.profile.domain.analytic.ProfileClickedDailyStudyRemindsHyperskillAnalyticEvent
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumbAnalyticEventMapper
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumbCategory
import org.hyperskill.app.sentry.domain.model.level.HyperskillSentryLevel

class HyperskillSentryBreadcrumbAnalyticEventMapperTest {
    @Test
    fun testMapAnalyticEventToBreadcrumb() {
        val analyticEvent = ProfileClickedDailyStudyRemindsHyperskillAnalyticEvent(isEnabled = true)

        val breadcrumb = HyperskillSentryBreadcrumbAnalyticEventMapper.mapAnalyticEvent(analyticEvent)

        assertEquals(HyperskillSentryBreadcrumbCategory.ANALYTIC_EVENT.stringValue, breadcrumb.category)
        assertEquals("ProfileClickedDailyStudyRemindsHyperskillAnalyticEvent", breadcrumb.message)
        assertEquals(HyperskillSentryLevel.INFO, breadcrumb.level)
        // data
        assertEquals("/profile", breadcrumb.data!!["route"])
        assertEquals("click", breadcrumb.data!!["action"])
        assertEquals("main", breadcrumb.data!!["part"])
        assertEquals("daily_study_reminds", breadcrumb.data!!["target"])
        assertEquals(true, (breadcrumb.data!!["context"] as Map<*, *>)["state"])
        assertNull(breadcrumb.data!!["client_time"])
    }
}