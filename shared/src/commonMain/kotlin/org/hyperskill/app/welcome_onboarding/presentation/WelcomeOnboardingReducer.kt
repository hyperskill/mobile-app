package org.hyperskill.app.welcome_onboarding.presentation

import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.profile.domain.model.isMobileOnlySubscriptionEnabled
import org.hyperskill.app.profile.domain.model.isNewUser
import org.hyperskill.app.subscriptions.domain.model.isFreemium
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action.ViewAction
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.InternalAction
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.InternalMessage
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Message
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

class WelcomeOnboardingReducer(
    private val isSubscriptionPurchaseEnabled: Boolean
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            is InternalMessage.OnboardingFlowRequested ->
                handleOnboardingFlowRequested(message)

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

    private fun handleNotificationOnboardingCompleted(state: State): ReducerResult =
        if (isSubscriptionPurchaseEnabled && state.profile?.features?.isMobileOnlySubscriptionEnabled == true) {
            state to setOf(InternalAction.FetchSubscription)
        } else {
            handlePaywallCompleted(state)
        }

    private fun handleFetchSubscriptionSuccess(
        state: State,
        message: InternalMessage.FetchSubscriptionSuccess
    ): ReducerResult =
        if (message.subscription.isFreemium) {
            state to setOf(ViewAction.NavigateTo.Paywall(PaywallTransitionSource.LOGIN))
        } else {
            handlePaywallCompleted(state)
        }

    private fun handleFetchSubscriptionError(state: State): ReducerResult =
        handlePaywallCompleted(state)

    private fun handlePaywallCompleted(state: State): ReducerResult =
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
        val (newState, actions) = completeOnboardingFlow(state)
        return newState to actions + setOfNotNull(
            message
                .firstProblemStepRoute
                ?.let(ViewAction.NavigateTo::StudyPlanWithStep)
        )
    }

    private fun completeOnboardingFlow(state: State): ReducerResult =
        State(profile = null) to setOf(Action.OnboardingFlowFinished(state.profile))
}