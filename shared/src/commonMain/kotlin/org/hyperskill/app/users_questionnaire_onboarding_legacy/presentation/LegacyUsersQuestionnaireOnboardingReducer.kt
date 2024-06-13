package org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.users_questionnaire_onboarding_legacy.domain.analytic.UsersQuestionnaireOnboardingClickedChoiceHyperskillAnalyticEvent
import org.hyperskill.app.users_questionnaire_onboarding_legacy.domain.analytic.UsersQuestionnaireOnboardingClickedSendHyperskillAnalyticEvent
import org.hyperskill.app.users_questionnaire_onboarding_legacy.domain.analytic.UsersQuestionnaireOnboardingClickedSkipHyperskillAnalyticEvent
import org.hyperskill.app.users_questionnaire_onboarding_legacy.domain.analytic.UsersQuestionnaireOnboardingViewedHyperskillAnalyticEvent
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.Action
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.InternalAction
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.Message
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

@Deprecated("Should be removed in ALTAPPS-1276")
internal class LegacyUsersQuestionnaireOnboardingReducer(
    private val resourceProvider: ResourceProvider
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            is Message.ClickedChoice ->
                state.copy(selectedChoice = message.choice) to setOf(
                    InternalAction.LogAnalyticEvent(
                        UsersQuestionnaireOnboardingClickedChoiceHyperskillAnalyticEvent(message.choice)
                    )
                )
            is Message.TextInputValueChanged ->
                handleTextInputValueChangedMessage(state, message)
            Message.SendButtonClicked ->
                handleSendButtonClickedMessage(state)
            Message.SkipButtonClicked ->
                handleSkipButtonClickedMessage(state)
            Message.ViewedEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(UsersQuestionnaireOnboardingViewedHyperskillAnalyticEvent)
                )
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
                    UsersQuestionnaireOnboardingClickedSendHyperskillAnalyticEvent(
                        selectedChoice = state.selectedChoice,
                        textInputValue = state.textInputValue
                    ),
                    forceLogEvent = true
                ),
                Action.ViewAction.ShowSendSuccessMessage(
                    resourceProvider.getString(
                        SharedResources.strings.users_questionnaire_onboarding_send_answer_success_message
                    )
                ),
                Action.ViewAction.CompleteUsersQuestionnaireOnboarding
            )
        } else {
            null
        }

    private fun handleSkipButtonClickedMessage(state: State): ReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(UsersQuestionnaireOnboardingClickedSkipHyperskillAnalyticEvent),
            Action.ViewAction.CompleteUsersQuestionnaireOnboarding
        )
}