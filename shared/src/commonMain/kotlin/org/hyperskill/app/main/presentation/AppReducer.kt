package org.hyperskill.app.main.presentation

import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.main.presentation.AppFeature.Action
import org.hyperskill.app.main.presentation.AppFeature.Message
import org.hyperskill.app.main.presentation.AppFeature.State
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingReducer
import org.hyperskill.app.profile.domain.model.isNewUser
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryReducer
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

class AppReducer(
    private val streakRecoveryReducer: StreakRecoveryReducer,
    private val notificationClickHandlingReducer: NotificationClickHandlingReducer
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
                if (state is State.Ready && !state.isAuthorized) {
                    val navigateToViewAction = if (message.profile.isNewUser) {
                        Action.ViewAction.NavigateTo.TrackSelectionScreen
                    } else {
                        Action.ViewAction.NavigateTo.HomeScreen
                    }

                    State.Ready(isAuthorized = true) to setOf(
                        Action.IdentifyUserInSentry(message.profile.id),
                        navigateToViewAction
                    )
                } else {
                    null
                }
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
            is Message.OpenAuthScreen ->
                state to setOf(Action.ViewAction.NavigateTo.AuthScreen())
            is Message.OpenNewUserScreen ->
                state to setOf(Action.ViewAction.NavigateTo.TrackSelectionScreen)
            is Message.StreakRecoveryMessage ->
                state to reduceStreakRecoveryMessage(message.message)
            is Message.NotificationClicked -> handleNotificationClicked(state, message)
            is Message.NotificationClickHandlingMessage ->
                state to reduceNotificationClickHandlingMessage(message.message)
        } ?: (state to emptySet())

    private fun handleUserAccountStatus(
        state: State,
        message: Message.UserAccountStatus
    ): ReducerResult =
        if (state is State.Loading) {
            val isAuthorized = !message.profile.isGuest

            val sentryAction = if (isAuthorized) {
                Action.IdentifyUserInSentry(message.profile.id)
            } else {
                Action.ClearUserInSentry
            }

            val actions: Set<Action> =
                if (isAuthorized) {
                    when {
                        message.notificationData != null ->
                            reduceNotificationClickHandlingMessage(
                                NotificationClickHandlingFeature.Message.NotificationClicked(
                                    message.notificationData,
                                    isUserAuthorized = true
                                )
                            )
                        message.profile.isNewUser ->
                            setOf(Action.ViewAction.NavigateTo.TrackSelectionScreen)
                        else ->
                            setOf(Action.ViewAction.NavigateTo.HomeScreen)
                    }
                } else {
                    buildSet {
                        if (message.notificationData != null) {
                            addAll(
                                reduceNotificationClickHandlingMessage(
                                    NotificationClickHandlingFeature.Message.NotificationClicked(
                                        message.notificationData,
                                        isUserAuthorized = false
                                    )
                                )
                            )
                        }
                        add(Action.ViewAction.NavigateTo.OnboardingScreen)
                    }
                }

            val streakRecoveryActions = if (isAuthorized && message.notificationData == null) {
                reduceStreakRecoveryMessage(StreakRecoveryFeature.Message.Initialize)
            } else {
                emptySet()
            }

            State.Ready(isAuthorized) to
                actions + sentryAction + streakRecoveryActions
        } else {
            state to emptySet()
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
                    isUserAuthorized = state.isAuthorized
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
}