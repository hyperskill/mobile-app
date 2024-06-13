package org.hyperskill.app.welcome_onboarding.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingProgrammingLanguage
import org.hyperskill.app.welcome_onboarding.model.WelcomeQuestionnaireType
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action.ViewAction
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Message
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.State
import org.hyperskill.app.welcome_onboarding.model.WelcomeQuestionnaireItemType

class WelcomeOnboardingViewModel(
    flowView: FlowView<State, Message, ViewAction>
) : ReduxFlowViewModel<State, Message, ViewAction>(flowView) {
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
}