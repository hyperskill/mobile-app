package org.hyperskill.main

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.main.presentation.AppFeature
import org.hyperskill.app.main.presentation.AppReducer
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingReducer
import org.hyperskill.app.notification.remote.domain.model.PushNotificationCategory
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData
import org.hyperskill.app.notification.remote.domain.model.PushNotificationType
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.profile.domain.model.FeatureKeys
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryReducer
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingReducer
import org.hyperskill.profile.stub
import org.hyperskill.subscriptions.stub

class AppFeatureTest {
    private val appReducer = AppReducer(
        streakRecoveryReducer = StreakRecoveryReducer(resourceProvider = ResourceProviderStub()),
        notificationClickHandlingReducer = NotificationClickHandlingReducer(),
        welcomeOnboardingReducer = WelcomeOnboardingReducer()
    )

    @Test
    fun `Streak recovery should be initialized only when user is authorized and already selected track`() {
        val (_, actions) = appReducer.reduce(
            AppFeature.State.Loading,
            AppFeature.Message.FetchAppStartupConfigSuccess(
                profile = Profile.stub(isGuest = false, trackId = 1),
                notificationData = null,
                subscription = null,
                canMakePayments = false
            )
        )
        assertTrue {
            actions.any {
                it is AppFeature.Action.StreakRecoveryAction &&
                    it.action is StreakRecoveryFeature.InternalAction.FetchStreak
            }
        }
    }

    @Test
    fun `Streak recovery should NOT be initialized when user is NOT authorized`() {
        val (_, actions) = appReducer.reduce(
            AppFeature.State.Loading,
            AppFeature.Message.FetchAppStartupConfigSuccess(
                profile = Profile.stub(isGuest = true),
                notificationData = null,
                subscription = null,
                canMakePayments = false
            )
        )
        assertNoStreakRecoveryActions(actions)
    }

    @Test
    fun `Streak recovery should NOT be initialized in case of push notification handling`() {
        val (_, actions) = appReducer.reduce(
            AppFeature.State.Loading,
            AppFeature.Message.FetchAppStartupConfigSuccess(
                profile = Profile.stub(isGuest = true, trackId = 1),
                subscription = null,
                canMakePayments = false,
                notificationData = PushNotificationData(
                    typeString = PushNotificationType.STREAK_NEW.name,
                    categoryString = PushNotificationCategory.CONTINUE_LEARNING.backendName!!
                )
            )
        )
        assertNoStreakRecoveryActions(actions)
    }

    private fun assertNoStreakRecoveryActions(actions: Set<AppFeature.Action>) {
        assertTrue {
            actions.none {
                it is AppFeature.Action.StreakRecoveryAction
            }
        }
    }

    @Test
    fun `Paywall should be shown for user with freemium subscription on app startup`() {
        val (state, actions) = appReducer.reduce(
            AppFeature.State.Loading,
            AppFeature.Message.FetchAppStartupConfigSuccess(
                profile = Profile.stub(
                    isGuest = false,
                    trackId = 1,
                    featuresMap = mapOf(FeatureKeys.MOBILE_ONLY_SUBSCRIPTION to true)
                ),
                subscription = Subscription.stub(SubscriptionType.FREEMIUM),
                notificationData = null,
                canMakePayments = true
            )
        )
        assertContains(
            actions,
            AppFeature.Action.ViewAction.NavigateTo.StudyPlanWithPaywall(PaywallTransitionSource.APP_BECOMES_ACTIVE)
        )
        assertTrue {
            state is AppFeature.State.Ready && state.appShowsCount == 1
        }
    }

    @Test
    fun `Paywall should not be shown for user with non freemium subscription on app startup`() {
        SubscriptionType
            .values()
            .filterNot { it == SubscriptionType.FREEMIUM }
            .forEach { subscriptionType ->
                val (_, actions) = appReducer.reduce(
                    AppFeature.State.Loading,
                    AppFeature.Message.FetchAppStartupConfigSuccess(
                        profile = Profile.stub(
                            isGuest = false,
                            trackId = 1,
                            featuresMap = mapOf(FeatureKeys.MOBILE_ONLY_SUBSCRIPTION to true)
                        ),
                        subscription = Subscription.stub(subscriptionType),
                        notificationData = null,
                        canMakePayments = true
                    )
                )
                assertNoPaywallViewAction(actions)
            }
    }

    @Test
    fun `Paywall should be shown for user with freemium subscription every 3 time when app shown`() {
        var state: AppFeature.State = AppFeature.State.Ready(
            isAuthorized = true,
            isMobileLeaderboardsEnabled = false,
            subscription = Subscription.stub(SubscriptionType.FREEMIUM),
            isMobileOnlySubscriptionEnabled = true,
            canMakePayments = true
        )
        for (i in 1..AppReducer.APP_SHOWS_COUNT_TILL_PAYWALL + 1) {
            val (newState, actions) = appReducer.reduce(
                state,
                AppFeature.Message.AppBecomesActive
            )
            state = newState

            if (i % AppReducer.APP_SHOWS_COUNT_TILL_PAYWALL == 0) {
                assertContains(
                    actions,
                    AppFeature.Action.ViewAction.NavigateTo.Paywall(
                        PaywallTransitionSource.APP_BECOMES_ACTIVE
                    )
                )
            } else {
                assertNoPaywallViewAction(actions)
            }
        }
    }

    @Test
    fun `Paywall should not be shown if mobile only subscription feature is disabled`() {
        val (_, actions) = appReducer.reduce(
            AppFeature.State.Loading,
            AppFeature.Message.FetchAppStartupConfigSuccess(
                profile = Profile.stub(
                    isGuest = false,
                    trackId = 1,
                    featuresMap = mapOf(FeatureKeys.MOBILE_ONLY_SUBSCRIPTION to false)
                ),
                subscription = Subscription.stub(SubscriptionType.FREEMIUM),
                notificationData = null,
                canMakePayments = true
            )
        )
        assertNoPaywallViewAction(actions)
    }

    private fun assertNoPaywallViewAction(actions: Set<AppFeature.Action>) {
        assertTrue {
            actions.none {
                it is AppFeature.Action.ViewAction.NavigateTo.StudyPlanWithPaywall
            }
        }
    }
}