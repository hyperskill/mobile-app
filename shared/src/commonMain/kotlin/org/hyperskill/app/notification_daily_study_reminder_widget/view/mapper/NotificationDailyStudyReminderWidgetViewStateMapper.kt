package org.hyperskill.app.notification_daily_study_reminder_widget.view.mapper

import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.State
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.ViewState

internal object NotificationDailyStudyReminderWidgetViewStateMapper {
    fun map(state: State): ViewState =
        when (state) {
            State.Idle,
            State.Loading,
            State.Hidden ->
                ViewState.Hidden
            is State.Data ->
                if (state.passedTopicsCount > 0) {
                    ViewState.Visible
                } else {
                    ViewState.Hidden
                }
        }
}