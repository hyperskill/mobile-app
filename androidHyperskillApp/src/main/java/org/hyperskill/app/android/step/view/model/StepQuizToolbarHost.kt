package org.hyperskill.app.android.step.view.model

import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature

interface StepQuizToolbarHost {
    fun render(viewState: StepQuizToolbarFeature.ViewState)
}