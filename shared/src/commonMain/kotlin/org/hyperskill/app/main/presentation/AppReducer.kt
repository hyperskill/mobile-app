package org.hyperskill.app.main.presentation

import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.main.presentation.AppFeature.Action
import org.hyperskill.app.main.presentation.AppFeature.Action.ViewAction.NavigateTo
import org.hyperskill.app.main.presentation.AppFeature.InternalAction
import org.hyperskill.app.main.presentation.AppFeature.InternalMessage
import org.hyperskill.app.main.presentation.AppFeature.Message
import org.hyperskill.app.main.presentation.AppFeature.State
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingReducer
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.model.isMobileLeaderboardsEnabled
import org.hyperskill.app.profile.domain.model.isMobileOnlySubscriptionEnabled
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryReducer
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.isFreemium
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

internal class AppReducer(
    private val streakRecoveryReducer: StreakRecoveryReducer,
    private val notificationClickHandlingReducer: NotificationClickHandlingReducer
) : StateReducer<State, Message, Action> {

    companion object {
        internal const val APP_SHOWS_COUNT_TILL_PAYWALL = 3
    }

    override fun reduce(
        state: State,
        message: Message
    ): ReducerResult =
        when (message) {
            is Message.Initialize -> {
                if (state is State.Idle || (state is State.NetworkError && message.forceUpdate)) {
                    State.Loading to setOf(
                        InternalAction.FetchAppStartupConfig(message.pushNotificationData),
                        Action.LogAppLaunchFirstTimeAnalyticEventIfNeeded
                    )
                } else {
                    null
                }
            }
            is Message.FetchAppStartupConfigSuccess ->
                handleFetchAppStartupConfigSuccess(state, message)
            is Message.FetchAppStartupConfigError ->
                if (state is State.Loading) {
                    State.NetworkError to emptySet()
                } else {
                    null
                }
            is Message.AppBecomesActive -> handleAppBecomesActive(state)
            is Message.UserAuthorized ->
                handleUserAuthorized(state, message)
            is Message.UserDeauthorized ->
                if (state is State.Ready && state.isAuthorized) {
                    val navigateToViewAction = when (message.reason) {
                        UserDeauthorized.Reason.TOKEN_REFRESH_FAILURE ->
                            NavigateTo.WelcomeScreen
                        UserDeauthorized.Reason.SIGN_OUT ->
                            NavigateTo.AuthScreen()
                    }

                    State.Ready(
                        isAuthorized = false,
                        isMobileLeaderboardsEnabled = false,
                        isMobileOnlySubscriptionEnabled = false,
                        canMakePayments = false
                    ) to getDeauthorizedUserActions() + setOf(navigateToViewAction)
                } else {
                    null
                }
            is Message.OpenAuthScreen ->
                state to setOf(NavigateTo.AuthScreen())
            is Message.OpenNewUserScreen ->
                state to setOf(NavigateTo.TrackSelectionScreen)
            is Message.StreakRecoveryMessage ->
                if (state is State.Ready) {
                    val (streakRecoveryState, streakRecoveryActions) =
                        reduceStreakRecoveryMessage(state.streakRecoveryState, message.message)
                    state.copy(streakRecoveryState = streakRecoveryState) to streakRecoveryActions
                } else {
                    null
                }
            is Message.NotificationClicked ->
                handleNotificationClicked(state, message)
            is Message.NotificationClickHandlingMessage ->
                state to reduceNotificationClickHandlingMessage(message.message)
            is InternalMessage.SubscriptionChanged ->
                handleSubscriptionChanged(state, message)
            is Message.IsPaywallShownChanged ->
                handleIsPaywallShownChanged(state, message)
            is InternalMessage.PaymentAbilityResult -> handlePaymentAbilityResult(state, message)
            is Message.WelcomeOnboardingCompleted -> handleWelcomeOnboardingCompleted(state, message)
        } ?: (state to emptySet())

    private fun handleFetchAppStartupConfigSuccess(
        state: State,
        message: Message.FetchAppStartupConfigSuccess
    ): ReducerResult =
        if (state is State.Loading) {
            val isAuthorized = !message.profile.isGuest

            val (streakRecoveryState, streakRecoveryActions) =
                if (isAuthorized && message.notificationData == null) {
                    reduceStreakRecoveryMessage(
                        StreakRecoveryFeature.State(),
                        StreakRecoveryFeature.Message.Initialize
                    )
                } else {
                    StreakRecoveryFeature.State() to emptySet()
                }

            val readyState = State.Ready(
                isAuthorized = isAuthorized,
                isMobileLeaderboardsEnabled = message.profile.features.isMobileLeaderboardsEnabled,
                streakRecoveryState = streakRecoveryState,
                appShowsCount = 0, // This is a hack to show paywall on the first app start
                subscription = message.subscription,
                isMobileOnlySubscriptionEnabled = message.profile.features.isMobileOnlySubscriptionEnabled,
                canMakePayments = message.canMakePayments
            )

            val actions: Set<Action> =
                buildSet {
                    if (isAuthorized) {
                        when {
                            message.notificationData != null ->
                                addAll(
                                    reduceNotificationClickHandlingMessage(
                                        NotificationClickHandlingFeature.Message.NotificationClicked(
                                            message.notificationData,
                                            isUserAuthorized = true,
                                            notificationLaunchedApp = true
                                        )
                                    )
                                )
                            WelcomeOnboardingFeature.shouldBeLaunchedOnStartup(message.profile) ->
                                add(NavigateTo.WelcomeOnboarding(message.profile))
                            shouldShowPaywall(readyState) ->
                                add(
                                    NavigateTo.StudyPlanWithPaywall(
                                        PaywallTransitionSource.APP_BECOMES_ACTIVE
                                    )
                                )
                            else ->
                                add(NavigateTo.StudyPlan)
                        }
                        addAll(
                            getOnAuthorizedAppStartUpActions(
                                profileId = message.profile.id,
                                subscription = message.subscription
                            )
                        )
                    } else {
                        if (message.notificationData != null) {
                            addAll(
                                reduceNotificationClickHandlingMessage(
                                    NotificationClickHandlingFeature.Message.NotificationClicked(
                                        message.notificationData,
                                        isUserAuthorized = false,
                                        notificationLaunchedApp = true
                                    )
                                )
                            )
                        }
                        addAll(getNotAuthorizedAppStartUpActions())
                        add(NavigateTo.WelcomeScreen)
                    }
                    addAll(streakRecoveryActions)
                }

            readyState.incrementAppShowsCount() to actions
        } else {
            state to emptySet()
        }

    private fun handleUserAuthorized(
        state: State,
        message: Message.UserAuthorized
    ): ReducerResult =
        if (state is State.Ready && !state.isAuthorized) {
            val authState = State.Ready(
                isAuthorized = true,
                isMobileLeaderboardsEnabled = message.profile.features.isMobileLeaderboardsEnabled,
                isMobileOnlySubscriptionEnabled = message.profile.features.isMobileOnlySubscriptionEnabled,
                canMakePayments = false
            )
            authState to getAuthorizedUserActions(message.profile, message.isNotificationPermissionGranted)
        } else {
            state to emptySet()
        }

    private fun handleAppBecomesActive(state: State): ReducerResult =
        if (state is State.Ready) {
            state.incrementAppShowsCount() to buildSet {
                when {
                    shouldShowPaywall(state) ->
                        add(NavigateTo.Paywall(PaywallTransitionSource.APP_BECOMES_ACTIVE))
                    // Fetch actual payment ability state if it's not possible to make payment at the moment
                    !state.canMakePayments && state.subscription?.isFreemium == true ->
                        add(InternalAction.FetchPaymentAbility)
                }
                if (state.isAuthorized) {
                    add(InternalAction.FetchSubscription(forceUpdate = true))
                }
            }
        } else {
            state to emptySet()
        }

    private fun shouldShowPaywall(state: State.Ready): Boolean =
        state.isAuthorized &&
            state.isMobileOnlySubscriptionEnabled &&
            state.canMakePayments &&
            state.subscription?.isFreemium == true &&
            !state.isPaywallShown &&
            state.appShowsCount % APP_SHOWS_COUNT_TILL_PAYWALL == 0

    private fun reduceStreakRecoveryMessage(
        state: StreakRecoveryFeature.State,
        message: StreakRecoveryFeature.Message
    ): Pair<StreakRecoveryFeature.State, Set<Action>> {
        val (streakRecoveryState, streakRecoveryActions) = streakRecoveryReducer.reduce(state, message)

        val actions = streakRecoveryActions
            .map {
                if (it is StreakRecoveryFeature.Action.ViewAction) {
                    Action.ViewAction.StreakRecoveryViewAction(it)
                } else {
                    Action.StreakRecoveryAction(it)
                }
            }
            .toSet()

        return streakRecoveryState to actions
    }

    private fun handleNotificationClicked(
        state: State,
        message: Message.NotificationClicked
    ): ReducerResult =
        if (state is State.Ready) {
            state to reduceNotificationClickHandlingMessage(
                NotificationClickHandlingFeature.Message.NotificationClicked(
                    notificationData = message.notificationData,
                    isUserAuthorized = state.isAuthorized,
                    notificationLaunchedApp = false
                )
            )
        } else {
            state to emptySet()
        }

    private fun reduceNotificationClickHandlingMessage(
        message: NotificationClickHandlingFeature.Message
    ): Set<Action> {
        val (_, notificationClickedHandlingActions) =
            notificationClickHandlingReducer.reduce(NotificationClickHandlingFeature.State, message)
        return notificationClickedHandlingActions.map {
            if (it is NotificationClickHandlingFeature.Action.ViewAction) {
                Action.ViewAction.ClickedNotificationViewAction(it)
            } else {
                Action.ClickedNotificationAction(it)
            }
        }.toSet()
    }

    private fun getOnAuthorizedAppStartUpActions(
        profileId: Long,
        subscription: Subscription?
    ): Set<Action> =
        setOfNotNull(
            Action.IdentifyUserInSentry(userId = profileId),
            subscription?.let(InternalAction::RefreshSubscriptionOnExpiration),
            Action.UpdateDailyLearningNotificationTime,
            Action.SendPushNotificationsToken
        )

    private fun getNotAuthorizedAppStartUpActions(): Set<Action> =
        setOf(Action.ClearUserInSentry)

    private fun getAuthorizedUserActions(profile: Profile, isNotificationPermissionGranted: Boolean): Set<Action> =
        setOf(
            if (WelcomeOnboardingFeature.shouldBeLaunchedAfterAuthorization(profile, isNotificationPermissionGranted)) {
                NavigateTo.WelcomeOnboarding(profile)
            } else {
                NavigateTo.StudyPlan
            },
            InternalAction.FetchSubscription(forceUpdate = false),
            InternalAction.IdentifyUserInPurchaseSdk(userId = profile.id),
            Action.IdentifyUserInSentry(userId = profile.id),
            Action.UpdateDailyLearningNotificationTime,
            Action.SendPushNotificationsToken
        )

    private fun getDeauthorizedUserActions(): Set<Action> =
        setOf(
            Action.ClearUserInSentry,
            InternalAction.CancelSubscriptionRefresh
        )

    private fun handleSubscriptionChanged(
        state: State,
        message: InternalMessage.SubscriptionChanged
    ): ReducerResult =
        if (state is State.Ready) {
            state.copy(subscription = message.subscription) to setOf(
                InternalAction.RefreshSubscriptionOnExpiration(message.subscription)
            )
        } else {
            state to emptySet()
        }

    private fun handleIsPaywallShownChanged(
        state: State,
        message: Message.IsPaywallShownChanged
    ): ReducerResult =
        if (state is State.Ready) {
            state.copy(isPaywallShown = message.isPaywallShown) to emptySet()
        } else {
            state to emptySet()
        }

    private fun handlePaymentAbilityResult(
        state: State,
        message: InternalMessage.PaymentAbilityResult
    ): ReducerResult =
        if (state is State.Ready) {
            state.copy(canMakePayments = message.canMakePayments) to emptySet()
        } else {
            state to emptySet()
        }

    private fun handleWelcomeOnboardingCompleted(
        state: State,
        message: Message.WelcomeOnboardingCompleted
    ): ReducerResult =
        state to setOf(
            if (message.stepRoute != null) {
                NavigateTo.StudyPlanWithStep(message.stepRoute)
            } else {
                NavigateTo.StudyPlan
            }
        )
}