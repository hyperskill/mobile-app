package org.hyperskill.app.step_quiz.presentation

val StepQuizFeature.StepQuizState.attemptLoadedState: StepQuizFeature.StepQuizState.AttemptLoaded?
    get() = when (this) {
        is StepQuizFeature.StepQuizState.AttemptLoaded -> this
        is StepQuizFeature.StepQuizState.AttemptLoading -> this.oldState
        StepQuizFeature.StepQuizState.Idle,
        StepQuizFeature.StepQuizState.Loading,
        StepQuizFeature.StepQuizState.NetworkError,
        StepQuizFeature.StepQuizState.Unsupported -> null
    }