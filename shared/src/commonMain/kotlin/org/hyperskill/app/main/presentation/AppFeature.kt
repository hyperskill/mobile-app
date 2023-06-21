package org.hyperskill.app.main.presentation

import kotlinx.serialization.Serializable
import org.hyperskill.app.auth.domain.model.UserDeauthorized.Reason
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData
import org.hyperskill.app.profile.domain.model.Profile

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
        data class NotificationClicked(
            val notificationData: PushNotificationData
        ) : Message

        data class ClickedNotificationMessage(
            val message: NotificationClickHandlingFeature.Message
        ) : Message
    }

    sealed interface Action {
        object DetermineUserAccountStatus : Action

        /**
         * Sentry
         */
        data class IdentifyUserInSentry(val userId: Long) : Action
        object ClearUserInSentry : Action

        data class ClickedNotificationAction(
            val action: NotificationClickHandlingFeature.Action
        ) : Action

        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                object HomeScreen : NavigateTo
                data class AuthScreen(val isInSignUpMode: Boolean = false) : NavigateTo
                object TrackSelectionScreen : NavigateTo
                object OnboardingScreen : NavigateTo
            }

            data class ClickedNotificationViewAction(
                val viewAction: NotificationClickHandlingFeature.Action.ViewAction
            ) : ViewAction
        }
    }
}