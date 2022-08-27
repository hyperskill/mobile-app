package org.hyperskill.app.placeholder_new_user.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

interface PlaceholderNewUserFeature {
    sealed interface State {
        object Content : State
    }

    sealed interface Message {
        /**
         * Analytic
         */
        object PlaceholderViewedEventMessage : Message
        object PlaceholderClickedContinueEventMessage : Message
    }

    sealed interface Action {
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action
        sealed class ViewAction : Action
    }
}
