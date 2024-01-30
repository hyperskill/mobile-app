package org.hyperskill.app.main.presentation

import kotlinx.serialization.Serializable
import org.hyperskill.app.auth.domain.model.UserDeauthorized.Reason
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature

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
        data class Ready(
            val isAuthorized: Boolean,
            val isMobileLeaderboardsEnabled: Boolean,
            internal val streakRecoveryState: StreakRecoveryFeature.State = StreakRecoveryFeature.State(),
            internal val welcomeOnboardingState: WelcomeOnboardingFeature.State = WelcomeOnboardingFeature.State()
        ) : State
    }

    sealed interface Message {
        data class Initialize(
            val pushNotificationData: PushNotificationData?,
            val forceUpdate: Boolean = false
        ) : Message

        data class UserAccountStatus(
            val profile: Profile,
            val notificationData: PushNotificationData?
        ) : Message
        object UserAccountStatusError : Message

        data class UserAuthorized(
            val profile: Profile,
            val isNotificationPermissionGranted: Boolean
        ) : Message
        data class UserDeauthorized(val reason: Reason) : Message

        object OpenAuthScreen : Message
        object OpenNewUserScreen : Message

        data class NotificationClicked(
            val notificationData: PushNotificationData
        ) : Message

        /**
         * Message Wrappers
         */
        data class StreakRecoveryMessage(val message: StreakRecoveryFeature.Message) : Message

        data class NotificationClickHandlingMessage(
            val message: NotificationClickHandlingFeature.Message
        ) : Message

        data class WelcomeOnboardingMessage(
            val message: WelcomeOnboardingFeature.Message
        ) : Message
    }

    sealed interface Action {
        data class DetermineUserAccountStatus(
            val pushNotificationData: PushNotificationData?
        ) : Action

        object UpdateDailyLearningNotificationTime : Action

        object SendPushNotificationsToken : Action

        /**
         * Action Wrappers
         */
        data class StreakRecoveryAction(val action: StreakRecoveryFeature.Action) : Action

        data class ClickedNotificationAction(
            val action: NotificationClickHandlingFeature.Action
        ) : Action

        data class WelcomeOnboardingAction(
            val action: WelcomeOnboardingFeature.Action
        ) : Action

        /**
         * Sentry
         */
        data class IdentifyUserInSentry(val userId: Long) : Action
        object ClearUserInSentry : Action

        data class IdentifyUserInPurchaseSdk(val userId: Long) : Action

        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                data class AuthScreen(val isInSignUpMode: Boolean = false) : NavigateTo
                object TrackSelectionScreen : NavigateTo
                object OnboardingScreen : NavigateTo
                object StudyPlan : NavigateTo
            }

            /**
             * ViewAction Wrappers
             */
            data class StreakRecoveryViewAction(val viewAction: StreakRecoveryFeature.Action.ViewAction) : ViewAction

            data class ClickedNotificationViewAction(
                val viewAction: NotificationClickHandlingFeature.Action.ViewAction
            ) : ViewAction

            data class WelcomeOnboardingViewAction(
                val viewAction: WelcomeOnboardingFeature.Action.ViewAction
            ) : ViewAction
        }
    }
}