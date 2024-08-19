package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer
import org.hyperskill.app.step_quiz_code_blanks.presentation.getRequestedParentFeatureAction
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsReducer
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarReducer

internal class StepQuizChildFeatureReducer(
    private val stepQuizToolbarReducer: StepQuizToolbarReducer,
    private val stepQuizHintsReducer: StepQuizHintsReducer,
    private val stepQuizCodeBlanksReducer: StepQuizCodeBlanksReducer
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
            is StepQuizFeature.Message.StepQuizToolbarMessage -> {
                val (stepQuizToolbarState, stepQuizToolbarActions) =
                    reduceStepQuizToolbarMessage(state.stepQuizToolbarState, message.message)
                state.copy(stepQuizToolbarState = stepQuizToolbarState) to stepQuizToolbarActions
            }
            is StepQuizFeature.Message.StepQuizCodeBlanksMessage -> {
                val (stepQuizCodeBlanksState, stepQuizCodeBlanksActions) =
                    reduceStepQuizCodeBlanksMessage(state.stepQuizCodeBlanksState, message.message)
                state.copy(stepQuizCodeBlanksState = stepQuizCodeBlanksState) to stepQuizCodeBlanksActions
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

    fun reduceStepQuizToolbarMessage(
        state: StepQuizToolbarFeature.State,
        message: StepQuizToolbarFeature.Message
    ): Pair<StepQuizToolbarFeature.State, Set<StepQuizFeature.Action>> {
        val (stepQuizToolbarState, stepQuizToolbarActions) = stepQuizToolbarReducer.reduce(state, message)

        val actions = stepQuizToolbarActions
            .map {
                if (it is StepQuizToolbarFeature.Action.ViewAction) {
                    StepQuizFeature.Action.ViewAction.StepQuizToolbarViewAction(it)
                } else {
                    StepQuizFeature.Action.StepQuizToolbarAction(it)
                }
            }
            .toSet()

        return stepQuizToolbarState to actions
    }

    fun reduceStepQuizCodeBlanksMessage(
        state: StepQuizCodeBlanksFeature.State,
        message: StepQuizCodeBlanksFeature.Message
    ): Pair<StepQuizCodeBlanksFeature.State, Set<StepQuizFeature.Action>> {
        val (stepQuizCodeBlanksState, stepQuizCodeBlanksActions) =
            stepQuizCodeBlanksReducer.reduce(state, message)

        val requestedParentFeatureAction =
            stepQuizCodeBlanksActions.getRequestedParentFeatureAction()

        return if (requestedParentFeatureAction != null) {
            stepQuizCodeBlanksState to
                when (requestedParentFeatureAction.parentFeatureAction) {
                    StepQuizCodeBlanksFeature.ParentFeatureAction.HighlightCallToActionButton ->
                        setOf(
                            StepQuizFeature.Action.ViewAction.ScrollTo.CallToActionButton,
                            StepQuizFeature.Action.ViewAction.HighlightCallToActionButton
                        )
                }
        } else {
            stepQuizCodeBlanksState to
                stepQuizCodeBlanksActions
                    .map {
                        if (it is StepQuizCodeBlanksFeature.Action.ViewAction) {
                            StepQuizFeature.Action.ViewAction.StepQuizCodeBlanksViewAction(it)
                        } else {
                            StepQuizFeature.Action.StepQuizCodeBlanksAction(it)
                        }
                    }
                    .toSet()
        }
    }
}