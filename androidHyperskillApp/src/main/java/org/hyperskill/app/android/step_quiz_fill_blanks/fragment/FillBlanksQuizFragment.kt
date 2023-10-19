package org.hyperskill.app.android.step_quiz_fill_blanks.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.hyperskill.app.android.databinding.LayoutStepQuizDescriptionBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizFillBlanksBindingBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.fragment.DefaultStepQuizFragment
import org.hyperskill.app.android.step_quiz_fill_blanks.delegate.FillBlanksStepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_fill_blanks.dialog.FillBlanksInputDialogFragment
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute

class FillBlanksQuizFragment :
    DefaultStepQuizFragment(),
    FillBlanksInputDialogFragment.Callback {

    companion object {
        fun newInstance(
            step: Step,
            stepRoute: StepRoute
        ): FillBlanksQuizFragment =
            FillBlanksQuizFragment().apply {
                this.step = step
                this.stepRoute = stepRoute
            }
    }

    private var _binding: LayoutStepQuizFillBlanksBindingBinding? = null
    private val binding: LayoutStepQuizFillBlanksBindingBinding
        get() = requireNotNull(_binding)

    private var fillBlanksStepQuizFormDelegate: FillBlanksStepQuizFormDelegate? = null

    override val quizViews: Array<View>
        get() = arrayOf(binding.stepQuizFillBlanksContainer)
    override val skeletonView: View
        get() = binding.stepQuizFillBlanksSkeleton.root

    override val descriptionBinding: LayoutStepQuizDescriptionBinding? = null

    override fun createStepView(layoutInflater: LayoutInflater, parent: ViewGroup): View {
        val binding = LayoutStepQuizFillBlanksBindingBinding.inflate(layoutInflater, parent, false)
        this._binding = binding
        return binding.root
    }

    override fun createStepQuizFormDelegate(): StepQuizFormDelegate =
        FillBlanksStepQuizFormDelegate(
            binding = binding,
            fragmentManager = childFragmentManager,
            onQuizChanged = ::syncReplyState
        ).also {
            this.fillBlanksStepQuizFormDelegate = it
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        fillBlanksStepQuizFormDelegate = null
    }

    override fun onSyncInputItemWithParent(index: Int, text: String) {
        fillBlanksStepQuizFormDelegate?.onInputItemModified(index, text)
    }
}