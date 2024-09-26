package org.hyperskill.notification_daily_study_reminder_widget

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.notification_daily_study_reminder_widget.domain.analytic.NotificationDailyStudyReminderWidgetClickedCloseHyperskillAnalyticEvent
import org.hyperskill.app.notification_daily_study_reminder_widget.domain.analytic.NotificationDailyStudyReminderWidgetClickedHyperskillAnalyticEvent
import org.hyperskill.app.notification_daily_study_reminder_widget.domain.analytic.NotificationDailyStudyReminderWidgetViewedHyperskillAnalyticEvent
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.Action
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.InternalAction
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.InternalMessage
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.Message
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.State
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetReducer

class NotificationDailyStudyReminderWidgetTest {
    private val reducer = NotificationDailyStudyReminderWidgetReducer()

    @Test
    fun `Initialize message when permission not granted should trigger loading state and fetch widget data`() {
        val (state, actions) = reducer.reduce(
            State.Idle,
            Message.Initialize(isNotificationPermissionGranted = false)
        )

        assertEquals(State.Loading, state)
        assertContains(actions, InternalAction.FetchWidgetData)
    }

    @Test
    fun `FetchWidgetDataResult with hidden widget should result in Hidden state`() {
        val (state, actions) = reducer.reduce(
            State.Loading,
            InternalMessage.FetchWidgetDataResult(
                passedTopicsCount = 5,
                isWidgetHidden = true
            )
        )

        assertEquals(State.Hidden, state)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `FetchWidgetDataResult with visible widget should result in Data state`() {
        val (state, actions) = reducer.reduce(
            State.Loading,
            InternalMessage.FetchWidgetDataResult(
                passedTopicsCount = 5,
                isWidgetHidden = false
            )
        )

        assertEquals(State.Data(passedTopicsCount = 5), state)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `CloseClicked message in Data state should hide widget and log analytic event`() {
        val (state, actions) = reducer.reduce(
            State.Data(passedTopicsCount = 5),
            Message.CloseClicked
        )

        assertEquals(State.Hidden, state)
        assertContains(actions, InternalAction.HideWidget)
        assertTrue {
            actions.any { action ->
                action is InternalAction.LogAnalyticEvent &&
                    action.event is NotificationDailyStudyReminderWidgetClickedCloseHyperskillAnalyticEvent
            }
        }
    }

    @Test
    fun `WidgetClicked message in Data state should request permission and log analytic event`() {
        val (state, actions) = reducer.reduce(
            State.Data(passedTopicsCount = 5),
            Message.WidgetClicked
        )

        assertEquals(State.Data(passedTopicsCount = 5), state)
        assertContains(actions, Action.ViewAction.RequestNotificationPermission)
        assertTrue {
            actions.any { action ->
                action is InternalAction.LogAnalyticEvent &&
                    action.event is NotificationDailyStudyReminderWidgetClickedHyperskillAnalyticEvent
            }
        }
    }

    @Test
    fun `ViewedEventMessage in Data state should log viewed analytic event`() {
        val (state, actions) = reducer.reduce(
            State.Data(passedTopicsCount = 5),
            Message.ViewedEventMessage
        )

        assertEquals(State.Data(passedTopicsCount = 5), state)
        assertTrue {
            actions.any { action ->
                action is InternalAction.LogAnalyticEvent &&
                    action.event is NotificationDailyStudyReminderWidgetViewedHyperskillAnalyticEvent
            }
        }
    }

    @Test
    fun `NotificationPermissionRequestResult when permission granted should save daily reminder interval`() {
        val (state, actions) = reducer.reduce(
            State.Data(passedTopicsCount = 5),
            Message.NotificationPermissionRequestResult(
                isPermissionGranted = true
            )
        )

        assertEquals(State.Hidden, state)
        assertTrue {
            actions.any { action ->
                action is InternalAction.SaveDailyStudyRemindersIntervalStartHour
            }
        }
    }

    @Test
    fun `PassedTopicsCountChanged should update passed topics count in Data state`() {
        val (state, actions) = reducer.reduce(
            State.Data(passedTopicsCount = 5),
            InternalMessage.PassedTopicsCountChanged(
                passedTopicsCount = 10
            )
        )

        assertEquals(State.Data(passedTopicsCount = 10), state)
        assertTrue(actions.isEmpty())
    }
}