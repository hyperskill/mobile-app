package org.hyperskill.app.step_quiz_hints.model

sealed interface StepQuizHintsViewState {
    object Idle : StepQuizHintsViewState

    object InitialLoading : StepQuizHintsViewState

    object HintLoading : StepQuizHintsViewState

    sealed interface Content : StepQuizHintsViewState {
        object SeeHintButton : Content
        data class HintCard(
            val authorAvatar: String,
            val authorName: String,
            val hintText: String,
            val hintState: HintState
        ) : Content
    }

    object Error : StepQuizHintsViewState

    enum class HintState {
        ReactToHint,
        SeeNextHint,
        LastHint
    }
}