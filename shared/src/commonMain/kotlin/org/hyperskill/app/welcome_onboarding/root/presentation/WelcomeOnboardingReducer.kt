package org.hyperskill.app.welcome_onboarding.root.presentation

import kotlin.time.Duration.Companion.milliseconds
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityTargetType
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeOnboardingProgrammingLanguage
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeOnboardingStartScreen
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeQuestionnaireType
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.Action.ViewAction.NavigateTo
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.InternalAction
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.InternalMessage
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.Message
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.NextLearningActivityState
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias WelcomeOnboardingReducerResult = Pair<State, Set<Action>>

internal class WelcomeOnboardingReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): WelcomeOnboardingReducerResult =
        when (message) {
            Message.Initialize -> handleInitialize(state)
            Message.StartJourneyClicked -> handleStartJourneyClicked(state)
            is Message.QuestionnaireItemClicked -> handleQuestionnaireItemClicked(state, message)
            is Message.ProgrammingLanguageSelected -> handleProgrammingLanguageSelected(state, message)
            is Message.TrackSelected -> navigateToNotificationOnboardingActions(state)
            Message.NotificationPermissionOnboardingCompleted -> handleNotificationPermissionOnboardingCompleted(state)
            Message.FinishOnboardingShowed -> handleFinishOnboardingShowed(state)
            InternalMessage.FinishOnboardingTimerFired -> handleFinishOnboardingTimerFired(state)
            is InternalMessage.FetchNextLearningActivitySuccess ->
                handleFetchNextLearningActivitySuccess(state, message)
            InternalMessage.FetchNextLearningActivityError ->
                handleFetchNextLearningActivityError(state)
        }

    private fun handleInitialize(state: State): WelcomeOnboardingReducerResult =
        when (state.initialStep) {
            WelcomeOnboardingStartScreen.START_SCREEN -> state to setOf(NavigateTo.StartScreen)
            WelcomeOnboardingStartScreen.NOTIFICATION_ONBOARDING -> navigateToNotificationOnboardingActions(state)
        }

    private fun handleStartJourneyClicked(state: State): WelcomeOnboardingReducerResult =
        state to setOf(
            // TODO: Send analytic event
            NavigateTo.WelcomeOnboardingQuestionnaire(
                WelcomeQuestionnaireType.HOW_DID_YOU_HEAR_ABOUT_HYPERSKILL
            )
        )

    private fun handleQuestionnaireItemClicked(
        state: State,
        message: Message.QuestionnaireItemClicked
    ): WelcomeOnboardingReducerResult =
        state to setOf(
            // TODO: Send analytic event
            when (message.questionnaireType) {
                WelcomeQuestionnaireType.HOW_DID_YOU_HEAR_ABOUT_HYPERSKILL ->
                    NavigateTo.WelcomeOnboardingQuestionnaire(WelcomeQuestionnaireType.LEARNING_REASON)
                WelcomeQuestionnaireType.LEARNING_REASON ->
                    NavigateTo.WelcomeOnboardingQuestionnaire(WelcomeQuestionnaireType.CODING_EXPERIENCE)
                WelcomeQuestionnaireType.CODING_EXPERIENCE ->
                    NavigateTo.ChooseProgrammingLanguage
            }
        )

    private fun handleProgrammingLanguageSelected(
        state: State,
        message: Message.ProgrammingLanguageSelected
    ): WelcomeOnboardingReducerResult =
        // TODO: Send analytic event
        state to setOf(
            NavigateTo.TrackDetails(
                when (message.language) {
                    WelcomeOnboardingProgrammingLanguage.JAVA -> WelcomeOnboardingTrack.JAVA
                    WelcomeOnboardingProgrammingLanguage.JAVA_SCRIPT -> WelcomeOnboardingTrack.JAVA_SCRIPT
                    WelcomeOnboardingProgrammingLanguage.KOTLIN -> WelcomeOnboardingTrack.KOTLIN
                    WelcomeOnboardingProgrammingLanguage.SQL -> WelcomeOnboardingTrack.SQL
                    WelcomeOnboardingProgrammingLanguage.PYTHON,
                    WelcomeOnboardingProgrammingLanguage.UNDEFINED -> WelcomeOnboardingTrack.PYTHON
                }
            )
        )

    private fun navigateToNotificationOnboardingActions(state: State): WelcomeOnboardingReducerResult =
        state.copy(nextLearningActivityState = NextLearningActivityState.Loading) to setOf(
            NavigateTo.NotificationOnboarding,
            InternalAction.FetchNextLearningActivity
        )

    private fun handleNotificationPermissionOnboardingCompleted(state: State): WelcomeOnboardingReducerResult =
        state to setOf(NavigateTo.OnboardingFinish)

    private fun handleFinishOnboardingShowed(state: State): WelcomeOnboardingReducerResult =
        state to setOf(InternalAction.LaunchFinishOnboardingTimer(600.milliseconds))

    private fun handleFinishOnboardingTimerFired(state: State): WelcomeOnboardingReducerResult =
        when (state.nextLearningActivityState) {
            NextLearningActivityState.Idle -> state to setOf(InternalAction.FetchNextLearningActivity)
            NextLearningActivityState.Loading -> {
                // Wait for a result
                state.copy(isNextLearningActivityLoadingShowed = true) to emptySet()
            }
            NextLearningActivityState.Error -> completeWelcomeOnboarding(state, null)
            is NextLearningActivityState.Success ->
                completeWelcomeOnboarding(state, state.nextLearningActivityState.nextLearningActivity)
        }

    private fun handleFetchNextLearningActivitySuccess(
        state: State,
        message: InternalMessage.FetchNextLearningActivitySuccess
    ): WelcomeOnboardingReducerResult =
        if (state.isNextLearningActivityLoadingShowed) {
            completeWelcomeOnboarding(state, message.nextLearningActivity)
        } else {
            state.copy(
                nextLearningActivityState = NextLearningActivityState.Success(message.nextLearningActivity)
            ) to emptySet()
        }

    private fun handleFetchNextLearningActivityError(state: State): WelcomeOnboardingReducerResult =
        if (state.isNextLearningActivityLoadingShowed) {
            completeWelcomeOnboarding(state, nextLearningActivity = null)
        } else {
            state.copy(nextLearningActivityState = NextLearningActivityState.Error) to emptySet()
        }

    private fun completeWelcomeOnboarding(state: State, nextLearningActivity: LearningActivity?): WelcomeOnboardingReducerResult {
        val stepRoute = if (nextLearningActivity?.targetType == LearningActivityTargetType.STEP) {
            nextLearningActivity.targetId?.let { stepId ->
                StepRoute.Learn.Step(stepId = stepId, topicId = nextLearningActivity.topicId)
            }
        } else {
            null
        }
        return state.copy(isNextLearningActivityLoadingShowed = false) to
            setOf(Action.ViewAction.CompleteWelcomeOnboarding(stepRoute))
    }
}