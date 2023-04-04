package org.hyperskill.app.android.step_quiz_table.view.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.databinding.LayoutStepQuizDescriptionBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizTableBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.fragment.DefaultStepQuizFragment
import org.hyperskill.app.android.step_quiz_table.view.delegate.TableStepQuizFormDelegate
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.model.submissions.Cell
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.app.presentation.redux.container.ReduxView

class TableStepQuizFragment :
    DefaultStepQuizFragment(),
    ReduxView<StepQuizFeature.State, StepQuizFeature.Action.ViewAction>,
    TableColumnSelectionBottomSheetDialogFragment.Callback {
    companion object {
        fun newInstance(step: Step, stepRoute: StepRoute): Fragment =
            TableStepQuizFragment().apply {
                this.step = step
                this.stepRoute = stepRoute
            }
    }

    private var _binding: LayoutStepQuizTableBinding? = null
    private val binding get() = _binding!!

    private lateinit var tableStepQuizFormDelegate: TableStepQuizFormDelegate

    override val quizViews: Array<View>
        get() = arrayOf(binding.tableRecycler)

    override val skeletonView: View
        get() = binding.tableSkeleton.root

    override val descriptionBinding: LayoutStepQuizDescriptionBinding
        get() = binding.tableStepDescription

    override fun createStepView(layoutInflater: LayoutInflater, parent: ViewGroup): View {
        val binding = LayoutStepQuizTableBinding.inflate(layoutInflater, parent, false).also {
            _binding = it
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun createStepQuizFormDelegate(): StepQuizFormDelegate {
        tableStepQuizFormDelegate = TableStepQuizFormDelegate(binding, childFragmentManager, onQuizChanged = ::syncReplyState)
        return tableStepQuizFormDelegate
    }

    override fun onSyncChosenColumnsWithParent(index: Int, chosenRows: List<Cell>) {
        tableStepQuizFormDelegate.updateTableSelectionItem(index, chosenRows)
    }
}