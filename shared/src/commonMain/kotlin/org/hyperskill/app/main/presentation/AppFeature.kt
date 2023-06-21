package org.hyperskill.app.main.presentation

import kotlinx.serialization.Serializable
import org.hyperskill.app.auth.domain.model.UserDeauthorized.Reason
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature

interface AppFeature {
    @Serializable
    sealed interface State {
        @Serializable
        object Idle : State

        @Serializable
        object Loading : State

        @Serializable
        object NetworkError : State

        @Serializable
        data class Ready(val isAuthorized: Boolean) : State
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean = false) : Message
        data class UserAuthorized(val profile: Profile) : Message
        data class UserDeauthorized(val reason: Reason) : Message
        data class UserAccountStatus(val profile: Profile) : Message
        object UserAccountStatusError : Message
        object OpenAuthScreen : Message
        object OpenNewUserScreen : Message

        /**
         * Message Wrappers
         */
        data class StreakRecoveryMessage(val message: StreakRecoveryFeature.Message) : Message
    }

    sealed interface Action {
        object DetermineUserAccountStatus : Action

        /**
         * Action Wrappers
         */
        data class StreakRecoveryAction(val action: StreakRecoveryFeature.Action) : Action

        /**
         * Sentry
         */
        data class IdentifyUserInSentry(val userId: Long) : Action
        object ClearUserInSentry : Action

        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                object HomeScreen : NavigateTo
                data class AuthScreen(val isInSignUpMode: Boolean = false) : NavigateTo
                object TrackSelectionScreen : NavigateTo
                object OnboardingScreen : NavigateTo
            }

            /**
             * ViewAction Wrappers
             */
            data class StreakRecoveryViewAction(val viewAction: StreakRecoveryFeature.Action.ViewAction) : ViewAction
        }
    }
}