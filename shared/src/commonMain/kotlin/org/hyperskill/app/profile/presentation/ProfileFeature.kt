package org.hyperskill.app.profile.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.streak.domain.model.Streak

interface ProfileFeature {
    sealed interface State {
        /**
         * Represents initial state.
         */
        object Idle : State

        /**
         * Represents a state when loading profile screen data.
         */
        object Loading : State

        /**
         * Represents a state when profile screen data successfully loaded.
         *
         * @property profile User profile model.
         * @property streak User profile streak.
         * @property isRefreshing A boolean flag that indicates about is pull-to-refresh is ongoing.
         * @property isLinkLoadingShown A boolean flag that indicates about magic link loading.
         * @see Profile
         * @see Streak
         */
        data class Content(
            val profile: Profile,
            val streak: Streak?,
            val isRefreshing: Boolean = false,
            val isLinkLoadingShown: Boolean = false
        ) : State

        /**
         * Represents a state when profile screen data failed to load.
         */
        object Error : State
    }

    sealed interface Message {
        data class Initialize(
            val isInitCurrent: Boolean = true,
            val profileId: Long? = null,
            val forceUpdate: Boolean = false
        ) : Message

        sealed interface ProfileLoaded : Message {
            data class Success(val profile: Profile, val streak: Streak?) : ProfileLoaded
            data class Error(val errorMsg: String) : ProfileLoaded
        }

        data class PullToRefresh(
            val isRefreshCurrent: Boolean = true,
            val profileId: Long? = null
        ) : Message

        data class StepSolved(val id: Long) : Message

        object ClickedViewFullProfile : Message

        data class LinkReceived(val url: String) : Message
        object LinkReceiveFailed : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
        object ClickedSettingsEventMessage : Message
        data class ClickedDailyStudyRemindsEventMessage(val isEnabled: Boolean) : Message
        object ClickedDailyStudyRemindsTimeEventMessage : Message
        object ClickedViewFullProfileEventMessage : Message
    }

    sealed interface Action {
        data class FetchProfile(val profileId: Long) : Action
        object FetchCurrentProfile : Action
        data class UpdateStreakInfo(val streak: Streak?) : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        data class GetLink(val path: HyperskillUrlPath) : Action

        sealed interface ViewAction : Action {
            data class FollowLink(val url: String) : ViewAction
        }
    }
}