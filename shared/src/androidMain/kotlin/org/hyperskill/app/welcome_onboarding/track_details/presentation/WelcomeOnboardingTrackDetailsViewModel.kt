package org.hyperskill.app.welcome_onboarding.track_details.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.Action.ViewAction
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.Message
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.ViewState

class WelcomeOnboardingTrackDetailsViewModel(
    flowView: FlowView<ViewState, Message, ViewAction>
) : ReduxFlowViewModel<ViewState, Message, ViewAction>(flowView) {
    fun onContinueClick() {
        onNewMessage(Message.ContinueClicked)
    }
}