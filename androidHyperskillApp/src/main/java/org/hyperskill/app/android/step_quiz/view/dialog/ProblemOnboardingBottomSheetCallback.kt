package org.hyperskill.app.android.step_quiz.view.dialog

import org.hyperskill.app.step_quiz.presentation.StepQuizFeature

interface ProblemOnboardingBottomSheetCallback {
    fun problemOnboardingShown(modalType: StepQuizFeature.ProblemOnboardingModal)
    fun problemOnboardingHidden(modalType: StepQuizFeature.ProblemOnboardingModal)
}