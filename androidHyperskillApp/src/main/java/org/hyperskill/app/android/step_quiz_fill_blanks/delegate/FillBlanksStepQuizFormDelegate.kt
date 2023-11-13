package org.hyperskill.app.android.step_quiz_fill_blanks.delegate

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.transition.TransitionManager
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutStepQuizFillBlanksBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizFillBlanksOptionsBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_fill_blanks.adapter.FillBlanksInputItemAdapterDelegate
import org.hyperskill.app.android.step_quiz_fill_blanks.adapter.FillBlanksSelectItemAdapterDelegate
import org.hyperskill.app.android.step_quiz_fill_blanks.adapter.FillBlanksSelectOptionAdapterDelegate
import org.hyperskill.app.android.step_quiz_fill_blanks.adapter.FillBlanksTextItemAdapterDelegate
import org.hyperskill.app.android.step_quiz_fill_blanks.dialog.FillBlanksInputDialogFragment
import org.hyperskill.app.android.step_quiz_fill_blanks.model.FillBlanksSelectOptionUIItem
import org.hyperskill.app.android.step_quiz_fill_blanks.model.FillBlanksUiItem
import org.hyperskill.app.android.step_quiz_fill_blanks.model.HighlightResult
import org.hyperskill.app.android.step_quiz_fill_blanks.model.ResolveState
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksData
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksItem
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksMode
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksOption
import org.hyperskill.app.step_quiz_fill_blanks.model.InvalidFillBlanksConfigException
import org.hyperskill.app.step_quiz_fill_blanks.presentation.FillBlanksItemMapper
import org.hyperskill.app.step_quiz_fill_blanks.presentation.FillBlanksResolver
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.app.core.model.mutate

