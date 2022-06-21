package org.hyperskill.app.android.step_quiz_choice.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.chrynan.parcelable.core.putParcelable
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizChoiceBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.fragment.DefaultStepQuizFragment
import org.hyperskill.app.android.step_quiz_choice.view.delegate.ChoiceStepQuizFormDelegate
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.app.presentation.redux.container.ReduxView

class ChoiceStepQuizFragment :
    DefaultStepQuizFragment(),
    ReduxView<StepQuizFeature.State, StepQuizFeature.Action.ViewAction> {
    companion object {
        fun newInstance(step: Step): Fragment {
            val arguments = Bundle().apply {
                putParcelable(KEY_STEP, step)
            }
            return ChoiceStepQuizFragment().apply {
                this.arguments = arguments
            }
        }
    }

    private var _binding: LayoutStepQuizChoiceBinding? = null
    private val binding get() = _binding!!

    override val quizViews: Array<View>
        get() = arrayOf(binding.stepQuizChoiceRecyclerView)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = LayoutStepQuizChoiceBinding.inflate(LayoutInflater.from(requireContext()), viewBinding.root, false)
        viewBinding.root.addView(binding.root)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun createStepQuizFormDelegate(containerBinding: FragmentStepQuizBinding): StepQuizFormDelegate =
        ChoiceStepQuizFormDelegate(containerBinding, binding, requireContext(), onQuizChanged = ::syncReplyState)
}