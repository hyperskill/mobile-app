package org.hyperskill.app.android.step_quiz_parsons.view.delegate

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.decoration.VerticalMarginItemDecoration
import org.hyperskill.app.android.databinding.LayoutStepQuizParsonsBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizParsonsContentBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_parsons.view.adapter.ParsonsLinesAdapterDelegate
import org.hyperskill.app.android.step_quiz_parsons.view.model.ParsonsLine
import org.hyperskill.app.android.step_quiz_parsons.view.model.ParsonsLineControlMessage
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.app.core.model.mutate
import ru.nobird.app.core.model.swap

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
        with(binding.parsonsStepContent) {
            parsonsAddTabButton.setOnClickListener {
                onNewControlMessage(ParsonsLineControlMessage.ADD_TAB)
            }
            parsonsRemoveTabButton.setOnClickListener {
                onNewControlMessage(ParsonsLineControlMessage.REMOVE_TAB)
            }
            parsonsDropDownLineButton.setOnClickListener {
                onNewControlMessage(ParsonsLineControlMessage.DROP_DOWN)
            }
            parsonsRaiseUpLineButton.setOnClickListener {
                onNewControlMessage(ParsonsLineControlMessage.RAISE_UP)
            }
        }
        onSelectedLinePositionChanged(
            newSelectedLinePosition = null,
            binding = binding.parsonsStepContent,
            lines = linesAdapter.items
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
                onSelectedLinePositionChanged(
                    newSelectedLinePosition = newSelectedLinePosition,
                    lines = linesAdapter.items,
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

    private fun onNewControlMessage(
        controlMessage: ParsonsLineControlMessage
    ) {
        val newSelectedLinePosition = handleControlMessage(
            controlMessage = controlMessage,
            selectedLinePosition = selectedLinePosition,
            binding = binding.parsonsStepContent,
            linesAdapter = linesAdapter
        )
        selectedLinePosition = newSelectedLinePosition
        onSelectedLinePositionChanged(
            newSelectedLinePosition = newSelectedLinePosition,
            lines = linesAdapter.items,
            binding = binding.parsonsStepContent
        )
    }

    private fun handleControlMessage(
        controlMessage: ParsonsLineControlMessage,
        selectedLinePosition: Int?,
        binding: LayoutStepQuizParsonsContentBinding,
        linesAdapter: DefaultDelegateAdapter<ParsonsLine>
    ): Int? {
        if (selectedLinePosition == null) return selectedLinePosition
        return when (controlMessage) {
            ParsonsLineControlMessage.ADD_TAB -> TODO()
            ParsonsLineControlMessage.REMOVE_TAB -> TODO()
            ParsonsLineControlMessage.RAISE_UP,
            ParsonsLineControlMessage.DROP_DOWN -> {
                val targetPosition = when (controlMessage) {
                    ParsonsLineControlMessage.RAISE_UP -> selectedLinePosition - 1
                    ParsonsLineControlMessage.DROP_DOWN -> selectedLinePosition + 1
                    else -> error("")
                }
                linesAdapter.items = linesAdapter.items.swap(selectedLinePosition, targetPosition)
                targetPosition
            }
        }
    }

    private fun onSelectedLinePositionChanged(
        newSelectedLinePosition: Int?,
        lines: List<ParsonsLine>,
        binding: LayoutStepQuizParsonsContentBinding
    ) {
        with(binding) {
            parsonsAddTabButton.isEnabled = newSelectedLinePosition != null
            parsonsRemoveTabButton.isEnabled = newSelectedLinePosition != null
            parsonsRaiseUpLineButton.isEnabled =
                newSelectedLinePosition != null &&
                    newSelectedLinePosition in 1..lines.lastIndex
            parsonsDropDownLineButton.isEnabled =
                newSelectedLinePosition != null &&
                    newSelectedLinePosition in 0 until lines.lastIndex
        }
    }
}