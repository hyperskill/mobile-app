package org.hyperskill.app.android.step_quiz.view.dialog

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature

object ProblemsOnboardingBottomSheetFactory {
    const val TAG = "ProblemsOnboardingBottomSheet"
    fun getProblemsOnboardingBottomSheet(
        modalType: StepQuizFeature.ProblemOnboardingModal
    ): BottomSheetDialogFragment =
        when (modalType) {
            StepQuizFeature.ProblemOnboardingModal.Parsons -> {
                ProblemOnboardingBottomSheetDialogFragment.newInstance(modalType)
            }
        }
}