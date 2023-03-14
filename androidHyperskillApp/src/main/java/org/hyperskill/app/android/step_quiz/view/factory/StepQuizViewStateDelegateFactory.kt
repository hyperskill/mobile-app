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
    ): ViewStateDelegate<StepQuizFeature.State> =
        ViewStateDelegate<StepQuizFeature.State>()
            .apply {
                addState<StepQuizFeature.State.Idle>()
                addState<StepQuizFeature.State.Unsupported>(
                    fragmentStepQuizBinding.stepQuizFeedbackBlocks.root
                )
                addState<StepQuizFeature.State.Loading>(skeletonView, descriptionBinding.stepQuizDescriptionSkeleton)
                addState<StepQuizFeature.State.AttemptLoading>(
                    skeletonView,
                    descriptionBinding.stepQuizDescriptionSkeleton
                )
                addState<StepQuizFeature.State.AttemptLoaded>(
                    fragmentStepQuizBinding.stepQuizFeedbackBlocks.root,
                    descriptionBinding.stepQuizDescription,
                    fragmentStepQuizBinding.stepQuizButtons.stepQuizSubmitButton,
                    fragmentStepQuizBinding.stepQuizStatistics,
                    *quizViews
                )
                addState<StepQuizFeature.State.NetworkError>(fragmentStepQuizBinding.stepQuizNetworkError.root)
            }
}