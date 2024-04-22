package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsReducer

internal class StepQuizChildFeatureReducer(
    private val problemsLimitReducer: ProblemsLimitReducer,
    private val stepQuizHintsReducer: StepQuizHintsReducer
) {

    companion object

    fun reduce(
        state: StepQuizFeature.State,
        message: StepQuizFeature.ChildFeatureMessage
    ): StepQuizReducerResult =
        when (message) {
            is StepQuizFeature.Message.StepQuizHintsMessage -> {
                val (stepQuizHintsState, stepQuizHintsActions) =
                    reduceStepQuizHintsMessage(state.stepQuizHintsState, message.message)
                state.copy(stepQuizHintsState = stepQuizHintsState) to stepQuizHintsActions
            }
            is StepQuizFeature.Message.ProblemsLimitMessage -> {
                val (problemsLimitState, problemsLimitActions) =
                    reduceProblemsLimitMessage(state.problemsLimitState, message.message)
                state.copy(problemsLimitState = problemsLimitState) to problemsLimitActions
            }
        }

    fun reduceStepQuizHintsMessage(
        state: StepQuizHintsFeature.State,
        message: StepQuizHintsFeature.Message
    ): Pair<StepQuizHintsFeature.State, Set<StepQuizFeature.Action>> {
        val (stepQuizHintsState, stepQuizHintsActions) = stepQuizHintsReducer.reduce(state, message)

        val actions = stepQuizHintsActions
            .map {
                if (it is StepQuizHintsFeature.Action.ViewAction) {
                    StepQuizFeature.Action.ViewAction.StepQuizHintsViewAction(it)
                } else {
                    StepQuizFeature.Action.StepQuizHintsAction(it)
                }
            }
            .toSet()

        return stepQuizHintsState to actions
    }

    fun reduceProblemsLimitMessage(
        state: ProblemsLimitFeature.State,
        message: ProblemsLimitFeature.Message
    ): Pair<ProblemsLimitFeature.State, Set<StepQuizFeature.Action>> {
        val (problemsLimitState, problemsLimitActions) = problemsLimitReducer.reduce(state, message)

        val actions = problemsLimitActions
            .map {
                if (it is ProblemsLimitFeature.Action.ViewAction) {
                    StepQuizFeature.Action.ViewAction.ProblemsLimitViewAction(it)
                } else {
                    StepQuizFeature.Action.ProblemsLimitAction(it)
                }
            }
            .toSet()

        return problemsLimitState to actions
    }
}