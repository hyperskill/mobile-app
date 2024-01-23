package org.hyperskill.app.welcome_onboarding.presentation

import org.hyperskill.app.profile.domain.model.isNewUser
import org.hyperskill.app.subscriptions.domain.model.isFreemium
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
            is Message.OnboardingFlowRequested ->
                handleOnboardingFlowRequested(message)

            is InternalMessage.NotificationOnboardingDataFetched ->
                handleNotificationOnboardingDataFetched(state, message)
            Message.NotificationOnboardingCompleted ->
                handleNotificationOnboardingCompleted(state)

            is InternalMessage.FetchSubscriptionSuccess ->
                handleFetchSubscriptionSuccess(state, message)
            is InternalMessage.FetchSubscriptionError ->
                handleFetchSubscriptionError(state)
            is Message.PaywallCompleted ->
                handlePaywallCompleted(state)

            is InternalMessage.FirstProblemOnboardingDataFetched ->
                handleFirstProblemOnboardingDataFetched(state, message)
            is Message.FirstProblemOnboardingCompleted ->
                handleFirstProblemOnboardingCompleted(message)
        }

    private fun handleOnboardingFlowRequested(
        message: Message.OnboardingFlowRequested
    ): ReducerResult {
        val state = State(message.profile)
        return if (!message.isNotificationPermissionGranted) {
            state to setOf(InternalAction.FetchNotificationOnboardingData)
        } else {
            onNotificationOnboardingCompleted(state)
        }
    }

    private fun handleNotificationOnboardingDataFetched(
        state: State,
        message: InternalMessage.NotificationOnboardingDataFetched
    ): ReducerResult =
        if (!message.wasNotificationOnboardingShown) {
            state to setOf(ViewAction.NavigateTo.NotificationOnboardingScreen)
        } else {
            onNotificationOnboardingCompleted(state)
        }

    private fun handleNotificationOnboardingCompleted(
        state: State
    ): ReducerResult =
        onNotificationOnboardingCompleted(state)

    private fun onNotificationOnboardingCompleted(state: State): ReducerResult =
        state to setOf(InternalAction.FetchSubscription)

    private fun handleFetchSubscriptionSuccess(
        state: State,
        message: InternalMessage.FetchSubscriptionSuccess
    ): ReducerResult =
        if (message.subscription.isFreemium) {
            state to setOf(ViewAction.NavigateTo.Paywall)
        } else {
            onPaywallCompleted(state)
        }

    private fun handleFetchSubscriptionError(state: State): ReducerResult =
        onPaywallCompleted(state)

    private fun handlePaywallCompleted(state: State): ReducerResult =
        onPaywallCompleted(state)

    private fun onPaywallCompleted(state: State): ReducerResult =
        if (state.profile?.isNewUser == false) {
            state to setOf(InternalAction.FetchFirstProblemOnboardingData)
        } else {
            State(profile = null) to
                setOf(
                    Action.OnboardingFlowFinished(
                        OnboardingFlowFinishReason.PaywallCompleted
                    )
                )
        }

    private fun handleFirstProblemOnboardingDataFetched(
        state: State,
        message: InternalMessage.FirstProblemOnboardingDataFetched
    ): ReducerResult =
        state to setOf(
            if (state.profile?.isNewUser == false && !message.wasFirstProblemOnboardingShown) {
                ViewAction.NavigateTo.FirstProblemOnboardingScreen(isNewUserMode = false)
            } else {
                Action.OnboardingFlowFinished(
                    OnboardingFlowFinishReason.FirstProblemOnboardingFinished
                )
            }
        )

    private fun handleFirstProblemOnboardingCompleted(
        message: Message.FirstProblemOnboardingCompleted
    ): ReducerResult =
        State(profile = null) to setOf(
            message.firstProblemStepRoute
                ?.let { stepRoute -> ViewAction.NavigateTo.StudyPlanWithStep(stepRoute) }
                ?: Action.OnboardingFlowFinished(
                    OnboardingFlowFinishReason.FirstProblemOnboardingFinished
                )
        )
}