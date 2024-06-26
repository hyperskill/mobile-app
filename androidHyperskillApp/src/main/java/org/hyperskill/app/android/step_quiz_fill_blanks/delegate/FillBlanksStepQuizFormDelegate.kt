package org.hyperskill.app.android.step_quiz_fill_blanks.delegate

import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutStepQuizFillBlanksBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_fill_blanks.adapter.FillBlanksInputItemAdapterDelegate
import org.hyperskill.app.android.step_quiz_fill_blanks.adapter.FillBlanksSelectItemAdapterDelegate
import org.hyperskill.app.android.step_quiz_fill_blanks.adapter.FillBlanksSelectOptionAdapterDelegate
import org.hyperskill.app.android.step_quiz_fill_blanks.adapter.FillBlanksTextItemAdapterDelegate
import org.hyperskill.app.android.step_quiz_fill_blanks.dialog.FillBlanksInputDialogFragment
import org.hyperskill.app.android.step_quiz_fill_blanks.model.FillBlanksProcessedOption
import org.hyperskill.app.android.step_quiz_fill_blanks.model.FillBlanksSelectOptionUIItem
import org.hyperskill.app.android.step_quiz_fill_blanks.model.FillBlanksUiItem
import org.hyperskill.app.android.step_quiz_fill_blanks.model.HighlightResult
import org.hyperskill.app.android.step_quiz_fill_blanks.model.ResolveState
import org.hyperskill.app.android.step_quiz_fill_blanks.model.SelectState
import org.hyperskill.app.core.utils.mutate
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import org.hyperskill.app.step_quiz.presentation.submission
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksData
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksItem
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksMode
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksOption
import org.hyperskill.app.step_quiz_fill_blanks.model.InvalidFillBlanksConfigException
import org.hyperskill.app.step_quiz_fill_blanks.presentation.FillBlanksItemMapper
import org.hyperskill.app.step_quiz_fill_blanks.presentation.FillBlanksResolver
import org.hyperskill.app.submissions.domain.model.Reply
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.app.core.model.mutate

