package org.hyperskill.app.next_learning_activity_widget.presentation

import org.hyperskill.app.learning_activities.domain.model.LearningActivity

object NextLearningActivityWidgetFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object NetworkError : State
        object Empty : State
        data class Content(
            val learningActivity: LearningActivity,
            val isRefreshing: Boolean = false
        ) : State
    }

    internal val State.isRefreshing: Boolean
        get() = this is State.Content && isRefreshing

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object NetworkError : ViewState
        object Empty : ViewState
        data class Content(val learningActivity: LearningActivity) : ViewState
    }

    sealed interface Message {
        object RetryContentLoading : Message
        object NextLearningActivityClicked : Message
    }

    internal sealed interface InternalMessage : Message {
        data class Initialize(val forceUpdate: Boolean = false) : InternalMessage
        object FetchNextLearningActivityError : InternalMessage
        data class FetchNextLearningActivitySuccess(val learningActivity: LearningActivity?) : InternalMessage

        object PullToRefresh : InternalMessage

        data class NextLearningActivityChanged(val learningActivity: LearningActivity) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class FetchNextLearningActivity(val forceUpdate: Boolean) : InternalAction
    }
}