package org.hyperskill.app.first_problem_onboarding.presentation

import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Action
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.InternalAction
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Message
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.NextLearningActivityState
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.ProfileState
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.State
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias FirstProblemOnboardingReducerResult = Pair<State, Set<Action>>

internal class FirstProblemOnboardingReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            Message.Initialize ->
                handleInitialize(state)
            Message.RetryContentLoading ->
                handleRetryContentLoading(state)
            is FirstProblemOnboardingFeature.FetchProfileResult ->
                handleFetchProfileResult(state, message)
            is FirstProblemOnboardingFeature.FetchNextLearningActivityResult ->
                handleNextLearningActivityResult(state, message)
            Message.LearningActionButtonClicked ->
                handleLearningActionButtonClicked(state)
            Message.ViewedEventMessage ->
                TODO()
        } ?: (state to emptySet())

    private fun handleInitialize(state: State): FirstProblemOnboardingReducerResult? =
        if (state.profileState is ProfileState.Idle &&
            state.nextLearningActivityState is NextLearningActivityState.Idle
        ) {
            state
                .updateProfileState(ProfileState.Loading)
                .updateNextLearningActivityState(NextLearningActivityState.Loading) to
                setOf(
                    InternalAction.FetchProfile,
                    InternalAction.FetchNextLearningActivity
                )
        } else {
            null
        }

    private fun handleRetryContentLoading(state: State): FirstProblemOnboardingReducerResult? =
        if (state.profileState is ProfileState.Error) {
            state.updateProfileState(ProfileState.Loading) to setOf(InternalAction.FetchProfile)
        } else {
            null
        }

    private fun handleFetchProfileResult(
        state: State,
        message: FirstProblemOnboardingFeature.FetchProfileResult
    ): FirstProblemOnboardingReducerResult? =
        when (message) {
            FirstProblemOnboardingFeature.FetchProfileResult.Error ->
                state.updateProfileState(ProfileState.Error) to emptySet()
            is FirstProblemOnboardingFeature.FetchProfileResult.Success ->
                if (state.profileState is ProfileState.Loading) {
                    state.updateProfileState(ProfileState.Content(message.profile)) to emptySet()
                } else {
                    null
                }
        }

    private fun handleNextLearningActivityResult(
        state: State,
        message: FirstProblemOnboardingFeature.FetchNextLearningActivityResult
    ): FirstProblemOnboardingReducerResult =
        when (message) {
            FirstProblemOnboardingFeature.FetchNextLearningActivityResult.Error ->
                state
                    .copy(isLearningActivityLoading = false)
                    .updateNextLearningActivityState(NextLearningActivityState.Error) to
                    buildSet {
                        if (state.isLearningActivityLoading) {
                            add(Action.ViewAction.ShowNetworkError)
                        }
                    }
            is FirstProblemOnboardingFeature.FetchNextLearningActivityResult.Success ->
                if (state.isLearningActivityLoading) {
                    state.copy(isLearningActivityLoading = false) to setOf(
                        getNavigateActionByLearningActivity(message.nextLearningActivity)
                    )
                } else {
                    state.updateNextLearningActivityState(
                        NextLearningActivityState.Content(message.nextLearningActivity)
                    ) to emptySet()
                }
        }

    private fun handleLearningActionButtonClicked(state: State): FirstProblemOnboardingReducerResult? =
        if (state.profileState is ProfileState.Content) {
            when (state.nextLearningActivityState) {
                NextLearningActivityState.Idle ->
                    null
                NextLearningActivityState.Error ->
                    state.copy(isLearningActivityLoading = true) to setOf(InternalAction.FetchNextLearningActivity)
                NextLearningActivityState.Loading ->
                    state.copy(isLearningActivityLoading = true) to emptySet()
                is NextLearningActivityState.Content ->
                    state to setOf(
                        getNavigateActionByLearningActivity(state.nextLearningActivityState.nextLearningActivity)
                    )
            }
        } else {
            null
        }

    private fun getNavigateActionByLearningActivity(learningActivity: LearningActivity?) =
        learningActivity?.targetId?.let { stepId ->
            Action.ViewAction.NavigateTo.StepScreen(StepRoute.Learn.Step(stepId))
        } ?: Action.ViewAction.NavigateTo.StudyPlanScreen

    private fun State.updateProfileState(profileState: ProfileState): State =
        copy(profileState = profileState)

    private fun State.updateNextLearningActivityState(learningActivityState: NextLearningActivityState): State =
        copy(nextLearningActivityState = learningActivityState)
}