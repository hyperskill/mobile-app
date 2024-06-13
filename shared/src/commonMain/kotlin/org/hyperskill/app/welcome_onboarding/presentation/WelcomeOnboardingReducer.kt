package org.hyperskill.app.welcome_onboarding.presentation

import org.hyperskill.app.welcome_onboarding.model.WelcomeQuestionnaireType
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action.ViewAction.NavigateTo
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Message
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias WelcomeOnboardingReducerResult = Pair<State, Set<Action>>

internal class WelcomeOnboardingReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): WelcomeOnboardingReducerResult =
        when (message) {
            Message.StartJourneyClicked -> handleStartJourneyClicked(state)
            is Message.QuestionnaireItemClicked -> handleQuestionnaireItemClicked(state, message)
        }

    private fun handleStartJourneyClicked(state: State): WelcomeOnboardingReducerResult =
        state to setOf(
            // TODO("Send analytic event")
            NavigateTo.WelcomeOnboardingQuestionnaire(
                WelcomeQuestionnaireType.HOW_DID_YOU_HEAR_ABOUT_HYPERSKILL
            )
        )

    private fun handleQuestionnaireItemClicked(
        state: State,
        message: Message.QuestionnaireItemClicked
    ): WelcomeOnboardingReducerResult =
        state to setOf(
            // TODO("Send analytic event")
            when (message.questionnaireType) {
                WelcomeQuestionnaireType.HOW_DID_YOU_HEAR_ABOUT_HYPERSKILL ->
                    NavigateTo.WelcomeOnboardingQuestionnaire(WelcomeQuestionnaireType.LEARNING_REASON)
                WelcomeQuestionnaireType.LEARNING_REASON ->
                    NavigateTo.WelcomeOnboardingQuestionnaire(WelcomeQuestionnaireType.CODING_EXPERIENCE)
                WelcomeQuestionnaireType.CODING_EXPERIENCE -> TODO()
            }
        )
}