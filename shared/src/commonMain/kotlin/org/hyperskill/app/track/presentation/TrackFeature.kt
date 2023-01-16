package org.hyperskill.app.track.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.topics.domain.model.Topic
import org.hyperskill.app.track.domain.model.StudyPlan
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress

interface TrackFeature {
    data class State(
        val trackState: TrackState,
        val toolbarState: GamificationToolbarFeature.State
    ) {
        val isRefreshing: Boolean
            get() = trackState is TrackState.Content && trackState.isRefreshing ||
                toolbarState is GamificationToolbarFeature.State.Content && toolbarState.isRefreshing
    }

    sealed interface TrackState {
        /**
         * Represents initial state.
         */
        object Idle : TrackState

        /**
         * Represents a state when loading track screen data.
         */
        object Loading : TrackState

        /**
         * Represents a state when track screen data successfully loaded.
         *
         * @property track Current user profile selected track.
         * @property trackProgress Current user profile selected track progress.
         * @property studyPlan Current user profile study plan.
         * @property topicsToDiscoverNext Current user profile uncompleted topics (theory to discover next) for current stage.
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
            internal val isRefreshing: Boolean = false,
            val isLoadingMagicLink: Boolean = false
        ) : TrackState

        /**
         * Represents a state when track screen data failed to load.
         */
        object NetworkError : TrackState
    }

    sealed interface Message {
        /**
         * A message that triggers the process of loading all the necessary data for module.
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
            val topicsToDiscoverNext: List<Topic>
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

        object PullToRefresh : Message

        object ClickedContinueInWeb : Message

        data class GetMagicLinkReceiveSuccess(val url: String) : Message
        object GetMagicLinkReceiveFailure : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message

        /**
         * Message Wrappers
         */
        data class GamificationToolbarMessage(val message: GamificationToolbarFeature.Message) : Message
    }

    sealed interface Action {
        object FetchTrack : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        data class GetMagicLink(val path: HyperskillUrlPath) : Action

        /**
         * Action Wrappers
         */
        data class GamificationToolbarAction(val action: GamificationToolbarFeature.Action) : Action

        sealed interface ViewAction : Action {
            data class OpenUrl(val url: String) : ViewAction
            object ShowGetMagicLinkError : ViewAction

            data class GamificationToolbarViewAction(
                val viewAction: GamificationToolbarFeature.Action.ViewAction
            ) : ViewAction

            sealed interface NavigateTo : ViewAction {
                data class StepScreen(val stepId: Long) : NavigateTo
            }
        }
    }
}