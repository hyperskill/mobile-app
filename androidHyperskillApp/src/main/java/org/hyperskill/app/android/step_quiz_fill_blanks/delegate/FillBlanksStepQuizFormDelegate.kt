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

    private var selectOptions: List<FillBlanksOption>? = null

    private var selectItemIndices: List<Int> = emptyList()
    private var highlightedSelectItemIndex: Int? = null

    init {
        with(binding.stepQuizFillBlanksRecycler) {
            itemAnimator = null
            adapter = fillBlanksAdapter
            isNestedScrollingEnabled = false
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

            if (resolveState.mode == FillBlanksMode.SELECT && selectItemIndices.isEmpty()) {
                val selectItemIndices = getSelectItemIndices(fillBlanksData?.fillBlanks)
                this.selectItemIndices = selectItemIndices
                highlightedSelectItemIndex =
                    fillBlanksData?.fillBlanks?.let { items ->
                        selectItemIndices.firstOrNull { selectItemIndex ->
                            val item = items.getOrNull(selectItemIndex) ?: return@firstOrNull false
                            item is FillBlanksItem.Select && item.selectedOptionIndex == null
                        }
                    }
            }

            this.selectOptions = fillBlanksData?.options
            fillBlanksData?.options?.let { options ->
                // addDelegate adds delegate only once under the hood
                fillBlanksAdapter.addDelegate(
                    FillBlanksSelectItemAdapterDelegate(options) { blankIndex, selectedOptionIndex ->
                        onSelectItemClick(
                            blankIndex = blankIndex,
                            selectedOptionIndex = selectedOptionIndex,
                            items = fillBlanksAdapter.items,
                            selectItemIndices = selectItemIndices,
                            selectOptions = selectOptions
                        )
                    }
                )
            }

            val isEnabled = StepQuizResolver.isQuizEnabled(state)

            fillBlanksAdapter.items = mapItems(fillBlanksData, highlightedSelectItemIndex, isEnabled)
            binding.root.post { binding.stepQuizFillBlanksRecycler.requestLayout() }

            fillBlanksOptionsAdapter?.items = mapBlankOptions(fillBlanksData, isEnabled)
        }
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
        isEnabled: Boolean
    ): List<FillBlanksSelectOptionUIItem> {
        val selectedOptionIndices =
            getSelectedOptionsIndices(fillBlanksData?.fillBlanks, selectItemIndices)
        return fillBlanksData?.options?.mapIndexed { index, option ->
            FillBlanksSelectOptionUIItem(
                option = option,
                isUsed = index in selectedOptionIndices,
                isClickable = isEnabled
            )
        } ?: emptyList()
    }

    private fun getSelectedOptionsIndices(
        items: List<FillBlanksItem>?,
        selectItemIndices: List<Int>
    ): List<Int> =
        items?.let {
            selectItemIndices.mapNotNull { selectItemIndex ->
                (items.getOrNull(selectItemIndex) as? FillBlanksItem.Select)?.selectedOptionIndex
            }
        } ?: emptyList()

    private fun getSelectItemIndices(items: List<FillBlanksItem>?) =
        items?.mapIndexedNotNull { index, item ->
            if (item is FillBlanksItem.Select) index else null
        } ?: emptyList()

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
        selectOptions: List<FillBlanksOption>? = null
    ): Reply =
        Reply.fillBlanks(
            blanks = items.mapNotNull { item ->
                when (item) {
                    is FillBlanksUiItem.Input -> item.origin.inputText
                    is FillBlanksUiItem.Text -> null
                    is FillBlanksUiItem.Select -> {
                        selectOptions?.let {
                            item.origin.selectedOptionIndex?.let { optionIndex ->
                                selectOptions.getOrNull(optionIndex)?.originalText
                            }
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
        selectedOptionIndex: Int?,
        items: List<FillBlanksUiItem>,
        selectItemIndices: List<Int>,
        selectOptions: List<FillBlanksOption>?
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

        if (selectedOptionIndex != null) {
            fillBlanksOptionsAdapter?.items = fillBlanksOptionsAdapter?.items?.mutate {
                val optionItem = getOrNull(selectedOptionIndex) ?: return@mutate
                set(selectedOptionIndex, optionItem.copy(isUsed = false))
            } ?: emptyList()
        }

        if (selectedItem.origin.selectedOptionIndex != null) {
            onQuizChanged(createReplyInternal(highlightResult.updatedItems, selectOptions))
        }
    }

    private fun onOptionClick(
        selectedOptionIndex: Int,
        blankIndex: Int,
        selectedOption: FillBlanksOption,
        items: List<FillBlanksUiItem>,
        selectItemIndices: List<Int>,
        selectOptions: List<FillBlanksOption>?
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