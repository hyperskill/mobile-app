package org.hyperskill.app.android.step_quiz.view.factory

import android.view.View
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizDescriptionBinding
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate

object StepQuizViewStateDelegateFactory {
    fun create(
        fragmentStepQuizBinding: FragmentStepQuizBinding,
        descriptionBinding: LayoutStepQuizDescriptionBinding,
        skeletonView: View,
        vararg quizViews: View
    ): ViewStateDelegate<StepQuizFeature.StepQuizState> =
        ViewStateDelegate<StepQuizFeature.StepQuizState>()
            .apply {
                addState<StepQuizFeature.StepQuizState.Idle>()
                addState<StepQuizFeature.StepQuizState.Unsupported>(
                    fragmentStepQuizBinding.stepQuizFeedbackBlocks.root
                )
                addState<StepQuizFeature.StepQuizState.Loading>(skeletonView, descriptionBinding.stepQuizDescriptionSkeleton)
                addState<StepQuizFeature.StepQuizState.AttemptLoading>(
                    skeletonView,
                    descriptionBinding.stepQuizDescriptionSkeleton
                )
                addState<StepQuizFeature.StepQuizState.AttemptLoaded>(
                    fragmentStepQuizBinding.stepQuizFeedbackBlocks.root,
                    descriptionBinding.stepQuizDescription,
                    fragmentStepQuizBinding.stepQuizButtons.stepQuizSubmitButton,
                    fragmentStepQuizBinding.stepQuizStatistics,
                    fragmentStepQuizBinding.stepQuizProblemsLimit.root,
                    *quizViews
                )
                addState<StepQuizFeature.StepQuizState.NetworkError>(fragmentStepQuizBinding.stepQuizNetworkError.root)
            }
}