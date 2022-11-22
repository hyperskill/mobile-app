package org.hyperskill.app.placeholder_new_user.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.core.domain.url.HyperskillUrlPath

interface PlaceholderNewUserFeature {
    sealed interface State {
        object Content : State
    }

    sealed interface Message {
        object PlaceholderSignInTappedMessage : Message
        object OpenAuthScreen : Message

        object ClickedContinueOnWeb : Message

        data class LinkReceived(val url: String) : Message
        object LinkReceiveFailed : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
        object ClickedContinueEventMessage : Message
    }

    sealed interface Action {
        object Logout : Action

        data class GetLink(val path: HyperskillUrlPath) : Action

        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                object AuthScreen : NavigateTo
            }
            data class FollowUrl(val url: String) : ViewAction
        }

        /**
         * Analytic
         */
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action
    }
}
