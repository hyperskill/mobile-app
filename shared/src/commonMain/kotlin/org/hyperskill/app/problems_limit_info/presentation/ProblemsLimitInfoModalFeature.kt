package org.hyperskill.app.problems_limit_info.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalContext
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.app.subscriptions.domain.model.Subscription

object ProblemsLimitInfoModalFeature {
    data class State(
        val subscription: Subscription,
        val chargeLimitsStrategy: FreemiumChargeLimitsStrategy,
        val context: ProblemsLimitInfoModalContext
    )

    internal fun initialState(
        subscription: Subscription,
        chargeLimitsStrategy: FreemiumChargeLimitsStrategy,
        context: ProblemsLimitInfoModalContext
    ) =
        State(subscription, chargeLimitsStrategy, context)

    /**
     * [limitsDescription] represents a text displayed under the title.
     * [unlockDescription] represents a text displayed above the subscription button.
     */
    data class ViewState(
        val title: String,
        val limitsDescription: String?,
        val animation: Animation,
        val leftLimitsText: String?,
        val resetInText: String?,
        val unlockDescription: String?,
        val buttonText: String
    ) {
        enum class Animation(val isLooped: Boolean) {
            FULL_LIMITS(isLooped = false),
            PARTIALLY_SPENT_LIMITS(isLooped = false),
            NO_LIMITS_LEFT(isLooped = true)
        }
    }

    sealed interface Message {
        object UnlockUnlimitedProblemsClicked : Message

        /**
         * Analytic
         */
        object ShownEventMessage : Message
        object HiddenEventMessage : Message
    }

    internal sealed interface InternalMessage : Message

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                data class Paywall(val paywallTransitionSource: PaywallTransitionSource) : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val event: AnalyticEvent) : InternalAction
    }
}