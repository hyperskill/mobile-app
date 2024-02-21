package org.hyperskill.app.questionnaire_onboarding.presentation

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.questionnaire_onboarding.domain.analytic.QuestionnaireOnboardingClickedChoiceHyperskillAnalyticEvent
import org.hyperskill.app.questionnaire_onboarding.domain.analytic.QuestionnaireOnboardingClickedSendHyperskillAnalyticEvent
import org.hyperskill.app.questionnaire_onboarding.domain.analytic.QuestionnaireOnboardingClickedSkipHyperskillAnalyticEvent
import org.hyperskill.app.questionnaire_onboarding.domain.analytic.QuestionnaireOnboardingViewedHyperskillAnalyticEvent
import org.hyperskill.app.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.Action
import org.hyperskill.app.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.InternalAction
import org.hyperskill.app.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.Message
import org.hyperskill.app.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

internal class QuestionnaireOnboardingReducer(
    private val resourceProvider: ResourceProvider
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            is Message.ClickedChoice ->
                state.copy(selectedChoice = message.choice) to setOf(
                    InternalAction.LogAnalyticEvent(
                        QuestionnaireOnboardingClickedChoiceHyperskillAnalyticEvent(message.choice)
                    )
                )
            is Message.TextInputValueChanged ->
                handleTextInputValueChangedMessage(state, message)
            Message.SendButtonClicked ->
                handleSendButtonClickedMessage(state)
            Message.SkipButtonClicked ->
                handleSkipButtonClickedMessage(state)
            Message.ViewedEventMessage ->
                state to setOf(InternalAction.LogAnalyticEvent(QuestionnaireOnboardingViewedHyperskillAnalyticEvent))
        } ?: (state to emptySet())

    private fun handleTextInputValueChangedMessage(
        state: State,
        message: Message.TextInputValueChanged
    ): ReducerResult? =
        if (state.selectedChoice != null) {
            state.copy(textInputValue = message.text) to emptySet()
        } else {
            null
        }

    private fun handleSendButtonClickedMessage(state: State): ReducerResult? =
        if (state.selectedChoice != null) {
            state to setOf(
                InternalAction.LogAnalyticEvent(
                    QuestionnaireOnboardingClickedSendHyperskillAnalyticEvent(
                        selectedChoice = state.selectedChoice,
                        textInputValue = state.textInputValue
                    ),
                    forceLogEvent = true
                ),
                Action.ViewAction.ShowSendSuccessMessage(
                    resourceProvider.getString(
                        SharedResources.strings.questionnaire_onboarding_send_answer_success_message
                    )
                ),
                Action.ViewAction.CompleteQuestionnaireOnboarding
            )
        } else {
            null
        }

    private fun handleSkipButtonClickedMessage(state: State): ReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(QuestionnaireOnboardingClickedSkipHyperskillAnalyticEvent),
            Action.ViewAction.CompleteQuestionnaireOnboarding
        )
}