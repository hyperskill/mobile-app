package org.hyperskill.app.android.step_quiz_text.view.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.databinding.LayoutStepQuizDescriptionBinding
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

    override val descriptionBinding: LayoutStepQuizDescriptionBinding
        get() = binding.codeStepDescription

    override fun createStepView(layoutInflater: LayoutInflater, parent: ViewGroup): View {
        val binding = LayoutStepQuizTextBinding.inflate(layoutInflater, parent, false).also {
            _binding = it
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun createStepQuizFormDelegate(): StepQuizFormDelegate =
        TextStepQuizFormDelegate(binding, step.block.name, onQuizChanged = ::syncReplyState)
}