package org.hyperskill.app.problems_limit.presentation

import kotlin.time.Duration
import org.hyperskill.app.subscriptions.domain.model.Subscription

object ProblemsLimitFeature {
    internal sealed interface State {
        object Idle : State

        object Loading : State

        data class Content(
            val subscription: Subscription,
            val isFreemiumEnabled: Boolean,
            val updateIn: Duration?
        ) : State

        object NetworkError : State
    }

    sealed interface ViewState {
        object Idle : ViewState

        object Loading : ViewState

        sealed interface Content : ViewState {
            object Empty : Content
            data class Widget(
                val stepsLimitTotal: Int,
                val stepsLimitLeft: Int,
                val stepsLimitLabel: String,
                val updateInLabel: String
            ) : Content
        }

        object Error : ViewState
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean = false) : Message

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
        data class LoadSubscription(val forceUpdate: Boolean) : Action

        data class LaunchTimer(val updateIn: Duration) : Action

        sealed interface ViewAction : Action
    }
}