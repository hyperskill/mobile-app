package org.hyperskill.app.placeholder_new_user.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.core.domain.url.HyperskillUrlPath

interface PlaceholderNewUserFeature {
    sealed interface State {
        /**
         * @property isLoadingMagicLink A boolean flag that indicates about magic link loading.
         * */
        data class Content(
            val isLoadingMagicLink: Boolean = false
        ) : State
    }

    sealed interface Message {
        object PlaceholderSignInTappedMessage : Message
        object OpenAuthScreen : Message

        object ClickedContinueOnWeb : Message

        data class GetMagicLinkReceiveSuccess(val url: String) : Message
        object GetMagicLinkReceiveFailure : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
    }

    sealed interface Action {
        object Logout : Action

        data class GetMagicLink(val path: HyperskillUrlPath) : Action

        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                object AuthScreen : NavigateTo
            }
            data class OpenUrl(val url: String) : ViewAction
            object ShowGetMagicLinkError : ViewAction
        }

        /**
         * Analytic
         */
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action
    }
}
