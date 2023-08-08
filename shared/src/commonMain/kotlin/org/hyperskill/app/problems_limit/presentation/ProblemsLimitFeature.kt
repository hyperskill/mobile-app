package org.hyperskill.app.problems_limit.presentation

import kotlin.time.Duration
import org.hyperskill.app.problems_limit.domain.model.ProblemsLimitScreen
import org.hyperskill.app.subscriptions.domain.model.Subscription

object ProblemsLimitFeature {
    sealed interface State {
        object Idle : State

        object Loading : State

        data class Content(
            val subscription: Subscription,
            val isFreemiumEnabled: Boolean,
            val updateIn: Duration?,
            internal val isRefreshing: Boolean = false
        ) : State

        object NetworkError : State
    }

    internal val State.isRefreshing: Boolean
        get() = this is State.Content && isRefreshing

    sealed interface ViewState {
        object Idle : ViewState

        object Loading : ViewState

        sealed interface Content : ViewState {
            object Empty : Content

            /**
             * [progress] represent progress from 0.0 to 1.0.
             * 1.0 means the user haven't passed any step.
             * 0.0 means the user passed all the available steps.
             */
            data class Widget(
                val progress: Float,
                val stepsLimitLabel: String,
                val updateInLabel: String
            ) : Content
        }

        object Error : ViewState
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean = false) : Message

        object PullToRefresh : Message

        sealed interface SubscriptionLoadingResult : Message {
            data class Success(
                val subscription: Subscription,
                val isFreemiumEnabled: Boolean
            ) : SubscriptionLoadingResult

            object Error : SubscriptionLoadingResult
        }

        data class UpdateInChanged(val newUpdateIn: Duration) : Message

        data class SubscriptionChanged(val newSubscription: Subscription) : Message
    }

    sealed interface Action {
        data class LoadSubscription(val screen: ProblemsLimitScreen, val forceUpdate: Boolean) : Action

        data class LaunchTimer(val updateIn: Duration) : Action

        sealed interface ViewAction : Action
    }
}