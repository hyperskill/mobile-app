package org.hyperskill.app.android.step_quiz_table.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.chrynan.parcelable.core.putParcelable
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizTableBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.fragment.DefaultStepQuizFragment
import org.hyperskill.app.android.step_quiz_table.view.delegate.TableStepQuizFormDelegate
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.domain.model.submissions.Cell
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.app.presentation.redux.container.ReduxView

class TableStepQuizFragment :
    DefaultStepQuizFragment(),
    ReduxView<StepQuizFeature.State, StepQuizFeature.Action.ViewAction>,
    TableColumnSelectionBottomSheetDialogFragment.Callback {
    companion object {
        fun newInstance(step: Step): Fragment {
            val arguments = Bundle().apply {
                putParcelable(KEY_STEP, step)
            }
            return TableStepQuizFragment().apply {
                this.arguments = arguments
            }
        }
    }

    private var _binding: LayoutStepQuizTableBinding? = null
    private val binding get() = _binding!!

    private lateinit var tableStepQuizFormDelegate: TableStepQuizFormDelegate

    override val quizViews: Array<View>
        get() = arrayOf(binding.tableRecycler)

    override val skeletonView: View
        get() = binding.tableSkeleton.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = LayoutStepQuizTableBinding.inflate(LayoutInflater.from(requireContext()), viewBinding.root, false)
        viewBinding.root.addView(binding.root)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun createStepQuizFormDelegate(containerBinding: FragmentStepQuizBinding): StepQuizFormDelegate {
        tableStepQuizFormDelegate = TableStepQuizFormDelegate(containerBinding, binding, childFragmentManager, onQuizChanged = ::syncReplyState)
        return tableStepQuizFormDelegate
    }

    override fun onSyncChosenColumnsWithParent(index: Int, chosenRows: List<Cell>) {
        tableStepQuizFormDelegate.updateTableSelectionItem(index, chosenRows)
    }
}