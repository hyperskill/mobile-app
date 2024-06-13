package org.hyperskill.app.welcome_onboarding.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Message
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.State

class WelcomeOnboardingViewModel(
    flowView: FlowView<State, Message, Action>
) : ReduxFlowViewModel<State, Message, Action>(flowView) {
    fun onStartClick() {
        onNewMessage(Message.StartJourneyClicked)
    }
}