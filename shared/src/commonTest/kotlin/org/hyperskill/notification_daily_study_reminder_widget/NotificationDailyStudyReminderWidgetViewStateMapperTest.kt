package org.hyperskill.notification_daily_study_reminder_widget

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature
import org.hyperskill.app.notification_daily_study_reminder_widget.view.mapper.NotificationDailyStudyReminderWidgetViewStateMapper

class NotificationDailyStudyReminderWidgetViewStateMapperTest {
    @Test
    fun `map Idle state to Hidden ViewState`() {
        val state = NotificationDailyStudyReminderWidgetFeature.State.Idle
        val viewState = NotificationDailyStudyReminderWidgetViewStateMapper.map(state)
        assertEquals(NotificationDailyStudyReminderWidgetFeature.ViewState.Hidden, viewState)
    }

    @Test
    fun `map Loading state to Hidden ViewState`() {
        val state = NotificationDailyStudyReminderWidgetFeature.State.Loading
        val viewState = NotificationDailyStudyReminderWidgetViewStateMapper.map(state)
        assertEquals(NotificationDailyStudyReminderWidgetFeature.ViewState.Hidden, viewState)
    }

    @Test
    fun `map Hidden state to Hidden ViewState`() {
        val state = NotificationDailyStudyReminderWidgetFeature.State.Hidden
        val viewState = NotificationDailyStudyReminderWidgetViewStateMapper.map(state)
        assertEquals(NotificationDailyStudyReminderWidgetFeature.ViewState.Hidden, viewState)
    }

    @Test
    fun `map Data state with passedTopicsCount greater than 0 to Visible ViewState`() {
        val state = NotificationDailyStudyReminderWidgetFeature.State.Data(passedTopicsCount = 5)
        val viewState = NotificationDailyStudyReminderWidgetViewStateMapper.map(state)
        assertEquals(NotificationDailyStudyReminderWidgetFeature.ViewState.Visible, viewState)
    }

    @Test
    fun `map Data state with passedTopicsCount equals 0 to Hidden ViewState`() {
        val state = NotificationDailyStudyReminderWidgetFeature.State.Data(passedTopicsCount = 0)
        val viewState = NotificationDailyStudyReminderWidgetViewStateMapper.map(state)
        assertEquals(NotificationDailyStudyReminderWidgetFeature.ViewState.Hidden, viewState)
    }
}