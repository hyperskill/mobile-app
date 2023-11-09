package org.hyperskill.app.android.step_quiz_fill_blanks.delegate

import android.graphics.drawable.LayerDrawable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.LayerListDrawableDelegate
import org.hyperskill.app.android.databinding.ItemStepQuizFillBlanksSelectOptionBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizFillBlanksBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizFillBlanksOptionsBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_fill_blanks.dialog.FillBlanksInputDialogFragment
import org.hyperskill.app.android.step_quiz_fill_blanks.model.FillBlanksSelectOptionUIItem
import org.hyperskill.app.android.step_quiz_fill_blanks.model.FillBlanksUiItem
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksData
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksItem
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksMode
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksOption
import org.hyperskill.app.step_quiz_fill_blanks.model.InvalidFillBlanksConfigException
import org.hyperskill.app.step_quiz_fill_blanks.presentation.FillBlanksItemMapper
import org.hyperskill.app.step_quiz_fill_blanks.presentation.FillBlanksResolver
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.extension.setTextIfChanged
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.app.core.model.mutate

class FillBlanksStepQuizFormDelegate(
    private val binding: LayoutStepQuizFillBlanksBinding,
    private val optionsBinding: LayoutStepQuizFillBlanksOptionsBinding,
    private val fragmentManager: FragmentManager,
    private val onQuizChanged: (Reply) -> Unit
) : StepQuizFormDelegate {

    private val fillBlanksAdapter = DefaultDelegateAdapter<FillBlanksUiItem>().apply {
        addDelegate(textAdapterDelegate())
        addDelegate(
            inputAdapterDelegate(::onInputItemClick)
        )
    }

    private var fillBlanksOptionsAdapter: DefaultDelegateAdapter<FillBlanksSelectOptionUIItem>? = null

    private var fillBlanksMapper: FillBlanksItemMapper? = null

    private var resolveState: ResolveState = ResolveState.NotResolved

    private var selectItemIndices: List<Int> = emptyList()
    private var highlightedSelectItemIndex: Int? = null

    private val selectOptionLayers = listOf(
        R.id.step_quiz_fill_blanks_select_empty_layer,
        R.id.step_quiz_fill_blanks_select_filled_layer,
        R.id.step_quiz_fill_blanks_select_empty_selected_layer,
        R.id.step_quiz_fill_blanks_select_filled_selected_layer
    )

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

            fillBlanksData?.options?.let { options ->
                // addDelegate adds delegate only once under the hood
                fillBlanksAdapter.addDelegate(
                    selectAdapterDelegate(options) { blankIndex, selectedOptionIndex ->
                        onSelectItemClick(
                            blankIndex = blankIndex,
                            selectedOptionIndex = selectedOptionIndex,
                            items = fillBlanksAdapter.items,
                            selectItemIndices = selectItemIndices
                        )
                    }
                )
            }

            fillBlanksAdapter.items = mapItems(fillBlanksData, highlightedSelectItemIndex)
            binding.root.post { binding.stepQuizFillBlanksRecycler.requestLayout() }

            fillBlanksOptionsAdapter?.items = mapBlankOptions(fillBlanksData)
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
        highlightedSelectItemIndex: Int?
    ) =
        fillBlanksData?.fillBlanks?.mapIndexed { index, item ->
            when (item) {
                is FillBlanksItem.Input -> FillBlanksUiItem.Input(item)
                is FillBlanksItem.Select -> FillBlanksUiItem.Select(
                    origin = item,
                    isHighlighted = index == highlightedSelectItemIndex
                )
                is FillBlanksItem.Text -> FillBlanksUiItem.Text(item)
            }
        } ?: emptyList()

    private fun mapBlankOptions(
        fillBlanksData: FillBlanksData?
    ): List<FillBlanksSelectOptionUIItem> {
        val selectedOptionIndices =
            getSelectedOptionsIndices(fillBlanksData?.fillBlanks, selectItemIndices)
        return fillBlanksData?.options?.mapIndexed { index, option ->
            FillBlanksSelectOptionUIItem(
                option = option,
                isUsed = index in selectedOptionIndices
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
                optionAdapterDelegate { selectedOptionIndex, option ->
                    onOptionClick(
                        selectedOptionIndex = selectedOptionIndex,
                        blankIndex = highlightedSelectItemIndex ?: return@optionAdapterDelegate,
                        selectedOption = option,
                        items = fillBlanksAdapter.items,
                        selectItemIndices = selectItemIndices
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
        Reply.fillBlanks(
            blanks = fillBlanksAdapter.items.mapNotNull { item ->
                when (item) {
                    is FillBlanksUiItem.Input -> item.origin.inputText
                    is FillBlanksUiItem.Text -> null
                    is FillBlanksUiItem.Select -> {
                        // TODO: ALTAPPS-1021 provide reply blanks for select mode
                        null
                    }
                }
            }
        )

    fun onInputItemModified(index: Int, text: String) {
        fillBlanksAdapter.items = fillBlanksAdapter.items.mutate {
            val inputItem = get(index) as FillBlanksUiItem.Input
            set(index, inputItem.copy(inputText = text))
        }
        fillBlanksAdapter.notifyItemChanged(index)
        onQuizChanged(createReply())
    }

    private fun textAdapterDelegate() =
        adapterDelegate<FillBlanksUiItem, FillBlanksUiItem.Text>(R.layout.item_step_quiz_fill_blanks_text) {
            val textView = itemView as TextView
            onBind { textItem ->
                textView.updateLayoutParams<FlexboxLayoutManager.LayoutParams> {
                    isWrapBefore = textItem.origin.startsWithNewLine
                }
                textView.setTextIfChanged(
                    HtmlCompat.fromHtml(textItem.origin.text, HtmlCompat.FROM_HTML_MODE_COMPACT)
                )
            }
        }

    private fun inputAdapterDelegate(onClick: (index: Int, String) -> Unit) =
        adapterDelegate<FillBlanksUiItem, FillBlanksUiItem.Input>(R.layout.item_step_quiz_fill_blanks_input) {
            val textView = itemView as TextView
            textView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onClick(position, textView.text.toString())
                }
            }
            onBind { inputItem ->
                textView.setTextIfChanged(inputItem.origin.inputText ?: "")
            }
        }

    private fun onInputItemClick(index: Int, text: String) {
        FillBlanksInputDialogFragment
            .newInstance(index, text)
            .showIfNotExists(fragmentManager, FillBlanksInputDialogFragment.TAG)
    }

    private fun selectAdapterDelegate(
        options: List<FillBlanksOption>,
        onClick: (blankIndex: Int, selectedOptionIndex: Int?) -> Unit
    ) =
        adapterDelegate<FillBlanksUiItem, FillBlanksUiItem.Select>(R.layout.item_step_quiz_fill_blanks_select) {
            val textView = itemView as TextView
            val layerListDrawableDelegate = LayerListDrawableDelegate(
                selectOptionLayers,
                textView.background.mutate() as LayerDrawable
            )

            textView.setOnClickListener {
                val position = bindingAdapterPosition
                val selectedOptionIndex = item?.origin?.selectedOptionIndex
                if (position != RecyclerView.NO_POSITION) {
                    onClick(position, selectedOptionIndex)
                }
            }

            onBind { selectItem ->
                val selectedOptionIndex = selectItem.origin.selectedOptionIndex
                val text = if (selectedOptionIndex != null) {
                    options.getOrNull(selectedOptionIndex)?.displayText
                } else {
                    null
                }
                textView.setTextIfChanged(text ?: "")
                layerListDrawableDelegate.showLayer(
                    when {
                        selectItem.isHighlighted && text.isNullOrEmpty() ->
                            R.id.step_quiz_fill_blanks_select_empty_selected_layer
                        text.isNullOrEmpty() -> R.id.step_quiz_fill_blanks_select_empty_layer
                        else -> R.id.step_quiz_fill_blanks_select_filled_layer
                    }
                )
            }
        }

    private fun optionAdapterDelegate(
        onClick: (selectedOptionIndex: Int, option: FillBlanksOption) -> Unit
    ) =
        adapterDelegate<FillBlanksSelectOptionUIItem, FillBlanksSelectOptionUIItem>(
            R.layout.item_step_quiz_fill_blanks_select_option
        ) {
            val binding = ItemStepQuizFillBlanksSelectOptionBinding.bind(itemView)

            binding.stepQuizFillBlanksSelectOptionContainer.setOnClickListener {
                val position = bindingAdapterPosition
                val item = item
                if (position != RecyclerView.NO_POSITION && item != null) {
                    onClick(position, item.option)
                }
            }

            val layerListDrawableDelegate = LayerListDrawableDelegate(
                selectOptionLayers,
                binding.stepQuizFillBlanksSelectOptionContainer.background.mutate() as LayerDrawable
            )

            onBind { selectOption ->
                with(binding.stepQuizFillBlanksSelectOptionText) {
                    isInvisible = selectOption.isUsed
                    setTextIfChanged(selectOption.option.displayText)
                }
                binding.stepQuizFillBlanksSelectOptionContainer.isClickable = !selectOption.isUsed
                layerListDrawableDelegate.showLayer(
                    if (selectOption.isUsed) {
                        R.id.step_quiz_fill_blanks_select_empty_layer
                    } else {
                        R.id.step_quiz_fill_blanks_select_filled_layer
                    }
                )
            }
        }

    private fun onSelectItemClick(
        blankIndex: Int,
        selectedOptionIndex: Int?,
        items: List<FillBlanksUiItem>,
        selectItemIndices: List<Int>
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
                set(selectedOptionIndex, get(selectedOptionIndex).copy(isUsed = false))
            } ?: emptyList()
        }
    }

    private fun onOptionClick(
        selectedOptionIndex: Int,
        blankIndex: Int,
        selectedOption: FillBlanksOption,
        items: List<FillBlanksUiItem>,
        selectItemIndices: List<Int>,
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
            set(
                selectedOptionIndex,
                FillBlanksSelectOptionUIItem(selectedOption, isUsed = true)
            )
        } ?: emptyList()
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

    private sealed class ResolveState {
        object NotResolved : ResolveState()
        data class ResolveSucceed(val mode: FillBlanksMode) : ResolveState()
        object ResolveFailed : ResolveState()
    }

    private class HighlightResult(
        val updatedItems: List<FillBlanksUiItem>,
        val newHighlightedSelectItemIndex: Int?
    )
}
