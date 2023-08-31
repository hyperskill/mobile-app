package org.hyperskill.app.android.step_quiz_parsons.view.delegate

import android.content.Context
import androidx.core.text.HtmlCompat
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

    companion object {
        private const val MAX_TABS_COUNT = 10
    }

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
        updateControlsEnabled(
            newSelectedLinePosition = null,
            binding = binding.parsonsStepContent,
            lines = linesAdapter.items
        )
    }
    override fun setState(state: StepQuizFeature.StepQuizState.AttemptLoaded) {
        linesAdapter.items = state.attempt.dataset?.lines?.mapIndexed { index, text ->
            ParsonsLine(
                lineNumber = index,
                // Html parsing is used to handle symbols like &gt
                text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT),
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
                updateControlsEnabled(
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
            linesAdapter = linesAdapter
        )
        selectedLinePosition = newSelectedLinePosition
        updateControlsEnabled(
            newSelectedLinePosition = newSelectedLinePosition,
            lines = linesAdapter.items,
            binding = binding.parsonsStepContent
        )
    }

    private fun handleControlMessage(
        controlMessage: ParsonsLineControlMessage,
        selectedLinePosition: Int?,
        linesAdapter: DefaultDelegateAdapter<ParsonsLine>
    ): Int? {
        if (selectedLinePosition == null) return selectedLinePosition
        return when (controlMessage) {
            ParsonsLineControlMessage.ADD_TAB,
            ParsonsLineControlMessage.REMOVE_TAB -> {
                handleTabChanges(controlMessage, selectedLinePosition, linesAdapter)
                selectedLinePosition
            }
            ParsonsLineControlMessage.RAISE_UP,
            ParsonsLineControlMessage.DROP_DOWN ->
                handleLineDrag(controlMessage, selectedLinePosition, linesAdapter)
        }
    }

    private fun handleTabChanges(
        controlMessage: ParsonsLineControlMessage,
        selectedLinePosition: Int,
        linesAdapter: DefaultDelegateAdapter<ParsonsLine>
    ) {
        val selectedLine = linesAdapter.items.getOrNull(selectedLinePosition) ?: return
        val newTabsCount = when (controlMessage) {
            ParsonsLineControlMessage.ADD_TAB ->
                (selectedLine.tabsCount + 1).coerceAtMost(10)
            ParsonsLineControlMessage.REMOVE_TAB ->
                (selectedLine.tabsCount - 1).coerceAtLeast(0)
            else -> error("")
        }
        linesAdapter.items = linesAdapter.items.mutate {
            set(selectedLinePosition, selectedLine.copy(tabsCount = newTabsCount))
        }
    }

    private fun handleLineDrag(
        controlMessage: ParsonsLineControlMessage,
        selectedLinePosition: Int,
        linesAdapter: DefaultDelegateAdapter<ParsonsLine>
    ): Int {
        val targetPosition = when (controlMessage) {
            ParsonsLineControlMessage.RAISE_UP -> selectedLinePosition - 1
            ParsonsLineControlMessage.DROP_DOWN -> selectedLinePosition + 1
            else -> error("")
        }
        linesAdapter.items = linesAdapter.items.swap(selectedLinePosition, targetPosition)
        return targetPosition
    }

    private fun updateControlsEnabled(
        newSelectedLinePosition: Int?,
        lines: List<ParsonsLine>,
        binding: LayoutStepQuizParsonsContentBinding
    ) {
        val selectedLine = if (newSelectedLinePosition != null) {
            lines.getOrNull(newSelectedLinePosition)
        } else {
            null
        }
        with(binding) {
            parsonsAddTabButton.isEnabled =
                newSelectedLinePosition != null &&
                    selectedLine?.tabsCount in 0 until MAX_TABS_COUNT
            parsonsRemoveTabButton.isEnabled =
                newSelectedLinePosition != null &&
                    selectedLine?.tabsCount in 1..MAX_TABS_COUNT
            parsonsRaiseUpLineButton.isEnabled =
                newSelectedLinePosition != null &&
                    newSelectedLinePosition in 1..lines.lastIndex
            parsonsDropDownLineButton.isEnabled =
                newSelectedLinePosition != null &&
                    newSelectedLinePosition in 0 until lines.lastIndex
        }
    }
}