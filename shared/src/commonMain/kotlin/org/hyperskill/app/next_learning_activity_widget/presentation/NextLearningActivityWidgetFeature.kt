package org.hyperskill.app.next_learning_activity_widget.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.presentation.model.LearningActivityTargetViewAction
import org.hyperskill.app.study_plan.domain.model.StudyPlan

object NextLearningActivityWidgetFeature {
    data class State(
        internal val studyPlan: StudyPlan?,
        val contentState: ContentState
    ) {
        internal val isRefreshing: Boolean
            get() = contentState is ContentState.Content && contentState.isRefreshing
    }

    internal fun initialState(): State =
        State(studyPlan = null, contentState = ContentState.Idle)

    sealed interface ContentState {
        object Idle : ContentState
        object Loading : ContentState
        object NetworkError : ContentState
        object Empty : ContentState
        data class Content(
            val learningActivity: LearningActivity,
            val isRefreshing: Boolean = false
        ) : ContentState
    }

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object NetworkError : ViewState
        object Empty : ViewState
        data class Content(
            val id: Long,
            val title: String,
            val subtitle: String?,
            val isIdeRequired: Boolean,
            val progress: Int,
            val formattedProgress: String?
        ) : ViewState
    }

    sealed interface Message {
        object RetryContentLoading : Message
        object NextLearningActivityClicked : Message
    }

    internal sealed interface InternalMessage : Message {
        data class Initialize(val forceUpdate: Boolean = false) : InternalMessage

        object FetchNextLearningActivityDataError : InternalMessage
        data class FetchNextLearningActivityDataSuccess(
            val learningActivity: LearningActivity?,
            val studyPlan: StudyPlan?
        ) : InternalMessage

        object PullToRefresh : InternalMessage

        data class NextLearningActivityChanged(val learningActivity: LearningActivity?) : InternalMessage
        data class StudyPlanChanged(val studyPlan: StudyPlan) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                data class LearningActivityTarget(val viewAction: LearningActivityTargetViewAction) : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class FetchNextLearningActivity(val forceUpdate: Boolean) : InternalAction

        data class CaptureSentryException(val throwable: Throwable) : InternalAction
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}