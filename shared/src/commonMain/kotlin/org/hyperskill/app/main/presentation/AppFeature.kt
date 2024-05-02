package org.hyperskill.app.main.presentation

import kotlinx.serialization.Serializable
import org.hyperskill.app.auth.domain.model.UserDeauthorized.Reason
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature

object AppFeature {

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
            internal val welcomeOnboardingState: WelcomeOnboardingFeature.State = WelcomeOnboardingFeature.State(),
            internal val isMobileOnlySubscriptionEnabled: Boolean,
            internal val canMakePayments: Boolean,
            internal val subscription: Subscription? = null,
            internal val appShowsCount: Int = 1,
            internal val isPaywallShown: Boolean = false
        ) : State {
            internal fun incrementAppShowsCount(): Ready =
                // ALTAPPS-1151: Fix redundant paywall shows -> increment app shows count only if paywall is not shown
                if (!isPaywallShown) {
                    copy(appShowsCount = appShowsCount + 1)
                } else {
                    this
                }
        }
    }

    sealed interface Message {
        data class Initialize(
            val pushNotificationData: PushNotificationData?,
            val forceUpdate: Boolean = false
        ) : Message

        object AppBecomesActive : Message

        data class FetchAppStartupConfigSuccess(
            val profile: Profile,
            val subscription: Subscription?,
            val notificationData: PushNotificationData?,
            val canMakePayments: Boolean
        ) : Message
        object FetchAppStartupConfigError : Message

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

        data class IsPaywallShownChanged(
            val isPaywallShown: Boolean
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

    internal sealed interface InternalMessage : Message {
        data class PaymentAbilityResult(val canMakePayments: Boolean) : InternalMessage

        data class SubscriptionChanged(
            val subscription: Subscription
        ) : InternalMessage
    }

    sealed interface Action {
        object UpdateDailyLearningNotificationTime : Action

        object SendPushNotificationsToken : Action

        object LogAppLaunchFirstTimeAnalyticEventIfNeeded : Action

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

        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                data class AuthScreen(val isInSignUpMode: Boolean = false) : NavigateTo
                object TrackSelectionScreen : NavigateTo
                object WelcomeScreen : NavigateTo
                object StudyPlan : NavigateTo
                data class Paywall(val paywallTransitionSource: PaywallTransitionSource) : NavigateTo
                data class StudyPlanWithPaywall(
                    val paywallTransitionSource: PaywallTransitionSource
                ) : NavigateTo
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

    internal sealed interface InternalAction : Action {
        /**
         * Fetch data required for the App startup
         * and identify user in Purchase SDK if the user has already authorized.
         */
        data class FetchAppStartupConfig(val pushNotificationData: PushNotificationData?) : InternalAction

        object FetchSubscription : InternalAction

        data class IdentifyUserInPurchaseSdk(val userId: Long) : InternalAction

        /**
         * Check whether it's possible to make payment from user's device and account.
         * @see [InternalMessage.PaymentAbilityResult]
         */
        object FetchPaymentAbility : InternalAction

        data class RefreshSubscriptionOnExpiration(val subscription: Subscription) : InternalAction

        object CancelSubscriptionRefresh : InternalAction
    }
}