class FillBlanksStepQuizFormDelegate(
    private val binding: LayoutStepQuizFillBlanksBinding,
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

    private var selectState: SelectState? = null

    init {
        setupFillBlankItemsRecycler()
    }

    override fun setState(state: StepQuizFeature.StepQuizState.AttemptLoaded) {
        val resolveState = resolve(resolveState, state)
        this.resolveState = resolveState
        if (resolveState is ResolveState.ResolveSucceed) {
            render(resolveState, state, selectState)
        }
    }

    private fun render(
        resolveState: ResolveState.ResolveSucceed,
        state: StepQuizFeature.StepQuizState.AttemptLoaded,
        selectState: SelectState?
    ) {
        if (fillBlanksMapper == null) {
            fillBlanksMapper = FillBlanksItemMapper(mode = resolveState.mode)
        }

        if (resolveState.mode == FillBlanksMode.SELECT && selectState == null) {
            setupOptionsRecycler()
        }

        val fillBlanksData = fillBlanksMapper?.map(
            attempt = state.attempt,
            submission = state.submission
        )

        val actualSelectState = getSelectState(resolveState, fillBlanksData, selectState)
        this.selectState = actualSelectState

        val isEnabled = StepQuizResolver.isQuizEnabled(state)
        setFillBlanksItems(
            fillBlanksData = fillBlanksData,
            highlightedSelectItemIndex = actualSelectState?.highlightedSelectItemIndex,
            isEnabled = isEnabled
        )
        if (actualSelectState != null) {
            setOptionsItems(
                options = actualSelectState.options,
                selectBlankToSelectOptionMap = actualSelectState.selectBlankToSelectOptionMap,
                isEnabled = isEnabled
            )
        }
    }

    private fun getSelectState(
        resolveState: ResolveState.ResolveSucceed,
        fillBlanksData: FillBlanksData?,
        selectState: SelectState?
    ): SelectState? =
        when {
            resolveState.mode != FillBlanksMode.SELECT || fillBlanksData == null -> null
            selectState == null -> {
                val newSelectState = initSelectState(fillBlanksData)
                addFillBlanksSelectAdapterDelegate(newSelectState.options)
                newSelectState
            }
            isAllSelectItemsEmpty(fillBlanksData.fillBlanks, selectState.selectItemIndices) -> {
                selectState.copy(
                    selectBlankToSelectOptionMap = emptyMap(),
                    highlightedSelectItemIndex = getHighlightedSelectItemIndex(
                        items = fillBlanksData.fillBlanks,
                        selectItemIndices = selectState.selectItemIndices
                    )
                )
            }
            else -> selectState
        }

    private fun initSelectState(fillBlanksData: FillBlanksData): SelectState {
        val selectItemIndices = getSelectItemIndices(fillBlanksData.fillBlanks)
        val options = processOptions(fillBlanksData.options)
        return SelectState(
            options = options,
            selectBlankToSelectOptionMap = getBlankToOptionMap(
                items = fillBlanksData.fillBlanks,
                selectItemIndices = selectItemIndices,
                selectOptions = options
            ),
            selectItemIndices = selectItemIndices,
            highlightedSelectItemIndex = getHighlightedSelectItemIndex(
                items = fillBlanksData.fillBlanks,
                selectItemIndices = selectItemIndices
            )
        )
    }

    private fun addFillBlanksSelectAdapterDelegate(options: List<FillBlanksProcessedOption>) {
        fillBlanksAdapter.addDelegate(
            FillBlanksSelectItemAdapterDelegate(options) { blankIndex ->
                selectState?.let {
                    onSelectItemClick(
                        blankIndex = blankIndex,
                        items = fillBlanksAdapter.items,
                        selectState = it
                    )
                }
            }
        )
    }

    private fun processOptions(options: List<FillBlanksOption>) =
        options.map {
            FillBlanksProcessedOption(
                originalText = it.originalText,
                displayText = HtmlCompat.fromHtml(it.displayText, HtmlCompat.FROM_HTML_MODE_COMPACT)
            )
        }

    private fun setOptionsItems(
        options: List<FillBlanksProcessedOption>,
        selectBlankToSelectOptionMap: Map<Int, Int>,
        isEnabled: Boolean
    ) {
        fillBlanksOptionsAdapter?.items = mapBlankOptions(
            options = options,
            usedOptionsIndices = selectBlankToSelectOptionMap.values.toList(),
            isEnabled = isEnabled
        )
        binding.root.post { binding.stepQuizFillBlanksOptionsRecycler.requestLayout() }
    }

    private fun mapBlankOptions(
        options: List<FillBlanksProcessedOption>,
        usedOptionsIndices: List<Int>,
        isEnabled: Boolean
    ): List<FillBlanksSelectOptionUIItem> =
        options.mapIndexed { index, option ->
            FillBlanksSelectOptionUIItem(
                id = index,
                option = option,
                isUsed = index in usedOptionsIndices,
                isClickable = isEnabled
            )
        }

    private fun setFillBlanksItems(
        fillBlanksData: FillBlanksData?,
        highlightedSelectItemIndex: Int?,
        isEnabled: Boolean
    ) {
        fillBlanksAdapter.items = mapFillBlanksUiItems(
            items = fillBlanksData?.fillBlanks ?: emptyList(),
            highlightedSelectItemIndex = highlightedSelectItemIndex,
            isEnabled = isEnabled
        )
    }

    private fun mapFillBlanksUiItems(
        items: List<FillBlanksItem>,
        highlightedSelectItemIndex: Int?,
        isEnabled: Boolean
    ): List<FillBlanksUiItem> =
        items.mapIndexed { index, item ->
            when (item) {
                is FillBlanksItem.Input -> FillBlanksUiItem.Input(item, isEnabled)
                is FillBlanksItem.Select -> FillBlanksUiItem.Select(
                    origin = item,
                    isHighlighted = index == highlightedSelectItemIndex,
                    isEnabled = isEnabled
                )
                is FillBlanksItem.Text -> FillBlanksUiItem.Text(item)
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

    private fun isAllSelectItemsEmpty(
        items: List<FillBlanksItem>,
        selectItemIndices: List<Int>
    ): Boolean =
        selectItemIndices.all { selectItemIndex ->
            (items.getOrNull(selectItemIndex) as? FillBlanksItem.Select)
                ?.selectedOptionIndex == null
        }

    private fun getBlankToOptionMap(
        items: List<FillBlanksItem>,
        selectItemIndices: List<Int>,
        selectOptions: List<FillBlanksProcessedOption>
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
                } catch (_: InvalidFillBlanksConfigException) {
                    ResolveState.ResolveFailed
                }
            } else {
                ResolveState.ResolveFailed
            }
        } else {
            currentResolveState
        }

    private fun getSelectItemIndices(items: List<FillBlanksItem>?) =
        items?.mapIndexedNotNull { index, item ->
            if (item is FillBlanksItem.Select) index else null
        } ?: emptyList()

    private fun setupFillBlankItemsRecycler() {
        with(binding.stepQuizFillBlanksRecycler) {
            itemAnimator = null
            adapter = fillBlanksAdapter
            isNestedScrollingEnabled = false
            layoutManager = FlexboxLayoutManager(context)
                .apply { justifyContent = JustifyContent.FLEX_START }
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

    private fun setupOptionsRecycler() {
        fillBlanksOptionsAdapter = getFillBlanksOptionsAdapter()
        with(binding.stepQuizFillBlanksOptionsRecycler) {
            itemAnimator = null
            adapter = fillBlanksOptionsAdapter
            isNestedScrollingEnabled = false
            layoutManager = FlexboxLayoutManager(context)
                .apply { justifyContent = JustifyContent.FLEX_START }
            isVisible = true
        }
    }

    private fun getFillBlanksOptionsAdapter(): DefaultDelegateAdapter<FillBlanksSelectOptionUIItem> =
        DefaultDelegateAdapter<FillBlanksSelectOptionUIItem>().apply {
            addDelegate(
                FillBlanksSelectOptionAdapterDelegate { selectedOptionIndex, option ->
                    selectState?.let {
                        onOptionClick(
                            selectedOptionIndex = selectedOptionIndex,
                            blankIndex = it.highlightedSelectItemIndex ?: return@FillBlanksSelectOptionAdapterDelegate,
                            selectedOption = option,
                            items = fillBlanksAdapter.items,
                            selectState = it
                        )
                    }
                }
            )
        }

    override fun createReply(): Reply =
        createReplyInternal(
            items = fillBlanksAdapter.items,
            selectOptions = selectState?.options ?: emptyList()
        )

    private fun createReplyInternal(
        items: List<FillBlanksUiItem>,
        selectOptions: List<FillBlanksProcessedOption>? = null
    ): Reply =
        Reply.fillBlanks(
            blanks = items.mapNotNull { item ->
                when (item) {
                    is FillBlanksUiItem.Input -> item.origin.inputText
                    is FillBlanksUiItem.Text -> null
                    is FillBlanksUiItem.Select -> {
                        item.origin.selectedOptionIndex?.let { optionIndex ->
                            selectOptions?.getOrNull(optionIndex)?.originalText
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
        selectState: SelectState
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
            selectItemIndices = selectState.selectItemIndices,
            currentHighlightedSelectItemIndex = selectState.highlightedSelectItemIndex,
            clickedSelectItemIndex = blankIndex
        )

        this.selectState = selectState.copy(
            highlightedSelectItemIndex = highlightResult.newHighlightedSelectItemIndex,
            selectBlankToSelectOptionMap = selectState.selectBlankToSelectOptionMap.mutate {
                remove(blankIndex)
            }
        )

        fillBlanksAdapter.items = highlightResult.updatedItems
        fillBlanksOptionsAdapter?.items =
            updateUsedOptions(
                items = fillBlanksOptionsAdapter?.items,
                usedOptionsIndices = selectState.selectBlankToSelectOptionMap.values.toList()
            )

        if (selectedItem.origin.selectedOptionIndex != null) {
            onQuizChanged(
                createReplyInternal(
                    items = highlightResult.updatedItems,
                    selectOptions = selectState.options
                )
            )
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
        selectedOption: FillBlanksProcessedOption,
        items: List<FillBlanksUiItem>,
        selectState: SelectState
    ) {
        val filledItems = fillNextBlank(
            selectedOptionIndex = selectedOptionIndex,
            blankIndex = blankIndex,
            items = items
        )
        val highlightResult = updateSelectItemsHighlighting(
            items = filledItems,
            selectItemIndices = selectState.selectItemIndices,
            currentHighlightedSelectItemIndex = selectState.highlightedSelectItemIndex
        )

        this.selectState = selectState.copy(
            highlightedSelectItemIndex = highlightResult.newHighlightedSelectItemIndex,
            selectBlankToSelectOptionMap = selectState.selectBlankToSelectOptionMap.mutate {
                set(blankIndex, selectedOptionIndex)
            }
        )

        fillBlanksAdapter.items = highlightResult.updatedItems
        fillBlanksOptionsAdapter?.let {
            it.items = markOptionAsUsed(it.items, selectedOptionIndex, selectedOption)
        }

        onQuizChanged(createReplyInternal(highlightResult.updatedItems, selectState.options))
    }

    private fun markOptionAsUsed(
        options: List<FillBlanksSelectOptionUIItem>,
        selectedOptionIndex: Int,
        selectedOption: FillBlanksProcessedOption
    ): List<FillBlanksSelectOptionUIItem> =
        options.mutate {
            val optionItem = getOrNull(selectedOptionIndex) ?: return options
            set(
                selectedOptionIndex,
                optionItem.copy(option = selectedOption, isUsed = true)
            )
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