package org.hyperskill.app.problems_limit_reached.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.subscriptions.domain.model.Subscription

object ProblemsLimitReachedModalFeature {
    data class State(
        val subscription: Subscription,
        val profile: Profile
    )

    internal fun initialState(
        subscription: Subscription,
        profile: Profile
    ) = State(subscription, profile)

    data class ViewState(
        val title: String,
        val description: String,
        val unlockLimitsButtonText: String?
    )

    sealed interface Message {
        object Initialize : Message
        object ViewedEventMessage : Message
    }

    internal sealed interface InternalMessage : Message {
        data class FetchProfileResultSuccess(val profile: Profile) : InternalMessage

        object FetchProfileResultError : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val event: AnalyticEvent) : InternalAction

        object FetchProfile : InternalMessage
    }
}