package org.hyperskill.app.android.step_quiz_matching.view.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.databinding.LayoutStepQuizDescriptionBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizMatchingBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.fragment.DefaultStepQuizFragment
import org.hyperskill.app.android.step_quiz_matching.view.delegate.MatchingStepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_table.view.fragment.TableColumnSelectionBottomSheetDialogFragment
import org.hyperskill.app.android.step_quiz_table.view.model.TableChoiceItem
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.app.presentation.redux.container.ReduxView

class MatchingStepQuizFragment :
    DefaultStepQuizFragment(),
    ReduxView<StepQuizFeature.State, StepQuizFeature.Action.ViewAction>,
    TableColumnSelectionBottomSheetDialogFragment.Callback {
    companion object {
        fun newInstance(step: Step, stepRoute: StepRoute): Fragment =
            MatchingStepQuizFragment().apply {
                this.step = step
                this.stepRoute = stepRoute
            }
    }

    private var _binding: LayoutStepQuizMatchingBinding? = null
    private val binding get() = _binding!!

    private var matchingStepQuizFormDelegate: MatchingStepQuizFormDelegate? = null

    override val quizViews: Array<View>
        get() = arrayOf(binding.matchingRecycler)

    override val skeletonView: View
        get() = binding.matchingSkeleton.root

    override val descriptionBinding: LayoutStepQuizDescriptionBinding
        get() = binding.matchingStepDescription

    override fun createStepView(layoutInflater: LayoutInflater, parent: ViewGroup): View {
        val binding = LayoutStepQuizMatchingBinding.inflate(layoutInflater, parent, false).also {
            _binding = it
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        matchingStepQuizFormDelegate = null
        super.onDestroyView()
    }

    override fun createStepQuizFormDelegate(): StepQuizFormDelegate {
        val delegate = MatchingStepQuizFormDelegate(
            binding, childFragmentManager, onQuizChanged = ::syncReplyState
        )
        this.matchingStepQuizFormDelegate = delegate
        return delegate
    }

    override fun onSyncChosenColumnsWithParent(index: Int, chosenRows: List<TableChoiceItem>) {
        matchingStepQuizFormDelegate?.updateTableSelectionItem(index, chosenRows)
    }
}