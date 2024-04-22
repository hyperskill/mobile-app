package org.hyperskill.app.first_problem_onboarding.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.first_problem_onboarding.domain.analytic.FirstProblemOnboardingClickedLearningActionHyperskillAnalyticEvent
import org.hyperskill.app.first_problem_onboarding.domain.analytic.FirstProblemOnboardingViewedHyperskillAnalyticEvent
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Action
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.InternalAction
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Message
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.NextLearningActivityState
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.ProfileState
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.State
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityTargetType
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
            Message.CallToActionButtonClicked ->
                handleLearningActionButtonClicked(state)
            Message.ViewedEventMessage ->
                handleViewedEvent(state)
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

    private fun handleRetryContentLoading(state: State): FirstProblemOnboardingReducerResult =
        state.apply {
            if (state.profileState is ProfileState.Error) {
                updateProfileState(ProfileState.Loading)
            }
            if (state.nextLearningActivityState is NextLearningActivityState.Error) {
                updateNextLearningActivityState(NextLearningActivityState.Loading)
            }
        } to buildSet {
            if (state.profileState is ProfileState.Error) {
                add(InternalAction.FetchProfile)
            }
            if (state.nextLearningActivityState is NextLearningActivityState.Error) {
                add(InternalAction.FetchNextLearningActivity)
            }
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
            FirstProblemOnboardingFeature.FetchNextLearningActivityResult.Error -> {
                val actions = if (state.isLearningActivityLoading) {
                    setOf(Action.ViewAction.ShowNetworkError)
                } else {
                    emptySet()
                }

                state
                    .copy(isLearningActivityLoading = false)
                    .updateNextLearningActivityState(NextLearningActivityState.Error) to actions
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
            val actions = when (state.nextLearningActivityState) {
                NextLearningActivityState.Idle, NextLearningActivityState.Loading ->
                    emptySet()
                NextLearningActivityState.Error ->
                    setOf(InternalAction.FetchNextLearningActivity)
                is NextLearningActivityState.Content ->
                    setOf(
                        getNavigateActionByLearningActivity(state.nextLearningActivityState.nextLearningActivity),
                    )
            }

            state.copy(
                isLearningActivityLoading = state.nextLearningActivityState is NextLearningActivityState.Error ||
                    state.nextLearningActivityState is NextLearningActivityState.Loading
            ) to actions + setOf(
                InternalAction.LogAnalyticEvent(
                    FirstProblemOnboardingClickedLearningActionHyperskillAnalyticEvent(
                        if (state.isNewUserMode) {
                            HyperskillAnalyticTarget.START_LEARNING
                        } else {
                            HyperskillAnalyticTarget.KEEP_LEARNING
                        }
                    )
                )
            )
        } else {
            null
        }

    private fun handleViewedEvent(state: State): FirstProblemOnboardingReducerResult =
        state to setOf(
            InternalAction.SetFirstProblemOnboardingShownFlag,
            InternalAction.LogAnalyticEvent(FirstProblemOnboardingViewedHyperskillAnalyticEvent)
        )

    private fun getNavigateActionByLearningActivity(learningActivity: LearningActivity?) =
        Action.ViewAction.CompleteFirstProblemOnboarding(
            if (learningActivity?.targetType == LearningActivityTargetType.STEP) {
                learningActivity.targetId?.let { stepId ->
                    StepRoute.Learn.Step(stepId = stepId, topicId = learningActivity.topicId)
                }
            } else {
                null
            }
        )

    private fun State.updateProfileState(newProfileState: ProfileState): State =
        copy(profileState = newProfileState)

    private fun State.updateNextLearningActivityState(newNextLearningActivityState: NextLearningActivityState): State =
        copy(nextLearningActivityState = newNextLearningActivityState)
}