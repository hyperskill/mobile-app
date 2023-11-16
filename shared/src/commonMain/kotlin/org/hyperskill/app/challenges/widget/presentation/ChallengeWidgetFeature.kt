package org.hyperskill.app.challenges.widget.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.challenges.domain.model.Challenge
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState

object ChallengeWidgetFeature {
    sealed interface State {
        object Idle : State
        data class Loading(val isLoadingSilently: Boolean) : State
        object Error : State
        data class Content(
            val challenges: List<Challenge>,
            internal val isRefreshing: Boolean = false
        ) : State
    }

    internal val State.isRefreshing: Boolean
        get() = this is State.Content && isRefreshing

    sealed interface Message {
        object RetryContentLoading : Message

        /**
         * When view state is [ChallengeWidgetViewState.Content.Announcement] or
         * [ChallengeWidgetViewState.Content.HappeningNow] description text can contain links.
         *
         * Send this message when user clicks on a link in the description text.
         *
         * @property url URL of the clicked link.
         *
         * @see ChallengeWidgetViewState.Content.Announcement
         * @see ChallengeWidgetViewState.Content.HappeningNow
         */
        data class LinkInTheDescriptionClicked(val url: String) : Message

        /**
         * When view state is [ChallengeWidgetViewState.Content.Announcement] or
         * [ChallengeWidgetViewState.Content.HappeningNow] deadline can be reached (starts in and complete in).
         *
         * Send this message when user clicks on the reload button.
         *
         * @see ChallengeWidgetViewState.Content.Announcement
         * @see ChallengeWidgetViewState.Content.HappeningNow
         */
        object DeadlineReachedReloadClicked : Message
    }

    internal sealed interface InternalMessage : Message {
        data class Initialize(val forceUpdate: Boolean = false) : InternalMessage
        object FetchChallengesError : InternalMessage
        data class FetchChallengesSuccess(val challenges: List<Challenge>) : InternalMessage

        object PullToRefresh : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            data class OpenUrl(val url: String) : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        object FetchChallenges : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}