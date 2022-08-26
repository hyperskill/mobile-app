package org.hyperskill.app.profile.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature
import org.hyperskill.app.streak.domain.model.Streak

interface ProfileFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(
            val profile: Profile,
            val streak: Streak?
        ) : State
    }

    sealed interface Message {
        data class Init(
            val isInitCurrent: Boolean = true,
            val profileId: Long? = null,
            val forceUpdate: Boolean = false
        ) : Message

        sealed interface ProfileLoaded : Message {
            data class Success(val profile: Profile, val streak: Streak?) : ProfileLoaded
            data class Error(val errorMsg: String) : ProfileLoaded
        }

        data class StepSolved(val id: Long) : Message

        /**
         * Analytic
         */
        object ProfileViewedEventMessage : Message
        object ProfileClickedSettingsEventMessage : Message
        object ProfileClickedDailyStudyRemindsEventMessage : Message
        object ProfileClickedDailyStudyRemindsTimeEventMessage : Message
        object ProfileClickedViewFullProfileEventMessage : Message
    }

    sealed interface Action {
        data class FetchProfile(val profileId: Long) : Action
        object FetchCurrentProfile : Action
        data class UpdateStreakInfo(val streak: Streak?) : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed class ViewAction : Action
    }
}