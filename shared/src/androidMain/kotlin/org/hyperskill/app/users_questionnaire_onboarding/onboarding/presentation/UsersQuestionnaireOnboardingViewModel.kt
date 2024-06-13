package org.hyperskill.app.users_questionnaire_onboarding.onboarding.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.Action.ViewAction
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.Message
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.ViewState

class UsersQuestionnaireOnboardingViewModel(
    viewContainer: FlowView<ViewState, Message, ViewAction>
) : ReduxFlowViewModel<ViewState, Message, ViewAction>(viewContainer) {
    fun onChoiceClicked(choice: String) {
        onNewMessage(Message.ClickedChoice(choice))
    }

    fun onTextInputChanged(text: String) {
        onNewMessage(Message.TextInputValueChanged(text))
    }

    fun onSendButtonClick() {
        onNewMessage(Message.SendButtonClicked)
    }

    fun onSkipButtonClick() {
        onNewMessage(Message.SkipButtonClicked)
    }
}