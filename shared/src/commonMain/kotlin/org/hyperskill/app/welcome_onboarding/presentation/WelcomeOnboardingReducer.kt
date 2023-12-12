package org.hyperskill.app.welcome_onboarding.presentation

import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.model.isNewUser
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Message
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.OnboardingFlowFinishReason
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.State
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.ViewAction
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

class WelcomeOnboardingReducer : StateReducer<State, Message, Action> {

    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            is Message.OnboardingFlowRequested ->
                handleOnboardingFlowRequested(message)

            is Message.NotificationOnboardingDataFetched ->
                handleNotificationOnboardingDataFetched(state, message)
            Message.NotificationOnboardingCompleted ->
                handleNotificationOnboardingCompleted(state)

            is Message.FirstProblemOnboardingDataFetched ->
                handleFirstProblemOnboardingDataFetched(state, message)
            is Message.FirstProblemOnboardingCompleted ->
                handleFirstProblemOnboardingCompleted(message)
        }

    private fun handleOnboardingFlowRequested(
        message: Message.OnboardingFlowRequested
    ): ReducerResult =
        if (!message.isNotificationPermissionGranted) {
            State(message.profile) to
                setOf(Action.FetchNotificationOnboardingData)
        } else {
            onNotificationOnboardingCompleted(message.profile)
        }
    private fun handleNotificationOnboardingDataFetched(
        state: State,
        message: Message.NotificationOnboardingDataFetched
    ): ReducerResult =
        if (state.profile != null) {
            if (!message.wasNotificationOnBoardingShown) {
                state to setOf(ViewAction.NavigateTo.NotificationOnBoardingScreen)
            } else {
                onNotificationOnboardingCompleted(state.profile)
            }
        } else {
            state to setOf(
                Action.OnboardingFlowFinished(
                    OnboardingFlowFinishReason.NotificationOnboardingFinished(state.profile)
                )
            )
        }

    private fun handleNotificationOnboardingCompleted(
        state: State
    ): ReducerResult =
        if (state.profile != null) {
            onNotificationOnboardingCompleted(state.profile)
        } else {
            state to setOf(
                Action.OnboardingFlowFinished(
                    OnboardingFlowFinishReason.NotificationOnboardingFinished(state.profile)
                )
            )
        }

    private fun handleFirstProblemOnboardingDataFetched(
        state: State,
        message: Message.FirstProblemOnboardingDataFetched
    ): ReducerResult =
        if (state.profile?.isNewUser == false) {
            state to setOf(
                if (!message.wasFirstProblemOnboardingShown) {
                    ViewAction.NavigateTo.FirstProblemOnBoardingScreen(isNewUserMode = false)
                } else {
                    Action.OnboardingFlowFinished(
                        OnboardingFlowFinishReason.FirstProblemOnboardingFinished
                    )
                }
            )
        } else {
            state to setOf(
                Action.OnboardingFlowFinished(
                    OnboardingFlowFinishReason.FirstProblemOnboardingFinished
                )
            )
        }

    private fun handleFirstProblemOnboardingCompleted(
        message: Message.FirstProblemOnboardingCompleted
    ): ReducerResult =
        State(profile = null) to setOf(
            message.firstProblemStepRoute?.let { stepRoute ->
                ViewAction.NavigateTo.StudyPlanWithStep(stepRoute)
            } ?: Action.OnboardingFlowFinished(
                OnboardingFlowFinishReason.FirstProblemOnboardingFinished
            )
        )

    private fun onNotificationOnboardingCompleted(
        profile: Profile
    ): ReducerResult =
        State(profile = profile.takeIf { !it.isNewUser }) to setOf(
            if (profile.isNewUser) {
                Action.OnboardingFlowFinished(
                    OnboardingFlowFinishReason.NotificationOnboardingFinished(profile)
                )
            } else {
                Action.FetchFirstProblemOnboardingData
            }
        )
}