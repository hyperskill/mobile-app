package org.hyperskill.app.android.step_quiz.view.factory

import android.view.View
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizDescriptionBinding
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate

object StepQuizViewStateDelegateFactory {
    fun create(
        fragmentStepQuizBinding: FragmentStepQuizBinding,
        descriptionBinding: LayoutStepQuizDescriptionBinding?,
        skeletonView: View,
        vararg quizViews: View
    ): ViewStateDelegate<StepQuizFeature.StepQuizState> =
        ViewStateDelegate<StepQuizFeature.StepQuizState>()
            .apply {
                addState<StepQuizFeature.StepQuizState.Idle>()
                addState<StepQuizFeature.StepQuizState.Unsupported>(
                    fragmentStepQuizBinding.stepQuizFeedbackBlocks.root
                )
                addState<StepQuizFeature.StepQuizState.Loading>(
                    *listOfNotNull(
                        skeletonView,
                        descriptionBinding?.stepQuizDescriptionSkeleton
                    ).toTypedArray()
                )
                addState<StepQuizFeature.StepQuizState.AttemptLoading>(
                    *listOfNotNull(
                        skeletonView,
                        descriptionBinding?.stepQuizDescriptionSkeleton
                    ).toTypedArray()
                )
                addState<StepQuizFeature.StepQuizState.AttemptLoaded>(
                    *listOfNotNull(
                        fragmentStepQuizBinding.stepQuizFeedbackBlocks.root,
                        descriptionBinding?.stepQuizDescription,
                        fragmentStepQuizBinding.stepQuizButtons.stepQuizSubmitButton,
                        fragmentStepQuizBinding.stepQuizStatistics,
                        *quizViews
                    ).toTypedArray()
                )
                addState<StepQuizFeature.StepQuizState.NetworkError>(fragmentStepQuizBinding.stepQuizNetworkError.root)
            }
}