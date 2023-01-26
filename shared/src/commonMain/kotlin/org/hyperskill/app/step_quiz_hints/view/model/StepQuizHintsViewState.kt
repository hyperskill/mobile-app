package org.hyperskill.app.step_quiz_hints.view.model

// I think it would be better if we'll have StepQuizHintsViewState in scope of the Feature -> StepQuizHintsFeature.ViewState
// Kotlin Extensions allows to do this?
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

    // After Kotlin/Native compiling in ObjC this becomes (not friendly):
    // ReactToHint -> reacttohint
    // SeeNextHint -> seenexthint
    // LastHint -> lasthint
    // After using the following naming conversion it becomes friendly to use:
    // ReactToHint -> reactToHint
    // SeeNextHint -> seeNextHint
    // LastHint -> lastHint
    enum class HintState {
        REACT_TO_HINT,
        SEE_NEXT_HINT,
        LAST_HINT
    }
}