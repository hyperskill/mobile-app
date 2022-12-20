package org.hyperskill.app.step_quiz_hints.mapper

import org.hyperskill.app.step_quiz_hints.model.StepQuizHintsViewState
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature

object StepQuizHintsViewStateMapper {
    fun mapState(state: StepQuizHintsFeature.State): StepQuizHintsViewState =
        when (state) {
            is StepQuizHintsFeature.State.Idle -> StepQuizHintsViewState.Idle
            is StepQuizHintsFeature.State.Loading -> {
                if (state.isInitialLoading) {
                    StepQuizHintsViewState.InitialLoading
                } else {
                    StepQuizHintsViewState.HintLoading
                }
            }
            is StepQuizHintsFeature.State.Content -> {
                val hint = state.currentHint
                if (hint == null) {
                    StepQuizHintsViewState.Content.SeeHintButton
                } else {
                    StepQuizHintsViewState.Content.HintCard(
                        hintText = hint.text,
                        authorAvatar = hint.user.avatar,
                        authorName = hint.user.fullName,
                        hintState = when {
                            !state.hintHasReaction -> {
                                StepQuizHintsViewState.HintState.ReactToHint
                            }
                            state.hintHasReaction && state.hintsIds.isNotEmpty() -> {
                                StepQuizHintsViewState.HintState.SeeNextHint
                            }
                            else -> StepQuizHintsViewState.HintState.LastHint
                        }
                    )
                }
            }
            is StepQuizHintsFeature.State.NetworkError -> StepQuizHintsViewState.Error
        }
}