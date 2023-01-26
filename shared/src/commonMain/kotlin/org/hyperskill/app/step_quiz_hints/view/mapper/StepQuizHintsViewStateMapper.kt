package org.hyperskill.app.step_quiz_hints.view.mapper

import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_hints.view.model.StepQuizHintsViewState

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
                when {
                    state.currentHint != null -> {
                        val hint = state.currentHint
                        StepQuizHintsViewState.Content.HintCard(
                            hintText = hint.localizedText.ifBlank { hint.text },
                            authorAvatar = hint.user.avatar,
                            authorName = hint.user.fullName,
                            hintState = when {
                                !state.hintHasReaction -> {
                                    StepQuizHintsViewState.HintState.REACT_TO_HINT
                                }
                                state.hintHasReaction && state.hintsIds.isNotEmpty() -> {
                                    StepQuizHintsViewState.HintState.SEE_NEXT_HINT
                                }
                                else -> StepQuizHintsViewState.HintState.LAST_HINT
                            }
                        )
                    }
                    state.hintsIds.isNotEmpty() -> {
                        StepQuizHintsViewState.Content.SeeHintButton
                    }
                    else -> StepQuizHintsViewState.Idle
                }
            }
            is StepQuizHintsFeature.State.NetworkError -> StepQuizHintsViewState.Error
        }
}