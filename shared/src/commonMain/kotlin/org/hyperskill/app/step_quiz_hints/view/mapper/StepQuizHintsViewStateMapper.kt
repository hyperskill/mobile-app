package org.hyperskill.app.step_quiz_hints.view.mapper

import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature

internal object StepQuizHintsViewStateMapper {
    fun mapState(state: StepQuizHintsFeature.State): StepQuizHintsFeature.ViewState =
        when (state) {
            is StepQuizHintsFeature.State.Idle -> StepQuizHintsFeature.ViewState.Idle
            is StepQuizHintsFeature.State.Loading -> {
                if (state.isInitialLoading) {
                    StepQuizHintsFeature.ViewState.InitialLoading
                } else {
                    StepQuizHintsFeature.ViewState.HintLoading
                }
            }
            is StepQuizHintsFeature.State.Content -> {
                when {
                    state.currentHint != null -> {
                        val hint = state.currentHint
                        StepQuizHintsFeature.ViewState.Content.HintCard(
                            hintText = hint.localizedText.ifBlank { hint.text },
                            authorAvatar = hint.user.avatar,
                            authorName = hint.user.fullName,
                            hintState = when {
                                !state.hintHasReaction -> {
                                    StepQuizHintsFeature.ViewState.HintState.REACT_TO_HINT
                                }
                                state.hintHasReaction && state.hintsIds.isNotEmpty() && !state.isFreemiumEnabled -> {
                                    StepQuizHintsFeature.ViewState.HintState.SEE_NEXT_HINT
                                }
                                else -> StepQuizHintsFeature.ViewState.HintState.LAST_HINT
                            }
                        )
                    }
                    state.hintsIds.isNotEmpty() -> {
                        StepQuizHintsFeature.ViewState.Content.SeeHintButton
                    }
                    else -> StepQuizHintsFeature.ViewState.Idle
                }
            }
            is StepQuizHintsFeature.State.NetworkError -> StepQuizHintsFeature.ViewState.Error
        }
}