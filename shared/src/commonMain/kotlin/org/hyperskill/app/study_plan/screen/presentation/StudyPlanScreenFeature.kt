package org.hyperskill.app.study_plan.screen.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.isRefreshing
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.view.model.StudyPlanWidgetViewState
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature

object StudyPlanScreenFeature {
    internal data class State(
        val toolbarState: GamificationToolbarFeature.State,
        val usersInterviewWidgetState: UsersInterviewWidgetFeature.State,
        val notificationDailyStudyReminderWidgetState: NotificationDailyStudyReminderWidgetFeature.State,
        val studyPlanWidgetState: StudyPlanWidgetFeature.State
    ) {
        companion object;

        val isRefreshing: Boolean
            get() = toolbarState.isRefreshing ||
                studyPlanWidgetState.isRefreshing
    }

    data class ViewState(
        val trackTitle: String?,
        val toolbarViewState: GamificationToolbarFeature.ViewState,
        val usersInterviewWidgetState: UsersInterviewWidgetFeature.State,
        val notificationDailyStudyReminderWidgetViewState: NotificationDailyStudyReminderWidgetFeature.ViewState,
        val studyPlanWidgetViewState: StudyPlanWidgetViewState,
        val isRefreshing: Boolean
    )

    sealed interface Message {
        data object Initialize : Message

        data object RetryContentLoading : Message

        data object PullToRefresh : Message

        data object ScreenBecomesActive : Message

        data object ChangeTrackButtonClicked : Message

        data object ViewedEventMessage : Message

        data class GamificationToolbarMessage(
            val message: GamificationToolbarFeature.Message
        ) : Message

        data class UsersInterviewWidgetMessage(
            val message: UsersInterviewWidgetFeature.Message
        ) : Message

        data class NotificationDailyStudyReminderWidgetMessage(
            val message: NotificationDailyStudyReminderWidgetFeature.Message
        ) : Message

        data class StudyPlanWidgetMessage(
            val message: StudyPlanWidgetFeature.Message
        ) : Message
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                data object TrackSelectionScreen : NavigateTo
            }

            data class GamificationToolbarViewAction(
                val viewAction: GamificationToolbarFeature.Action.ViewAction
            ) : ViewAction

            data class UsersInterviewWidgetViewAction(
                val viewAction: UsersInterviewWidgetFeature.Action.ViewAction
            ) : ViewAction

            data class NotificationDailyStudyReminderWidgetViewAction(
                val viewAction: NotificationDailyStudyReminderWidgetFeature.Action.ViewAction
            ) : ViewAction

            data class StudyPlanWidgetViewAction(
                val viewAction: StudyPlanWidgetFeature.Action.ViewAction
            ) : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction

        data class GamificationToolbarAction(
            val action: GamificationToolbarFeature.Action
        ) : InternalAction

        data class UsersInterviewWidgetAction(
            val action: UsersInterviewWidgetFeature.Action
        ) : InternalAction

        data class NotificationDailyStudyReminderWidgetAction(
            val action: NotificationDailyStudyReminderWidgetFeature.Action
        ) : InternalAction

        data class StudyPlanWidgetAction(
            val action: StudyPlanWidgetFeature.Action
        ) : InternalAction
    }
}