package org.hyperskill.app.android.step_quiz_parsons.view.delegate

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.decoration.VerticalMarginItemDecoration
import org.hyperskill.app.android.databinding.LayoutStepQuizParsonsBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizParsonsContentBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_parsons.view.adapter.ParsonsLinesAdapterDelegate
import org.hyperskill.app.android.step_quiz_parsons.view.model.ParsonsLine
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.app.core.model.mutate

class ParsonsStepQuizFormDelegate(
    context: Context,
    private val binding: LayoutStepQuizParsonsBinding,
    private val onQuizChanged: (Reply) -> Unit
) : StepQuizFormDelegate {

    private var selectedLinePosition: Int? = null

    private val linesAdapter = DefaultDelegateAdapter<ParsonsLine>().apply {
        addDelegate(
            ParsonsLinesAdapterDelegate(::onLineClick)
        )
    }

    @ColorInt
    private val enabledControlStrokeColor: Int =
        ContextCompat.getColor(context, org.hyperskill.app.R.color.color_primary_alpha_60)
    private val disabledControlStrokeColor: Int =
        ContextCompat.getColor(context, org.hyperskill.app.R.color.color_overlay_blue_alpha_12)

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
        setControlsEnabled(
            areControlsEnabled = false,
            binding = binding.parsonsStepContent
        )
    }
    override fun setState(state: StepQuizFeature.StepQuizState.AttemptLoaded) {
        linesAdapter.items = state.attempt.dataset?.lines?.mapIndexed { index, text ->
            ParsonsLine(
                lineNumber = index,
                text = text,
                tabsCount = 0,
                isSelected = false
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

    private fun onLineClick(position: Int) {
        if (position >= 0) {
            val previousSelectedLinePosition = selectedLinePosition
            val newSelectedLinePosition = updateSelectedItem(position, previousSelectedLinePosition)
            selectedLinePosition = newSelectedLinePosition
            if (newSelectedLinePosition != previousSelectedLinePosition) {
                setControlsEnabled(
                    areControlsEnabled = newSelectedLinePosition != null,
                    binding = binding.parsonsStepContent
                )
            }
        }
    }

    private fun updateSelectedItem(clickedItemPosition: Int, selectedItemPosition: Int?): Int? =
        if (clickedItemPosition != selectedItemPosition) {
            handleSelectionChange(
                clickedItemPosition = clickedItemPosition,
                selectedItemPosition = selectedItemPosition,
                items = linesAdapter.items,
                linesAdapter = linesAdapter
            )
        } else {
            selectedItemPosition
        }

    private fun handleSelectionChange(
        clickedItemPosition: Int,
        selectedItemPosition: Int?,
        items: List<ParsonsLine>,
        linesAdapter: DefaultDelegateAdapter<ParsonsLine>
    ): Int? {
        val clickedItem = items.getOrNull(clickedItemPosition)
        val previouslySelectedItem = selectedItemPosition?.let(items::getOrNull)
        linesAdapter.items = items.mutate {
            if (previouslySelectedItem != null) {
                set(selectedItemPosition, previouslySelectedItem.copy(isSelected = false))
            }
            if (clickedItem != null) {
                set(clickedItemPosition, clickedItem.copy(isSelected = true))
            }
        }
        return if (clickedItem != null) {
            clickedItemPosition
        } else {
            null
        }
    }

    private fun setControlsEnabled(
        areControlsEnabled: Boolean,
        binding: LayoutStepQuizParsonsContentBinding
    ) {
        with(binding) {
            parsonsAddTabButton.isEnabled = areControlsEnabled
            parsonsRemoveTabButton.isEnabled = areControlsEnabled
            parsonsDropDownLineButton.isEnabled = areControlsEnabled
            parsonsRaiseUpLineButton.isEnabled = areControlsEnabled
        }
    }
}