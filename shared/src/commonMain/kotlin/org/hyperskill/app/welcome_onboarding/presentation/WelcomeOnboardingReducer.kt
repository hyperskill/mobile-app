package org.hyperskill.app.welcome_onboarding.presentation

import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.model.isNewUser
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action.ViewAction
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.InternalAction
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.InternalMessage
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Message
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.OnboardingFlowFinishReason
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

class WelcomeOnboardingReducer : StateReducer<State, Message, Action> {

    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            is InternalMessage.OnboardingFlowRequested ->
                handleOnboardingFlowRequested(message)

            is InternalMessage.NotificationOnboardingDataFetched ->
                handleNotificationOnboardingDataFetched(state, message)
            Message.NotificationOnboardingCompleted ->
                handleNotificationOnboardingCompleted(state)

            is InternalMessage.FirstProblemOnboardingDataFetched ->
                handleFirstProblemOnboardingDataFetched(state, message)
            is Message.FirstProblemOnboardingCompleted ->
                handleFirstProblemOnboardingCompleted(message)
        }

    private fun handleOnboardingFlowRequested(
        message: InternalMessage.OnboardingFlowRequested
    ): ReducerResult =
        if (!message.isNotificationPermissionGranted) {
            State(message.profile) to
                setOf(InternalAction.FetchNotificationOnboardingData)
        } else {
            onNotificationOnboardingCompleted(message.profile)
        }

    private fun handleNotificationOnboardingDataFetched(
        state: State,
        message: InternalMessage.NotificationOnboardingDataFetched
    ): ReducerResult =
        if (state.profile != null) {
            if (!message.wasNotificationOnboardingShown) {
                state to setOf(ViewAction.NavigateTo.NotificationOnboardingScreen)
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
        message: InternalMessage.FirstProblemOnboardingDataFetched
    ): ReducerResult =
        if (state.profile?.isNewUser == false) {
            state to setOf(
                if (!message.wasFirstProblemOnboardingShown) {
                    ViewAction.NavigateTo.FirstProblemOnboardingScreen(isNewUserMode = false)
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
                InternalAction.FetchFirstProblemOnboardingData
            }
        )
}