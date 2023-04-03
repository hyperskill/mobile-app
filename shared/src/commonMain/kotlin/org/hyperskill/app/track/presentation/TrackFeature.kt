package org.hyperskill.app.track.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.topics.domain.model.Topic
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextFeature
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress

interface TrackFeature {
    data class State(
        val trackState: TrackState,
        val toolbarState: GamificationToolbarFeature.State,
        val topicsToDiscoverNextState: TopicsToDiscoverNextFeature.State
    ) {
        val isRefreshing: Boolean
            get() = trackState is TrackState.Content && trackState.isRefreshing ||
                toolbarState is GamificationToolbarFeature.State.Content && toolbarState.isRefreshing ||
                topicsToDiscoverNextState is TopicsToDiscoverNextFeature.State.Content && topicsToDiscoverNextState.isRefreshing
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
            val studyPlan: StudyPlan? = null
        ) : Message

        /**
         * A message that indicates about failure module data loading.
         */
        object TrackFailure : Message

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
        data class TopicsToDiscoverNextMessage(val message: TopicsToDiscoverNextFeature.Message) : Message
    }

    sealed interface Action {
        data class FetchTrack(val force: Boolean) : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        data class GetMagicLink(val path: HyperskillUrlPath) : Action

        /**
         * Action Wrappers
         */
        data class GamificationToolbarAction(val action: GamificationToolbarFeature.Action) : Action
        data class TopicsToDiscoverNextAction(val action: TopicsToDiscoverNextFeature.Action) : Action

        sealed interface ViewAction : Action {
            data class OpenUrl(val url: String) : ViewAction
            object ShowGetMagicLinkError : ViewAction

            /**
             * ViewAction Wrappers
             */
            data class GamificationToolbarViewAction(
                val viewAction: GamificationToolbarFeature.Action.ViewAction
            ) : ViewAction

            data class TopicsToDiscoverNextViewAction(
                val viewAction: TopicsToDiscoverNextFeature.Action.ViewAction
            ) : ViewAction
        }
    }
}