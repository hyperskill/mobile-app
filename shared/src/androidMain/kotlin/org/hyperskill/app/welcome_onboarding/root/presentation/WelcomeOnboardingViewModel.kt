package org.hyperskill.app.welcome_onboarding.root.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeOnboardingProgrammingLanguage
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeQuestionnaireItemType
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeQuestionnaireType
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.Action.ViewAction
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.Message
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.State

class WelcomeOnboardingViewModel(
    flowView: FlowView<State, Message, ViewAction>
) : ReduxFlowViewModel<State, Message, ViewAction>(flowView) {

    init {
        onNewMessage(Message.Initialize)
    }

    fun onStartClick() {
        onNewMessage(Message.StartJourneyClicked)
    }

    fun onQuestionnaireItemClicked(
        questionnaireType: WelcomeQuestionnaireType,
        itemType: WelcomeQuestionnaireItemType
    ) {
        onNewMessage(Message.QuestionnaireItemClicked(questionnaireType, itemType))
    }

    fun onLanguageSelected(language: WelcomeOnboardingProgrammingLanguage) {
        onNewMessage(Message.ProgrammingLanguageSelected(language))
    }

    fun onTrackSelected(track: WelcomeOnboardingTrack, isNotificationPermissionGranted: Boolean) {
        onNewMessage(Message.TrackSelected(track, isNotificationPermissionGranted))
    }

    fun onNotificationPermissionCompleted() {
        onNewMessage(Message.NotificationPermissionOnboardingCompleted)
    }

    fun onFinishOnboardingShowed() {
        onNewMessage(Message.FinishOnboardingShowed)
    }
}