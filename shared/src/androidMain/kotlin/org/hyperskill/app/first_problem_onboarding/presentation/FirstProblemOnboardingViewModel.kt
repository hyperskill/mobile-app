package org.hyperskill.app.first_problem_onboarding.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Action.ViewAction
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Message
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.ViewState

class FirstProblemOnboardingViewModel(
    viewContainer: FlowView<ViewState, Message, ViewAction>
) : ReduxFlowViewModel<ViewState, Message, ViewAction>(viewContainer) {
    init {
        onNewMessage(Message.Initialize)
    }
}