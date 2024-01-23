package org.hyperskill.app.main.presentation

import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.domain.platform.PlatformType
import org.hyperskill.app.main.presentation.AppFeature.Action
import org.hyperskill.app.main.presentation.AppFeature.Message
import org.hyperskill.app.main.presentation.AppFeature.State
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingReducer
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.model.isMobileLeaderboardsEnabled
import org.hyperskill.app.profile.domain.model.isNewUser
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryReducer
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.OnboardingFlowFinishReason
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingReducer
import org.hyperskill.app.welcome_onboarding.presentation.getFinishAction
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

internal class AppReducer(
    private val streakRecoveryReducer: StreakRecoveryReducer,
    private val notificationClickHandlingReducer: NotificationClickHandlingReducer,
    private val welcomeOnboardingReducer: WelcomeOnboardingReducer,
    private val platformType: PlatformType
) : StateReducer<State, Message, Action> {
    override fun reduce(
        state: State,
        message: Message
    ): ReducerResult =
        when (message) {
            is Message.Initialize -> {
                if (state is State.Idle || (state is State.NetworkError && message.forceUpdate)) {
                    State.Loading to setOf(Action.DetermineUserAccountStatus(message.pushNotificationData))
                } else {
                    null
                }
            }
            is Message.UserAccountStatus ->
                handleUserAccountStatus(state, message)
            is Message.UserAccountStatusError ->
                if (state is State.Loading) {
                    State.NetworkError to emptySet()
                } else {
                    null
                }
            is Message.UserAuthorized ->
                handleUserAuthorized(state, message)
            is Message.UserDeauthorized ->
                if (state is State.Ready && state.isAuthorized) {
                    val navigateToViewAction = when (message.reason) {
                        UserDeauthorized.Reason.TOKEN_REFRESH_FAILURE ->
                            Action.ViewAction.NavigateTo.OnboardingScreen
                        UserDeauthorized.Reason.SIGN_OUT ->
                            Action.ViewAction.NavigateTo.AuthScreen()
                    }

                    State.Ready(
                        isAuthorized = false,
                        isMobileLeaderboardsEnabled = false
                    ) to setOf(Action.ClearUserInSentry, navigateToViewAction)
                } else {
                    null
                }
            is Message.OpenAuthScreen ->
                state to setOf(Action.ViewAction.NavigateTo.AuthScreen())
            is Message.OpenNewUserScreen ->
                state to setOf(Action.ViewAction.NavigateTo.TrackSelectionScreen)
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
            is Message.WelcomeOnboardingMessage ->
                reduceWelcomeOnboardingMessage(state, message.message)
        } ?: (state to emptySet())

    private fun handleUserAccountStatus(
        state: State,
        message: Message.UserAccountStatus
    ): ReducerResult =
        if (state is State.Loading) {
            val isAuthorized = !message.profile.isGuest

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
                            message.profile.isNewUser ->
                                add(Action.ViewAction.NavigateTo.TrackSelectionScreen)
                            else ->
                                add(Action.ViewAction.NavigateTo.StudyPlan)
                        }
                        addAll(getOnAuthorizedAppStartUpActions(message.profile.id, platformType))
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
                        add(Action.ViewAction.NavigateTo.OnboardingScreen)
                    }
                }

            val (streakRecoveryState, streakRecoveryActions) =
                if (isAuthorized && message.notificationData == null) {
                    reduceStreakRecoveryMessage(
                        StreakRecoveryFeature.State(),
                        StreakRecoveryFeature.Message.Initialize
                    )
                } else {
                    StreakRecoveryFeature.State() to emptySet()
                }

            State.Ready(
                isAuthorized = isAuthorized,
                isMobileLeaderboardsEnabled = message.profile.features.isMobileLeaderboardsEnabled,
                streakRecoveryState = streakRecoveryState
            ) to actions + streakRecoveryActions
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
                isMobileLeaderboardsEnabled = message.profile.features.isMobileLeaderboardsEnabled
            )
            val (onboardingState, onboardingActions) = reduceWelcomeOnboardingMessage(
                WelcomeOnboardingFeature.State(),
                WelcomeOnboardingFeature.Message.OnboardingFlowRequested(
                    message.profile,
                    message.isNotificationPermissionGranted
                )
            )
            authState.copy(welcomeOnboardingState = onboardingState) to
                getAuthorizedUserActions(message.profile) + onboardingActions
        } else {
            state to emptySet()
        }

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

    private fun reduceWelcomeOnboardingMessage(
        state: State,
        message: WelcomeOnboardingFeature.Message
    ): ReducerResult =
        if (state is State.Ready) {
            val (onboardingState, actions) =
                reduceWelcomeOnboardingMessage(state.welcomeOnboardingState, message)
            state.copy(welcomeOnboardingState = onboardingState) to actions
        } else {
            state to emptySet()
        }

    private fun reduceWelcomeOnboardingMessage(
        state: WelcomeOnboardingFeature.State,
        message: WelcomeOnboardingFeature.Message
    ): Pair<WelcomeOnboardingFeature.State, Set<Action>> {
        val (onboardingState, onboardingActions) =
            welcomeOnboardingReducer.reduce(state, message)
        val finishAction = onboardingActions.getFinishAction()
        return if (finishAction != null) {
            onboardingState to handleWelcomeOnboardingFinishAction(finishAction)
        } else {
            onboardingState to
                onboardingActions.map {
                    if (it is WelcomeOnboardingFeature.Action.ViewAction) {
                        Action.ViewAction.WelcomeOnboardingViewAction(it)
                    } else {
                        Action.WelcomeOnboardingAction(it)
                    }
                }.toSet()
        }
    }

    private fun handleWelcomeOnboardingFinishAction(
        finishAction: WelcomeOnboardingFeature.Action.OnboardingFlowFinished
    ): Set<Action> =
        setOf(
            when (val reason = finishAction.reason) {
                is OnboardingFlowFinishReason.NotificationOnboardingFinished ->
                    if (reason.profile?.isNewUser == true) {
                        Action.ViewAction.NavigateTo.TrackSelectionScreen
                    } else {
                        Action.ViewAction.NavigateTo.StudyPlan
                    }
                OnboardingFlowFinishReason.PaywallCompleted,
                OnboardingFlowFinishReason.FirstProblemOnboardingFinished ->
                    Action.ViewAction.NavigateTo.StudyPlan
            }
        )

    private fun getOnAuthorizedAppStartUpActions(
        profileId: Long,
        platformType: PlatformType
    ): Set<Action> =
        setOfNotNull(
            Action.IdentifyUserInSentry(userId = profileId),
            Action.UpdateDailyLearningNotificationTime,
            if (platformType == PlatformType.ANDROID) {
                // Don't send push token on app startup for IOS
                // because of custom token sending logic on IOS on app startup
                Action.SendPushNotificationsToken
            } else {
                null
            }
        )

    private fun getNotAuthorizedAppStartUpActions(): Set<Action> =
        setOf(Action.ClearUserInSentry)

    private fun getAuthorizedUserActions(profile: Profile): Set<Action> =
        setOf(
            Action.IdentifyUserInSentry(userId = profile.id),
            Action.UpdateDailyLearningNotificationTime,
            Action.SendPushNotificationsToken
        )
}