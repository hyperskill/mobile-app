package org.hyperskill.app.main.presentation

import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.domain.platform.PlatformType
import org.hyperskill.app.main.presentation.AppFeature.Action
import org.hyperskill.app.main.presentation.AppFeature.Message
import org.hyperskill.app.main.presentation.AppFeature.State
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingReducer
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.model.isNewUser
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryReducer
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

class AppReducer(
    private val streakRecoveryReducer: StreakRecoveryReducer,
    private val notificationClickHandlingReducer: NotificationClickHandlingReducer,
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

                    State.Ready(isAuthorized = false) to setOf(Action.ClearUserInSentry, navigateToViewAction)
                } else {
                    null
                }
            is Message.NotificationOnboardingDataFetched ->
                handleNotificationOnboardingDataFetched(state, message)
            is Message.NotificationOnboardingCompleted ->
                handleNotificationOnboardingCompleted(state)
            is Message.FirstProblemOnboardingDataFetched ->
                handleFirstProblemOnboardingDataFetched(state, message)
            is Message.FirstProblemOnboardingCompleted ->
                handleFirstProblemOnboardingCompleted(state, message)
            is Message.OpenAuthScreen ->
                state to setOf(Action.ViewAction.NavigateTo.AuthScreen())
            is Message.OpenNewUserScreen ->
                state to setOf(Action.ViewAction.NavigateTo.TrackSelectionScreen)
            is Message.StreakRecoveryMessage ->
                state to reduceStreakRecoveryMessage(message.message)
            is Message.NotificationClicked ->
                handleNotificationClicked(state, message)
            is Message.NotificationClickHandlingMessage ->
                state to reduceNotificationClickHandlingMessage(message.message)
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
                                add(Action.ViewAction.NavigateTo.HomeScreen)
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

                    if (isAuthorized && message.notificationData == null) {
                        addAll(reduceStreakRecoveryMessage(StreakRecoveryFeature.Message.Initialize))
                    }
                }

            State.Ready(isAuthorized = isAuthorized) to actions
        } else {
            state to emptySet()
        }

    private fun handleUserAuthorized(
        state: State,
        message: Message.UserAuthorized
    ): ReducerResult =
        if (state is State.Ready && !state.isAuthorized) {
            val navigationLogicAction = if (message.isNotificationPermissionGranted) {
                getAuthorizedUserNavigationAction(message.profile)
            } else {
                Action.FetchNotificationOnboardingData
            }
            State.Ready(
                isAuthorized = true,
                profile = if (!message.isNotificationPermissionGranted) message.profile else null
            ) to getAuthorizedUserActions(message.profile) + navigationLogicAction
        } else {
            state to emptySet()
        }

    private fun handleNotificationOnboardingDataFetched(
        state: State,
        message: Message.NotificationOnboardingDataFetched
    ): ReducerResult =
        if (state is State.Ready && state.profile != null) {
            if (!message.wasNotificationOnBoardingShown) {
                state to setOf(Action.ViewAction.NavigateTo.NotificationOnBoardingScreen)
            } else {
                navigateUserAfterNotificationOnboarding(state.profile)
            }
        } else {
            state to emptySet()
        }

    private fun handleNotificationOnboardingCompleted(
        state: State
    ): ReducerResult =
        if (state is State.Ready && state.profile != null) {
            navigateUserAfterNotificationOnboarding(state.profile)
        } else {
            state to emptySet()
        }

    private fun handleFirstProblemOnboardingDataFetched(
        state: State,
        message: Message.FirstProblemOnboardingDataFetched
    ): ReducerResult =
        if (state is State.Ready && state.profile != null && !state.profile.isNewUser) {
            state to setOf(
                if (!message.wasFirstProblemOnboardingShown) {
                    Action.ViewAction.NavigateTo.FirstProblemOnBoardingScreen(isNewUserMode = false)
                } else {
                    Action.ViewAction.NavigateTo.HomeScreen
                }
            )
        } else {
            state to emptySet()
        }

    private fun handleFirstProblemOnboardingCompleted(
        state: State,
        message: Message.FirstProblemOnboardingCompleted
    ): ReducerResult =
        if (state is State.Ready && state.profile != null) {
            state to setOf(
                when (message) {
                    is Message.FirstProblemOnboardingCompleted.FirstProblemLoaded ->
                        Action.ViewAction.NavigateTo.HomeScreenWithStep(message.firstProblemStepRoute)
                    Message.FirstProblemOnboardingCompleted.FirstProblemEmpty ->
                        Action.ViewAction.NavigateTo.StudyPlan
                }
            )
        } else {
            state to emptySet()
        }

    private fun navigateUserAfterNotificationOnboarding(profile: Profile): ReducerResult =
        State.Ready(
            isAuthorized = true,
            profile = profile
        ) to setOf(
            if (profile.isNewUser) {
                Action.ViewAction.NavigateTo.TrackSelectionScreen
            } else {
                Action.FetchFirstProblemOnboardingData
            }
        )

    private fun getAuthorizedUserNavigationAction(profile: Profile): Action =
        if (profile.isNewUser) {
            Action.ViewAction.NavigateTo.TrackSelectionScreen
        } else {
            Action.ViewAction.NavigateTo.HomeScreen
        }

    private fun reduceStreakRecoveryMessage(
        message: StreakRecoveryFeature.Message
    ): Set<Action> {
        val (_, streakRecoveryActions) = streakRecoveryReducer.reduce(StreakRecoveryFeature.State, message)

        return streakRecoveryActions
            .map {
                if (it is StreakRecoveryFeature.Action.ViewAction) {
                    Action.ViewAction.StreakRecoveryViewAction(it)
                } else {
                    Action.StreakRecoveryAction(it)
                }
            }
            .toSet()
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