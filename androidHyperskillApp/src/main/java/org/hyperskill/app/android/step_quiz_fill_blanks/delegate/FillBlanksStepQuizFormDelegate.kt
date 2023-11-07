package org.hyperskill.app.android.step_quiz_fill_blanks.delegate

import android.graphics.drawable.LayerDrawable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.LayerListDrawableDelegate
import org.hyperskill.app.android.databinding.LayoutStepQuizFillBlanksBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizFillBlanksOptionsBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_fill_blanks.dialog.FillBlanksInputDialogFragment
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
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

    private val fillBlanksAdapter = DefaultDelegateAdapter<FillBlanksItem>().apply {
        addDelegate(textAdapterDelegate())
        addDelegate(
            inputAdapterDelegate(::onInputItemClick)
        )
    }

    private var fillBlanksOptionsAdapter: DefaultDelegateAdapter<FillBlanksOption>? = null

    private var fillBlanksMapper: FillBlanksItemMapper? = null

    private var resolveState: ResolveState = ResolveState.NotResolved

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

            fillBlanksAdapter.items = fillBlanksData?.fillBlanks ?: emptyList()
            binding.root.post { binding.stepQuizFillBlanksRecycler.requestLayout() }

            fillBlanksOptionsAdapter?.items = fillBlanksData?.options ?: emptyList()
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

    private fun setupOptionsRecycler(optionsBinding: LayoutStepQuizFillBlanksOptionsBinding) {
        fillBlanksOptionsAdapter = DefaultDelegateAdapter<FillBlanksOption>().apply {
            addDelegate(optionAdapterDelegate())
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
                    is FillBlanksItem.Input -> item.inputText
                    is FillBlanksItem.Text -> null
                    is FillBlanksItem.Select -> {
                        // TODO: ALTAPPS-1021 provide reply blanks for select mode
                        null
                    }
                }
            }
        )

    fun onInputItemModified(index: Int, text: String) {
        fillBlanksAdapter.items = fillBlanksAdapter.items.mutate {
            val inputItem = get(index) as FillBlanksItem.Input
            set(index, inputItem.copy(inputText = text))
        }
        fillBlanksAdapter.notifyItemChanged(index)
        onQuizChanged(createReply())
    }

    private fun textAdapterDelegate() =
        adapterDelegate<FillBlanksItem, FillBlanksItem.Text>(R.layout.item_step_quiz_fill_blanks_text) {
            val textView = itemView as TextView
            onBind { textItem ->
                textView.updateLayoutParams<FlexboxLayoutManager.LayoutParams> {
                    isWrapBefore = textItem.startsWithNewLine
                }
                textView.setTextIfChanged(
                    HtmlCompat.fromHtml(textItem.text, HtmlCompat.FROM_HTML_MODE_COMPACT)
                )
            }
        }

    private fun inputAdapterDelegate(onClick: (index: Int, String) -> Unit) =
        adapterDelegate<FillBlanksItem, FillBlanksItem.Input>(R.layout.item_step_quiz_fill_blanks_input) {
            val textView = itemView as TextView
            textView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onClick(position, textView.text.toString())
                }
            }
            onBind { inputItem ->
                textView.setTextIfChanged(inputItem.inputText ?: "")
            }
        }

    private fun onInputItemClick(index: Int, text: String) {
        FillBlanksInputDialogFragment
            .newInstance(index, text)
            .showIfNotExists(fragmentManager, FillBlanksInputDialogFragment.TAG)
    }

    private fun optionAdapterDelegate() =
        adapterDelegate<FillBlanksOption, FillBlanksOption>(R.layout.item_step_quiz_fill_blanks_select) {
            val textView = itemView as TextView
            val layerListDrawableDelegate = LayerListDrawableDelegate(
                listOf(
                    R.id.step_quiz_fill_blanks_select_empty_layer,
                    R.id.step_quiz_fill_blanks_select_filled_layer,
                    R.id.step_quiz_fill_blanks_select_empty_selected_layer,
                    R.id.step_quiz_fill_blanks_select_filled_selected_layer
                ),
                textView.background.mutate() as LayerDrawable
            )
            onBind { selectOption ->
                textView.text = selectOption.displayText
                layerListDrawableDelegate.showLayer(R.id.step_quiz_fill_blanks_select_filled_layer)
            }
        }

    private sealed class ResolveState {
        object NotResolved : ResolveState()
        data class ResolveSucceed(val mode: FillBlanksMode) : ResolveState()
        object ResolveFailed : ResolveState()
    }
}