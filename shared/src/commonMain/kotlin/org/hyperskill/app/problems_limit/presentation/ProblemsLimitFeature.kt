package org.hyperskill.app.problems_limit.presentation

import kotlin.time.Duration
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.problems_limit.domain.model.ProblemsLimitScreen
import org.hyperskill.app.subscriptions.domain.model.Subscription

object ProblemsLimitFeature {
    sealed interface State {
        object Idle : State

        object Loading : State

        data class Content(
            val subscription: Subscription,
            val isFreemiumEnabled: Boolean,
            val isFreemiumWrongSubmissionChargeLimitsEnabled: Boolean,
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
                val updateInLabel: String?
            ) : Content
        }

        object Error : ViewState
    }

    sealed interface Message {
        object RetryContentLoading : Message
    }

    internal sealed interface InternalMessage : Message {
        data class Initialize(val forceUpdate: Boolean = false) : InternalMessage
        object PullToRefresh : InternalMessage

        object LoadSubscriptionResultError : InternalMessage
        data class LoadSubscriptionResultSuccess(
            val subscription: Subscription,
            val isFreemiumEnabled: Boolean,
            val isFreemiumWrongSubmissionChargeLimitsEnabled: Boolean
        ) : InternalMessage

        data class UpdateInChanged(val newUpdateIn: Duration) : InternalMessage
        data class SubscriptionChanged(val newSubscription: Subscription) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class LoadSubscription(
            val screen: ProblemsLimitScreen,
            val forceUpdate: Boolean
        ) : InternalAction

        data class LaunchTimer(val updateIn: Duration) : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}