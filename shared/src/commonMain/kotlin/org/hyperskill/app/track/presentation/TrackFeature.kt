package org.hyperskill.app.track.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.home.presentation.HomeFeature.Action.ViewAction
import org.hyperskill.app.streak.domain.model.Streak
import org.hyperskill.app.topics.domain.model.Topic
import org.hyperskill.app.track.domain.model.StudyPlan
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress

interface TrackFeature {
    sealed interface State {
        /**
         * Represents initial state.
         */
        object Idle : State

        /**
         * Represents a state when loading track screen data.
         */
        object Loading : State

        /**
         * Represents a state when track screen data successfully loaded.
         *
         * @property track Current user profile selected track.
         * @property trackProgress Current user profile selected track progress.
         * @property studyPlan Current user profile study plan.
         * @property topicsToDiscoverNext Current user profile uncompleted topics (theory to discover next) for current stage.
         * @property streak Current user profile streak.
         * @property hypercoinsBalance Current user profile balance of the hypercoins.
         * @property isRefreshing A boolean flag that indicates about is pull-to-refresh is ongoing.
         * @property isLoadingMagicLink A boolean flag that indicates about magic link loading.
         * @see Track
         * @see TrackProgress
         * @see StudyPlan
         * @see Topic
         */
        data class Content(
            val track: Track,
            val trackProgress: TrackProgress,
            val studyPlan: StudyPlan? = null,
            val topicsToDiscoverNext: List<Topic>,
            val streak: Streak?,
            val hypercoinsBalance: Int,
            val isRefreshing: Boolean = false,
            val isLoadingMagicLink: Boolean = false
        ) : State

        /**
         * Represents a state when track screen data failed to load.
         */
        object NetworkError : State
    }

    sealed interface Message {
        /**
         * A message that triggers the process of loading all of the necessary data for module.
         *
         * Use cases:
         * 1. Initial module data loading
         * 2. Reload data when failed to load them previously
         * 3. Refresh data on pull-to-refresh
         *
         * @property forceUpdate A boolean flag that indicates should force to load data without additional checks.
         */
        data class Initialize(val forceUpdate: Boolean = false) : Message

        /**
         * A message that indicates about success module data loading.
         */
        data class TrackSuccess(
            val track: Track,
            val trackProgress: TrackProgress,
            val studyPlan: StudyPlan? = null,
            val topicsToDiscoverNext: List<Topic>,
            val streak: Streak?,
            val hypercoinsBalance: Int
        ) : Message

        /**
         * A message that indicates about failure module data loading.
         */
        object TrackFailure : Message

        /**
         * A message that indicates about click on topic.
         * Triggers navigation to step screen and logs that event to the analytics.
         *
         * @property topicId A topic id that triggered that event.
         */
        data class TopicToDiscoverNextClicked(val topicId: Long) : Message

        object StepQuizSolved : Message
        data class HypercoinsBalanceChanged(val hypercoinsBalance: Int) : Message

        object PullToRefresh : Message

        object ClickedContinueInWeb : Message
        object ClickedStreakBarButtonItem : Message
        object ClickedGemsBarButtonItem : Message

        data class GetMagicLinkReceiveSuccess(val url: String) : Message
        object GetMagicLinkReceiveFailure : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
    }

    sealed interface Action {
        object FetchTrack : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        data class GetMagicLink(val path: HyperskillUrlPath) : Action

        sealed interface ViewAction : Action {
            data class OpenUrl(val url: String) : ViewAction
            object ShowGetMagicLinkError : ViewAction

            sealed interface NavigateTo : ViewAction {
                object ProfileTab : NavigateTo
                data class StepScreen(val stepId: Long) : NavigateTo
            }
        }
    }
}