class FillBlanksStepQuizFormDelegate(
    private val binding: LayoutStepQuizFillBlanksBinding,
    private val optionsBinding: LayoutStepQuizFillBlanksOptionsBinding,
    private val fragmentManager: FragmentManager,
    private val onQuizChanged: (Reply) -> Unit
) : StepQuizFormDelegate {

    private val fillBlanksAdapter = DefaultDelegateAdapter<FillBlanksUiItem>().apply {
        addDelegate(FillBlanksTextItemAdapterDelegate())
        addDelegate(FillBlanksInputItemAdapterDelegate(::onInputItemClick))
    }

    private var fillBlanksOptionsAdapter: DefaultDelegateAdapter<FillBlanksSelectOptionUIItem>? = null

    private var fillBlanksMapper: FillBlanksItemMapper? = null

    private var resolveState: ResolveState = ResolveState.NotResolved

    private var wasSelectStateInitialized: Boolean = false

    private var selectOptions: List<FillBlanksOption> = emptyList()
    private val selectBlankToSelectOptionMap: MutableMap<Int, Int> = mutableMapOf()

    private var selectItemIndices: List<Int> = emptyList()
    private var highlightedSelectItemIndex: Int? = null

    init {
        setupFillBlankItemsRecycler()
    }

    override fun setState(state: StepQuizFeature.StepQuizState.AttemptLoaded) {
        val resolveState = resolve(resolveState, state)
        this.resolveState = resolveState
        if (resolveState is ResolveState.ResolveSucceed) {
            if (fillBlanksMapper == null) {
                fillBlanksMapper = FillBlanksItemMapper(mode = resolveState.mode)
                if (resolveState.mode == FillBlanksMode.SELECT) {
                    setupOptionsRecycler(optionsBinding)
                }
            }

            val fillBlanksData = fillBlanksMapper?.map(
                attempt = state.attempt,
                submission = (state.submissionState as? StepQuizFeature.SubmissionState.Loaded)?.submission
            )

            initSelectedState(resolveState, fillBlanksData)

            val isEnabled = StepQuizResolver.isQuizEnabled(state)

            fillBlanksAdapter.items = mapItems(fillBlanksData, highlightedSelectItemIndex, isEnabled)
            binding.root.post { binding.stepQuizFillBlanksRecycler.requestLayout() }

            fillBlanksOptionsAdapter?.items = mapBlankOptions(
                fillBlanksData = fillBlanksData,
                usedOptionsIndices = selectBlankToSelectOptionMap.values.toList(),
                isEnabled = isEnabled
            )
        }
    }

    private fun initSelectedState(
        resolveState: ResolveState.ResolveSucceed,
        fillBlanksData: FillBlanksData?
    ) {
        if (resolveState.mode == FillBlanksMode.SELECT && !wasSelectStateInitialized) {
            fillBlanksData ?: return

            val selectItemIndices = getSelectItemIndices(fillBlanksData.fillBlanks)
            this.selectItemIndices = selectItemIndices
            highlightedSelectItemIndex =
                getHighlightedSelectItemIndex(fillBlanksData.fillBlanks, selectItemIndices)

            if (selectOptions.isEmpty()) {
                selectOptions = fillBlanksData.options
            }

            if (selectBlankToSelectOptionMap.isEmpty()) {
                selectBlankToSelectOptionMap.putAll(
                    getBlankToOptionMap(
                        items = fillBlanksData.fillBlanks,
                        selectItemIndices = selectItemIndices,
                        selectOptions = selectOptions
                    )
                )
            }

            fillBlanksAdapter.addDelegate(
                FillBlanksSelectItemAdapterDelegate(fillBlanksData.options) { blankIndex ->
                    onSelectItemClick(
                        blankIndex = blankIndex,
                        items = fillBlanksAdapter.items,
                        selectItemIndices = selectItemIndices,
                        selectOptions = fillBlanksData.options
                    )
                }
            )

            wasSelectStateInitialized = true
        }
    }

    private fun getHighlightedSelectItemIndex(
        items: List<FillBlanksItem>,
        selectItemIndices: List<Int>
    ): Int? =
        selectItemIndices.firstOrNull { selectItemIndex ->
            val item = items.getOrNull(selectItemIndex) ?: return@firstOrNull false
            item is FillBlanksItem.Select && item.selectedOptionIndex == null
        }

    private fun getBlankToOptionMap(
        items: List<FillBlanksItem>,
        selectItemIndices: List<Int>,
        selectOptions: List<FillBlanksOption>
    ): Map<Int, Int> {
        val duplicatedOptionIndices = mutableMapOf<Int, Int>()
        return selectItemIndices
            .mapNotNull { selectItemIndex ->
                val selectedOptionIndex =
                    (items.getOrNull(selectItemIndex) as? FillBlanksItem.Select)?.selectedOptionIndex
                if (selectedOptionIndex != null) {
                    if (!duplicatedOptionIndices.contains(selectedOptionIndex)) {
                        duplicatedOptionIndices[selectedOptionIndex] = 1
                        selectItemIndex to selectedOptionIndex
                    } else {
                        val duplicatesCount = duplicatedOptionIndices[selectedOptionIndex] ?: 0
                        duplicatedOptionIndices[selectedOptionIndex] = duplicatesCount + 1
                        var actualSelectedOptionIndex: Int? = null
                        for (i in selectedOptionIndex + 1..selectOptions.lastIndex) {
                            val option = selectOptions[i]
                            val wrongIndexOption = selectOptions[selectedOptionIndex]
                            if (option.originalText == wrongIndexOption.originalText) {
                                actualSelectedOptionIndex = i
                            }
                        }
                        if (actualSelectedOptionIndex != null) {
                            selectItemIndex to actualSelectedOptionIndex
                        } else {
                            null
                        }
                    }
                } else {
                    null
                }
            }
            .toMap()
    }

    private fun resolve(
        currentResolveState: ResolveState,
        state: StepQuizFeature.StepQuizState.AttemptLoaded
    ): ResolveState =
        if (currentResolveState == ResolveState.NotResolved) {
            val dataset = state.attempt.dataset
            if (dataset != null) {
                try {
                    val mode = FillBlanksResolver.resolve(dataset)
                    ResolveState.ResolveSucceed(mode)
                } catch (e: InvalidFillBlanksConfigException) {
                    ResolveState.ResolveFailed
                }
            } else {
                ResolveState.ResolveFailed
            }
        } else {
            currentResolveState
        }

    private fun mapItems(
        fillBlanksData: FillBlanksData?,
        highlightedSelectItemIndex: Int?,
        isEnabled: Boolean
    ) =
        fillBlanksData?.fillBlanks?.mapIndexed { index, item ->
            when (item) {
                is FillBlanksItem.Input -> FillBlanksUiItem.Input(item, isEnabled)
                is FillBlanksItem.Select -> FillBlanksUiItem.Select(
                    origin = item,
                    isHighlighted = index == highlightedSelectItemIndex,
                    isEnabled = isEnabled
                )
                is FillBlanksItem.Text -> FillBlanksUiItem.Text(item)
            }
        } ?: emptyList()

    private fun mapBlankOptions(
        fillBlanksData: FillBlanksData?,
        usedOptionsIndices: List<Int>,
        isEnabled: Boolean
    ): List<FillBlanksSelectOptionUIItem> {
        return fillBlanksData?.options?.mapIndexed { index, option ->
            FillBlanksSelectOptionUIItem(
                option = option,
                isUsed = index in usedOptionsIndices,
                isClickable = isEnabled
            )
        } ?: emptyList()
    }

    private fun getSelectItemIndices(items: List<FillBlanksItem>?) =
        items?.mapIndexedNotNull { index, item ->
            if (item is FillBlanksItem.Select) index else null
        } ?: emptyList()

    private fun setupFillBlankItemsRecycler() {
        with(binding.stepQuizFillBlanksRecycler) {
            itemAnimator = null
            adapter = fillBlanksAdapter
            // Allow nested scrolling to avoid half rendered content.
            // Fixme
            isNestedScrollingEnabled = true
            layoutManager = FlexboxLayoutManager(context)
            addItemDecoration(
                FlexboxItemDecoration(context).apply {
                    setOrientation(FlexboxItemDecoration.HORIZONTAL)
                    setDrawable(
                        ContextCompat.getDrawable(context, R.drawable.bg_step_quiz_fill_blanks_item_vertical_divider)
                    )
                }
            )
        }
    }

    private fun setupOptionsRecycler(optionsBinding: LayoutStepQuizFillBlanksOptionsBinding) {
        fillBlanksOptionsAdapter = DefaultDelegateAdapter<FillBlanksSelectOptionUIItem>().apply {
            addDelegate(
                FillBlanksSelectOptionAdapterDelegate { selectedOptionIndex, option ->
                    onOptionClick(
                        selectedOptionIndex = selectedOptionIndex,
                        blankIndex = highlightedSelectItemIndex ?: return@FillBlanksSelectOptionAdapterDelegate,
                        selectedOption = option,
                        items = fillBlanksAdapter.items,
                        selectItemIndices = selectItemIndices,
                        selectOptions = selectOptions
                    )
                }
            )
        }
        with(optionsBinding.stepQuizFillBlanksOptionsRecycler) {
            itemAnimator = null
            adapter = fillBlanksOptionsAdapter
            isNestedScrollingEnabled = false
            layoutManager = FlexboxLayoutManager(context)
            val dividerDrawable =
                ContextCompat.getDrawable(context, R.drawable.bg_step_quiz_fill_blanks_options_vertical_divider)
            addItemDecoration(
                FlexboxItemDecoration(context).apply {
                    setOrientation(FlexboxItemDecoration.HORIZONTAL)
                    setDrawable(dividerDrawable)
                }
            )
            addItemDecoration(
                FlexboxItemDecoration(context).apply {
                    setOrientation(FlexboxItemDecoration.VERTICAL)
                    setDrawable(dividerDrawable)
                }
            )
        }
        optionsBinding.root.isVisible = true
        TransitionManager.beginDelayedTransition(optionsBinding.root)
    }

    override fun createReply(): Reply =
        createReplyInternal(
            items = fillBlanksAdapter.items,
            selectOptions = selectOptions
        )

    private fun createReplyInternal(
        items: List<FillBlanksUiItem>,
        selectOptions: List<FillBlanksOption> = emptyList()
    ): Reply =
        Reply.fillBlanks(
            blanks = items.mapNotNull { item ->
                when (item) {
                    is FillBlanksUiItem.Input -> item.origin.inputText
                    is FillBlanksUiItem.Text -> null
                    is FillBlanksUiItem.Select -> {
                        item.origin.selectedOptionIndex?.let { optionIndex ->
                            selectOptions.getOrNull(optionIndex)?.originalText
                        } ?: ""
                    }
                }
            }
        )

    fun onInputItemModified(index: Int, text: String) {
        val updatedItems = fillBlanksAdapter.items.mutate {
            val inputItem = get(index) as FillBlanksUiItem.Input
            set(index, inputItem.copy(inputText = text))
        }
        fillBlanksAdapter.items = updatedItems
        onQuizChanged(createReplyInternal(updatedItems))
    }

    private fun onInputItemClick(inputItemIndex: Int, text: String) {
        FillBlanksInputDialogFragment
            .newInstance(inputItemIndex, text)
            .showIfNotExists(fragmentManager, FillBlanksInputDialogFragment.TAG)
    }

    private fun onSelectItemClick(
        blankIndex: Int,
        items: List<FillBlanksUiItem>,
        selectItemIndices: List<Int>,
        selectOptions: List<FillBlanksOption>
    ) {
        val selectedItem = items.getOrNull(blankIndex) as? FillBlanksUiItem.Select ?: return
        val updatedItems = items.mutate {
            set(
                blankIndex,
                selectedItem.copy(selectedOptionIndex = null)
            )
        }

        val highlightResult = updateSelectItemsHighlighting(
            items = updatedItems,
            selectItemIndices = selectItemIndices,
            currentHighlightedSelectItemIndex = highlightedSelectItemIndex,
            clickedSelectItemIndex = blankIndex
        )
        this.highlightedSelectItemIndex = highlightResult.newHighlightedSelectItemIndex
        fillBlanksAdapter.items = highlightResult.updatedItems

        this.selectBlankToSelectOptionMap.remove(blankIndex)
        fillBlanksOptionsAdapter?.items =
            updateUsedOptions(
                items = fillBlanksOptionsAdapter?.items,
                usedOptionsIndices = selectBlankToSelectOptionMap.values.toList()
            )

        if (selectedItem.origin.selectedOptionIndex != null) {
            onQuizChanged(createReplyInternal(highlightResult.updatedItems, selectOptions))
        }
    }

    private fun updateUsedOptions(
        items: List<FillBlanksSelectOptionUIItem>?,
        usedOptionsIndices: List<Int>
    ): List<FillBlanksSelectOptionUIItem> =
        items?.mapIndexed { index, option ->
            option.copy(isUsed = index in usedOptionsIndices)
        } ?: emptyList()

    private fun onOptionClick(
        selectedOptionIndex: Int,
        blankIndex: Int,
        selectedOption: FillBlanksOption,
        items: List<FillBlanksUiItem>,
        selectItemIndices: List<Int>,
        selectOptions: List<FillBlanksOption>
    ) {
        val filledItems = fillNextBlank(
            selectedOptionIndex = selectedOptionIndex,
            blankIndex = blankIndex,
            items = items
        )
        val highlightResult = updateSelectItemsHighlighting(
            items = filledItems,
            selectItemIndices = selectItemIndices,
            currentHighlightedSelectItemIndex = highlightedSelectItemIndex
        )
        this.highlightedSelectItemIndex = highlightResult.newHighlightedSelectItemIndex
        this.selectBlankToSelectOptionMap[blankIndex] = selectedOptionIndex
        fillBlanksAdapter.items = highlightResult.updatedItems
        fillBlanksOptionsAdapter?.items = fillBlanksOptionsAdapter?.items?.mutate {
            val optionItem = getOrNull(selectedOptionIndex) ?: return
            set(
                selectedOptionIndex,
                optionItem.copy(option = selectedOption, isUsed = true)
            )
        } ?: emptyList()
        onQuizChanged(createReplyInternal(highlightResult.updatedItems, selectOptions))
    }

    private fun updateSelectItemsHighlighting(
        items: List<FillBlanksUiItem>,
        selectItemIndices: List<Int>,
        currentHighlightedSelectItemIndex: Int?,
        clickedSelectItemIndex: Int? = null
    ): HighlightResult {
        if (currentHighlightedSelectItemIndex == clickedSelectItemIndex) {
            return HighlightResult(
                updatedItems = items,
                newHighlightedSelectItemIndex = currentHighlightedSelectItemIndex
            )
        }
        val highlightedSelectItemIndex =
            clickedSelectItemIndex ?: selectItemIndices.firstOrNull { selectItemIndex ->
                val item = items.getOrNull(selectItemIndex)
                item is FillBlanksUiItem.Select && item.origin.selectedOptionIndex == null
            }
        return HighlightResult(
            newHighlightedSelectItemIndex = highlightedSelectItemIndex,
            updatedItems = if (highlightedSelectItemIndex != null) {
                val itemToHighlight =
                    items.getOrNull(highlightedSelectItemIndex) as? FillBlanksUiItem.Select
                if (itemToHighlight != null) {
                    items.mutate {
                        set(highlightedSelectItemIndex, itemToHighlight.copy(isHighlighted = true))
                        if (currentHighlightedSelectItemIndex != null) {
                            val currentHighlightedSelectItem =
                                getOrNull(currentHighlightedSelectItemIndex) as? FillBlanksUiItem.Select
                            if (currentHighlightedSelectItem != null) {
                                set(
                                    currentHighlightedSelectItemIndex,
                                    currentHighlightedSelectItem.copy(isHighlighted = false)
                                )
                            }
                        }
                    }
                } else {
                    items
                }
            } else {
                items
            }
        )
    }

    private fun fillNextBlank(
        selectedOptionIndex: Int,
        blankIndex: Int,
        items: List<FillBlanksUiItem>
    ): List<FillBlanksUiItem> =
        items.mutate {
            val selectItemToFill = get(blankIndex)
            if (selectItemToFill is FillBlanksUiItem.Select) {
                set(
                    blankIndex,
                    selectItemToFill.copy(selectedOptionIndex = selectedOptionIndex)
                )
            }
        }
}