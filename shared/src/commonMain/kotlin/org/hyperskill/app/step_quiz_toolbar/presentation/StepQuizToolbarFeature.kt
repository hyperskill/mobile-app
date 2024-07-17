package org.hyperskill.app.step_quiz_toolbar.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalContext
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.app.subscriptions.domain.model.Subscription

object StepQuizToolbarFeature {
    sealed interface State {
        object Idle : State
        object Unavailable : State
        object Loading : State
        object Error : State
        data class Content(
            val subscription: Subscription,
            val isMobileContentTrialEnabled: Boolean,
            val chargeLimitsStrategy: FreemiumChargeLimitsStrategy
        ) : State
    }

    internal fun initialState(stepRoute: StepRoute) =
        if (StepQuizToolbarResolver.isToolbarAvailable(stepRoute)) {
            State.Idle
        } else {
            State.Unavailable
        }

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object Error : ViewState
        sealed interface Content : ViewState {
            object Hidden : Content
            data class Visible(val stepsLimitLabel: String) : Content
        }
    }

    sealed interface Message {
        object ProblemsLimitClicked : Message
    }

    internal sealed interface InternalMessage : Message {
        object Initialize : InternalMessage

        object SubscriptionFetchError : InternalMessage
        data class SubscriptionFetchSuccess(
            val subscription: Subscription,
            val isMobileContentTrialEnabled: Boolean,
            val chargeLimitsStrategy: FreemiumChargeLimitsStrategy
        ) : InternalMessage

        data class SubscriptionChanged(val subscription: Subscription) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            data class ShowProblemsLimitInfoModal(
                val subscription: Subscription,
                val chargeLimitsStrategy: FreemiumChargeLimitsStrategy,
                val context: ProblemsLimitInfoModalContext
            ) : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val event: AnalyticEvent) : InternalAction

        object FetchSubscription : InternalAction
    }
}