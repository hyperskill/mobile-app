package org.hyperskill.app.android.step_quiz_parsons.view.delegate

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.decoration.VerticalMarginItemDecoration
import org.hyperskill.app.android.databinding.LayoutStepQuizParsonsBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_parsons.view.adapter.ParsonsLinesAdapterDelegate
import org.hyperskill.app.android.step_quiz_parsons.view.model.ParsonsLine
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter

class ParsonsStepQuizFormDelegate(
    binding: LayoutStepQuizParsonsBinding,
    private val onQuizChanged: (Reply) -> Unit
) : StepQuizFormDelegate {

    private val linesAdapter = DefaultDelegateAdapter<ParsonsLine>().apply {
        addDelegate(ParsonsLinesAdapterDelegate())
    }

    init {
        with(binding.parsonsStepContent.parsonsRecycler) {
            adapter = linesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                VerticalMarginItemDecoration(resources.getDimensionPixelOffset(R.dimen.step_quiz_parsons_line_gap))
            )
            (itemAnimator as? SimpleItemAnimator)
                ?.supportsChangeAnimations = false
        }
    }
    override fun setState(state: StepQuizFeature.StepQuizState.AttemptLoaded) {
        linesAdapter.items = state.attempt.dataset?.lines?.mapIndexed { index, text ->
            ParsonsLine(
                lineNumber = index,
                text = text,
                tabsCount = 0
            )
        } ?: emptyList()
    }

    override fun createReply(): Reply =
        Reply.parsons(
            lines = linesAdapter.items.map { line ->
                org.hyperskill.app.step_quiz.domain.model.submissions.ParsonsLine(
                    level = line.tabsCount,
                    lineNumber = line.lineNumber
                )
            }
        )
}