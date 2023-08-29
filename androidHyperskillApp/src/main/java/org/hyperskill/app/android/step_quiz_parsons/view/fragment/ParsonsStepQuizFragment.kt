package org.hyperskill.app.android.step_quiz_parsons.view.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.hyperskill.app.android.databinding.LayoutStepQuizDescriptionBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizParsonsBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.fragment.DefaultStepQuizFragment
import org.hyperskill.app.android.step_quiz_parsons.view.delegate.ParsonsStepQuizFormDelegate
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.app.presentation.redux.container.ReduxView

class ParsonsStepQuizFragment :
    DefaultStepQuizFragment(),
    ReduxView<StepQuizFeature.State, StepQuizFeature.Action.ViewAction> {

    companion object {
        fun newInstance(step: Step, stepRoute: StepRoute): ParsonsStepQuizFragment =
            ParsonsStepQuizFragment().apply {
                this.step = step
                this.stepRoute = stepRoute
            }
    }

    private var _binding: LayoutStepQuizParsonsBinding? = null
    private val binding get() = _binding!!

    override val quizViews: Array<View>
        get() = arrayOf(binding.parsonsStepContent.root)
    override val skeletonView: View
        get() = binding.parsonsSkeleton.root
    override val descriptionBinding: LayoutStepQuizDescriptionBinding
        get() = binding.parsonsStepDescription

    override fun createStepView(layoutInflater: LayoutInflater, parent: ViewGroup): View {
        val binding = LayoutStepQuizParsonsBinding.inflate(layoutInflater, parent, false)
        _binding = binding
        return binding.root
    }

    override fun createStepQuizFormDelegate(): StepQuizFormDelegate =
        ParsonsStepQuizFormDelegate(
            binding = binding,
            onQuizChanged = ::syncReplyState
        )
}