package org.hyperskill.app.${package}.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

object ${featureNameWithPostfix} {
    internal sealed interface State {
        object Idle : State
        object Loading : State
        data class Content(
            // Add content state properties here
        ) : State
        object NetworkError : State
    }

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object Error : ViewState
        data class Content(
            // Add content view state properties here
        ) : ViewState
    }

    sealed interface Message {
        object Initialize : Message

        object RetryContentLoading : Message

        /**
        * Analytic
        */
        object ViewedEvent : Message
    }

    /**
    * Internal messages
    */
    internal sealed interface ContentFetchResult : Message {
        data class Success(
            // Add content fetch result properties here
        ) : ContentFetchResult

        object Error : ContentFetchResult
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                // Add navigate to actions here
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class FetchContent(
            val forceLoadFromNetwork: Boolean
        ) : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}

