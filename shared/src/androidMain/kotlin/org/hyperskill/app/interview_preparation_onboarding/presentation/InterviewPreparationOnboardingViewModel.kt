package org.hyperskill.app.interview_preparation_onboarding.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.Action.ViewAction
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.Message
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.State

class InterviewPreparationOnboardingViewModel(
    viewContainer: FlowView<State, Message, ViewAction>
) : ReduxFlowViewModel<State, Message, ViewAction>(viewContainer)