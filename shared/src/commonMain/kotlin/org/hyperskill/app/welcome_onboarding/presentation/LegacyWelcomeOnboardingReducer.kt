package org.hyperskill.app.welcome_onboarding.presentation

import org.hyperskill.app.profile.domain.model.isNewUser
import org.hyperskill.app.welcome_onboarding.presentation.LegacyWelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.presentation.LegacyWelcomeOnboardingFeature.Action.ViewAction
import org.hyperskill.app.welcome_onboarding.presentation.LegacyWelcomeOnboardingFeature.InternalAction
import org.hyperskill.app.welcome_onboarding.presentation.LegacyWelcomeOnboardingFeature.InternalMessage
import org.hyperskill.app.welcome_onboarding.presentation.LegacyWelcomeOnboardingFeature.Message
import org.hyperskill.app.welcome_onboarding.presentation.LegacyWelcomeOnboardingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

class LegacyWelcomeOnboardingReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            is InternalMessage.OnboardingFlowRequested ->
                handleOnboardingFlowRequested(message)

            Message.NotificationOnboardingCompleted ->
                handleNotificationOnboardingCompleted(state)

            Message.UsersQuestionnaireOnboardingCompleted ->
                handleUsersQuestionnaireOnboardingCompleted(state)

            is InternalMessage.FirstProblemOnboardingDataFetched ->
                handleFirstProblemOnboardingDataFetched(state, message)
            is Message.FirstProblemOnboardingCompleted ->
                handleFirstProblemOnboardingCompleted(state, message)
        }

    private fun handleOnboardingFlowRequested(
        message: InternalMessage.OnboardingFlowRequested
    ): ReducerResult {
        val state = State(message.profile)
        return if (!message.isNotificationPermissionGranted) {
            state to setOf(ViewAction.NavigateTo.NotificationOnboardingScreen)
        } else {
            handleNotificationOnboardingCompleted(state)
        }
    }

    private fun handleNotificationOnboardingCompleted(
        state: State
    ): ReducerResult =
        if (state.profile?.isNewUser == true) {
            state to setOf(ViewAction.NavigateTo.UsersQuestionnaireOnboardingScreen)
        } else {
            handleUsersQuestionnaireOnboardingCompleted(state)
        }

    private fun handleUsersQuestionnaireOnboardingCompleted(
        state: State
    ): ReducerResult =
        if (state.profile?.isNewUser == false) {
            state to setOf(InternalAction.FetchFirstProblemOnboardingData)
        } else {
            completeOnboardingFlow(state)
        }

    private fun handleFirstProblemOnboardingDataFetched(
        state: State,
        message: InternalMessage.FirstProblemOnboardingDataFetched
    ): ReducerResult =
        if (state.profile?.isNewUser == false && !message.wasFirstProblemOnboardingShown) {
            state to setOf(
                ViewAction.NavigateTo.FirstProblemOnboardingScreen(isNewUserMode = false)
            )
        } else {
            completeOnboardingFlow(state)
        }

    private fun handleFirstProblemOnboardingCompleted(
        state: State,
        message: Message.FirstProblemOnboardingCompleted
    ): ReducerResult {
        val (newState, newActions) = completeOnboardingFlow(state)

        val finalActions = if (message.firstProblemStepRoute != null) {
            setOf(ViewAction.NavigateTo.StudyPlanWithStep(message.firstProblemStepRoute))
        } else {
            newActions
        }

        return newState to finalActions
    }

    private fun completeOnboardingFlow(state: State): ReducerResult =
        State(profile = null) to setOf(Action.OnboardingFlowFinished(state.profile))
}