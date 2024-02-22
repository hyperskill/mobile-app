package org.hyperskill.app.users_questionnaire.onboarding.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

object QuestionnaireOnboardingFeature {
    internal data class State(
        val selectedChoice: String? = null,
        val textInputValue: String? = null
    )

    data class ViewState(
        val title: String,
        val choices: List<String>,
        val selectedChoice: String?,
        val textInputValue: String?,
        val isTextInputVisible: Boolean,
        val isSendButtonEnabled: Boolean
    )

    sealed interface Message {
        data class ClickedChoice(val choice: String) : Message
        data class TextInputValueChanged(val text: String) : Message

        object SendButtonClicked : Message
        object SkipButtonClicked : Message

        object ViewedEventMessage : Message
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            object CompleteQuestionnaireOnboarding : ViewAction
            data class ShowSendSuccessMessage(val message: String) : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(
            val event: AnalyticEvent,
            val forceLogEvent: Boolean = false
        ) : InternalAction
    }
}