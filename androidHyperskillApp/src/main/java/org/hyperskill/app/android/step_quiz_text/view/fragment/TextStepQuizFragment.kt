package org.hyperskill.app.android.step_quiz_text.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizTextBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.fragment.DefaultStepQuizFragment
import org.hyperskill.app.android.step_quiz_text.view.delegate.TextStepQuizFormDelegate
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.app.presentation.redux.container.ReduxView

class TextStepQuizFragment :
    DefaultStepQuizFragment(),
    ReduxView<StepQuizFeature.State, StepQuizFeature.Action.ViewAction> {
    companion object {
        fun newInstance(step: Step, stepRoute: StepRoute): Fragment =
            TextStepQuizFragment().apply {
                this.step = step
                this.stepRoute = stepRoute
            }
    }

    private var _binding: LayoutStepQuizTextBinding? = null
    private val binding get() = _binding!!

    override val quizViews: Array<View>
        get() = arrayOf(binding.stringStepQuizField)

    override val skeletonView: View
        get() = binding.stringStepQuizSkeleton.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = LayoutStepQuizTextBinding.inflate(LayoutInflater.from(requireContext()), viewBinding.root, false)
        viewBinding.root.addView(binding.root)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun createStepQuizFormDelegate(containerBinding: FragmentStepQuizBinding): StepQuizFormDelegate =
        TextStepQuizFormDelegate(containerBinding, binding, step.block.name, onQuizChanged = ::syncReplyState)
}