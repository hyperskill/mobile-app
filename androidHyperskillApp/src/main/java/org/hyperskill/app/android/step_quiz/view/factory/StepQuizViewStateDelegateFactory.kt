package org.hyperskill.app.android.step_quiz.view.factory

import android.view.View
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate

object StepQuizViewStateDelegateFactory {
    fun create(fragmentStepQuizBinding: FragmentStepQuizBinding, vararg quizViews: View): ViewStateDelegate<StepQuizFeature.State> =
        ViewStateDelegate<StepQuizFeature.State>()
            .apply {
                addState<StepQuizFeature.State.Idle>()
                addState<StepQuizFeature.State.Loading>(fragmentStepQuizBinding.stepQuizProgress)
                addState<StepQuizFeature.State.AttemptLoading>(fragmentStepQuizBinding.stepQuizProgress)
                addState<StepQuizFeature.State.AttemptLoaded>(
                    fragmentStepQuizBinding.stepQuizFeedbackBlocks.root,
                    fragmentStepQuizBinding.stepQuizDescription,
                    fragmentStepQuizBinding.stepQuizSubmitButton,
                    *quizViews
                )
                addState<StepQuizFeature.State.NetworkError>(fragmentStepQuizBinding.stepQuizNetworkError.root)
            }
}