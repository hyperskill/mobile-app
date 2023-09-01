package org.hyperskill.app.android.step_quiz_parsons.view.delegate

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.view.model.themes.CodeTheme
import org.hyperskill.app.android.code.view.model.themes.CodeThemes
import org.hyperskill.app.android.core.view.ui.adapter.decoration.VerticalMarginItemDecoration
import org.hyperskill.app.android.databinding.LayoutStepQuizParsonsBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizParsonsContentBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_parsons.view.adapter.ParsonsLinesAdapterDelegate
import org.hyperskill.app.android.step_quiz_parsons.view.mapper.ParsonsLinesMapper
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

    private val codeTheme: CodeTheme =
        CodeThemes.resolve(context)

    private val linesAdapter = DefaultDelegateAdapter<ParsonsLine>().apply {
        addDelegate(
            ParsonsLinesAdapterDelegate(
                codeTextColor = codeTheme.syntax.plain,
                onLineClick = ::onLineClick
            )
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
    }

    override fun setState(state: StepQuizFeature.StepQuizState.AttemptLoaded) {
        val lines = ParsonsLinesMapper.mapToParsonsLines(
            attempt = state.attempt,
            submission = (state.submissionState as? StepQuizFeature.SubmissionState.Loaded)?.submission,
            selectedLinePosition = selectedLinePosition
        )
        Log.d("ParsonsStepQuizFormDelegate", "setState \nselectedLinePosition=$selectedLinePosition\nlinesAdapter.items=$lines")
        linesAdapter.items = lines
        updateControlsEnabled(
            selectedLinePosition = selectedLinePosition,
            lines = lines,
            binding = binding.parsonsStepContent
        )
    }

    override fun createReply(): Reply =
        createReplyInternal(linesAdapter.items)

    private fun createReplyInternal(
        lines: List<ParsonsLine>
    ): Reply =
        Reply.parsons(
            lines.map { line ->
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
                    selectedLinePosition = newSelectedLinePosition,
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
        Log.d("ParsonsStepQuizFormDelegate", "onNewControlMessage($controlMessage)")
        val currentLines = linesAdapter.items
        val handleControlMessageResult = handleControlMessage(
            controlMessage = controlMessage,
            selectedLinePosition = selectedLinePosition,
            lines = currentLines
        )
        selectedLinePosition = handleControlMessageResult.selectedLinePosition
        updateControlsEnabled(
            selectedLinePosition = handleControlMessageResult.selectedLinePosition,
            lines = handleControlMessageResult.lines,
            binding = binding.parsonsStepContent
        )
        Log.d("ParsonsStepQuizFormDelegate", "onNewControlMessage: call onQuizChanged;\nselectedLinePosition=${selectedLinePosition}\nlines=${handleControlMessageResult.lines}")
        onQuizChanged(createReplyInternal(handleControlMessageResult.lines))
    }

    private fun handleControlMessage(
        controlMessage: ParsonsLineControlMessage,
        selectedLinePosition: Int?,
        lines: List<ParsonsLine>
    ): ControlMessageHandleResult {
        if (selectedLinePosition == null) {
            return ControlMessageHandleResult(
                selectedLinePosition,
                lines
            )
        }
        return when (controlMessage) {
            ParsonsLineControlMessage.ADD_TAB,
            ParsonsLineControlMessage.REMOVE_TAB ->
                ControlMessageHandleResult(
                    selectedLinePosition = selectedLinePosition,
                    lines = handleTabChanges(controlMessage, selectedLinePosition, lines)
                )
            ParsonsLineControlMessage.RAISE_UP,
            ParsonsLineControlMessage.DROP_DOWN ->
                handleLineDrag(controlMessage, selectedLinePosition, lines)
        }
    }

    private fun handleTabChanges(
        controlMessage: ParsonsLineControlMessage,
        selectedLinePosition: Int,
        lines: List<ParsonsLine>
    ): List<ParsonsLine> {
        val selectedLine = lines.getOrNull(selectedLinePosition) ?: return lines
        val newTabsCount = when (controlMessage) {
            ParsonsLineControlMessage.ADD_TAB ->
                (selectedLine.tabsCount + 1).coerceAtMost(10)
            ParsonsLineControlMessage.REMOVE_TAB ->
                (selectedLine.tabsCount - 1).coerceAtLeast(0)
            else -> error("")
        }
        return lines.mutate {
            set(selectedLinePosition, selectedLine.copy(tabsCount = newTabsCount))
        }
    }

    private fun handleLineDrag(
        controlMessage: ParsonsLineControlMessage,
        selectedLinePosition: Int,
        lines: List<ParsonsLine>
    ): ControlMessageHandleResult {
        val targetPosition = when (controlMessage) {
            ParsonsLineControlMessage.RAISE_UP -> selectedLinePosition - 1
            ParsonsLineControlMessage.DROP_DOWN -> selectedLinePosition + 1
            else -> error("")
        }
        return ControlMessageHandleResult(
            selectedLinePosition = targetPosition,
            lines = lines.swap(selectedLinePosition, targetPosition)
        )
    }

    private fun updateControlsEnabled(
        selectedLinePosition: Int?,
        lines: List<ParsonsLine>,
        binding: LayoutStepQuizParsonsContentBinding
    ) {
        val selectedLine = if (selectedLinePosition != null) {
            lines.getOrNull(selectedLinePosition)
        } else {
            null
        }
        with(binding) {
            parsonsAddTabButton.isEnabled =
                selectedLinePosition != null &&
                    selectedLine?.tabsCount in 0 until MAX_TABS_COUNT
            parsonsRemoveTabButton.isEnabled =
                selectedLinePosition != null &&
                    selectedLine?.tabsCount in 1..MAX_TABS_COUNT
            parsonsRaiseUpLineButton.isEnabled =
                selectedLinePosition != null &&
                    selectedLinePosition in 1..lines.lastIndex
            parsonsDropDownLineButton.isEnabled =
                selectedLinePosition != null &&
                    selectedLinePosition in 0 until lines.lastIndex
        }
    }

    private data class ControlMessageHandleResult(
        val selectedLinePosition: Int?,
        val lines: List<ParsonsLine>
    )
}