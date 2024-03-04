package org.hyperskill.app.test_feature

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.test_feature.TestFeature.Action.ViewAction
import org.hyperskill.app.test_feature.TestFeature.Message
import org.hyperskill.app.test_feature.TestFeature.State

object TestFeature {
    data class State()

    sealed interface Message {
        object Initialize : Message
        object ViewedEventMessage : Message
    }

    internal sealed interface InternalMessage : Message

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticsEvent(val event: AnalyticEvent) : InternalAction
    }
}