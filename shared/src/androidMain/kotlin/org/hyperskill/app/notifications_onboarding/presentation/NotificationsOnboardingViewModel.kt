package org.hyperskill.app.notifications_onboarding.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Action.ViewAction
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Message
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.State

class NotificationsOnboardingViewModel(
    viewContainer: FlowView<State, Message, ViewAction>
) : ReduxFlowViewModel<State, Message, ViewAction>(viewContainer)