package org.hyperskill.app.welcome_onboarding.root.presentation

import org.hyperskill.app.first_problem_onboarding.domain.analytic.OnboardingCompletionAppsFlyerAnalyticEvent
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityTargetType
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.root.domain.analytic.WelcomeOnboardingFinishScreenStartClickedHSAnalyticEvent
import org.hyperskill.app.welcome_onboarding.root.domain.analytic.WelcomeOnboardingFinishScreenViewedHSAnalyticEvent
import org.hyperskill.app.welcome_onboarding.root.domain.analytic.WelcomeOnboardingProgrammingLanguageClickedHSAnalyticEvent
import org.hyperskill.app.welcome_onboarding.root.domain.analytic.WelcomeOnboardingQuestionnaireItemClickedHSAnalyticEvent
import org.hyperskill.app.welcome_onboarding.root.domain.analytic.WelcomeOnboardingSelectProgrammingLanguageViewedHSAnalyticEvent
import org.hyperskill.app.welcome_onboarding.root.domain.analytic.WelcomeOnboardingStartJourneyClickedHSAnalyticEvent
import org.hyperskill.app.welcome_onboarding.root.domain.analytic.WelcomeOnboardingStartScreenViewedHSAnalyticEvent
import org.hyperskill.app.welcome_onboarding.root.domain.analytic.WelcomeOnboardingUserQuestionnaireViewedHSAnalyticEvent
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeOnboardingProgrammingLanguage
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeOnboardingStartScreen
import org.hyperskill.app.welcome_onboarding.questionnaire.model.WelcomeQuestionnaireType
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
            is Message.TrackSelected -> handleTrackSelected(state, message)
            Message.NotificationPermissionOnboardingCompleted -> handleNotificationPermissionOnboardingCompleted(state)
            Message.FinishClicked -> handleFinishClicked(state)
            is InternalMessage.FetchNextLearningActivitySuccess ->
                handleFetchNextLearningActivitySuccess(state, message)
            InternalMessage.FetchNextLearningActivityError ->
                handleFetchNextLearningActivityError(state)

            Message.StartOnboardingViewed -> handleStartOnboardingViewed(state)
            is Message.UserQuestionnaireViewed -> handleUserQuestionnaireViewed(state, message)
            Message.SelectProgrammingLanguageViewed -> handleSelectProgrammingLanguageViewed(state)
            Message.FinishOnboardingViewed -> handleFinishOnboardingViewed(state)
        }

    private fun handleInitialize(state: State): WelcomeOnboardingReducerResult =
        when (state.initialStep) {
            WelcomeOnboardingStartScreen.START_SCREEN -> state to setOf(NavigateTo.StartScreen)
            WelcomeOnboardingStartScreen.NOTIFICATION_ONBOARDING ->
                state.copy(nextLearningActivityState = NextLearningActivityState.Loading) to setOf(
                    NavigateTo.NotificationOnboarding,
                    InternalAction.FetchNextLearningActivity
                )
        }

    private fun handleStartJourneyClicked(state: State): WelcomeOnboardingReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(WelcomeOnboardingStartJourneyClickedHSAnalyticEvent),
            NavigateTo.WelcomeOnboardingQuestionnaire(
                WelcomeQuestionnaireType.HOW_DID_YOU_HEAR_ABOUT_HYPERSKILL
            )
        )

    private fun handleQuestionnaireItemClicked(
        state: State,
        message: Message.QuestionnaireItemClicked
    ): WelcomeOnboardingReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                WelcomeOnboardingQuestionnaireItemClickedHSAnalyticEvent(
                    message.questionnaireType,
                    message.itemType
                )
            ),
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
        state to setOf(
            InternalAction.LogAnalyticEvent(
                WelcomeOnboardingProgrammingLanguageClickedHSAnalyticEvent(message.language)
            ),
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

    private fun handleTrackSelected(
        state: State,
        message: Message.TrackSelected
    ): WelcomeOnboardingReducerResult =
        if (!message.isNotificationPermissionGranted) {
            state.copy(
                nextLearningActivityState = NextLearningActivityState.Loading,
                selectedTrack = message.selectedTrack
            ) to setOf(
                NavigateTo.NotificationOnboarding,
                InternalAction.FetchNextLearningActivity
            )
        } else {
            state.copy(
                nextLearningActivityState = NextLearningActivityState.Loading,
                isNextLearningActivityLoadingShown = true,
                selectedTrack = message.selectedTrack
            ) to setOf(InternalAction.FetchNextLearningActivity)
        }

    private fun handleNotificationPermissionOnboardingCompleted(state: State): WelcomeOnboardingReducerResult =
        if (state.selectedTrack != null) {
            state to setOf(NavigateTo.OnboardingFinish(state.selectedTrack))
        } else {
            finishOnboarding(state)
        }

    private fun handleStartOnboardingViewed(state: State): WelcomeOnboardingReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(WelcomeOnboardingStartScreenViewedHSAnalyticEvent)
        )

    private fun handleUserQuestionnaireViewed(
        state: State,
        message: Message.UserQuestionnaireViewed
    ): WelcomeOnboardingReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                WelcomeOnboardingUserQuestionnaireViewedHSAnalyticEvent(message.questionnaireType)
            )
        )

    private fun handleSelectProgrammingLanguageViewed(state: State): WelcomeOnboardingReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(WelcomeOnboardingSelectProgrammingLanguageViewedHSAnalyticEvent)
        )

    private fun handleFinishOnboardingViewed(state: State): WelcomeOnboardingReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(WelcomeOnboardingFinishScreenViewedHSAnalyticEvent)
        )

    private fun handleFinishClicked(state: State): WelcomeOnboardingReducerResult {
        val (newState, actions) = finishOnboarding(state)
        return newState to setOf(
            InternalAction.LogAnalyticEvent(OnboardingCompletionAppsFlyerAnalyticEvent),
            InternalAction.LogAnalyticEvent(WelcomeOnboardingFinishScreenStartClickedHSAnalyticEvent)
        ) + actions
    }

    private fun finishOnboarding(state: State): WelcomeOnboardingReducerResult =
        when (state.nextLearningActivityState) {
            NextLearningActivityState.Idle -> state to setOf(InternalAction.FetchNextLearningActivity)
            NextLearningActivityState.Loading -> {
                // Wait for a next learning activity
                state.copy(isNextLearningActivityLoadingShown = true) to emptySet()
            }
            NextLearningActivityState.Error -> completeWelcomeOnboarding(state, null)
            is NextLearningActivityState.Success ->
                completeWelcomeOnboarding(state, state.nextLearningActivityState.nextLearningActivity)
        }

    private fun handleFetchNextLearningActivitySuccess(
        state: State,
        message: InternalMessage.FetchNextLearningActivitySuccess
    ): WelcomeOnboardingReducerResult =
        if (state.isNextLearningActivityLoadingShown) {
            completeWelcomeOnboarding(state, message.nextLearningActivity)
        } else {
            state.copy(
                nextLearningActivityState = NextLearningActivityState.Success(message.nextLearningActivity)
            ) to emptySet()
        }

    private fun handleFetchNextLearningActivityError(state: State): WelcomeOnboardingReducerResult =
        if (state.isNextLearningActivityLoadingShown) {
            completeWelcomeOnboarding(state, nextLearningActivity = null)
        } else {
            state.copy(nextLearningActivityState = NextLearningActivityState.Error) to emptySet()
        }

    private fun completeWelcomeOnboarding(
        state: State,
        nextLearningActivity: LearningActivity?
    ): WelcomeOnboardingReducerResult {
        val stepRoute = if (nextLearningActivity?.targetType == LearningActivityTargetType.STEP) {
            nextLearningActivity.targetId?.let { stepId ->
                StepRoute.Learn.Step(stepId = stepId, topicId = nextLearningActivity.topicId)
            }
        } else {
            null
        }
        return state.copy(isNextLearningActivityLoadingShown = false) to
            setOf(Action.ViewAction.CompleteWelcomeOnboarding(stepRoute))
    }
}