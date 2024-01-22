package org.hyperskill.app.paywall.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

object PaywallFeature {
    object State

    sealed interface Message {
        object ViewedEventMessage : Message
    }

    internal sealed interface InternalMessage : Message {

    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticsEvent(val event: AnalyticEvent) : InternalAction
    }
}