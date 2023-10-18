package org.hyperskill.app.android.step_quiz_fill_blanks.delegate

import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutStepQuizFillBlanksBindingBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_fill_blanks.dialog.FillBlanksInputDialogFragment
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksItem
import org.hyperskill.app.step_quiz_fill_blanks.model.InvalidFillBlanksConfigException
import org.hyperskill.app.step_quiz_fill_blanks.presentation.FillBlanksItemMapper
import org.hyperskill.app.step_quiz_fill_blanks.presentation.FillBlanksResolver
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.extension.setTextIfChanged
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.app.core.model.mutate

class FillBlanksStepQuizFormDelegate(
    private val binding: LayoutStepQuizFillBlanksBindingBinding,
    private val fragmentManager: FragmentManager,
    private val onQuizChanged: (Reply) -> Unit
) : StepQuizFormDelegate {

    private val fillBlanksAdapter = DefaultDelegateAdapter<FillBlanksItem>().apply {
        addDelegate(textAdapterDelegate())
        addDelegate(
            inputAdapterDelegate(::onInputItemClick)
        )
    }

    private var resolveState: ResolveState = ResolveState.NOT_RESOLVED

    init {
        with(binding.stepQuizFillBlanksRecycler) {
            itemAnimator = null
            adapter = fillBlanksAdapter
            isNestedScrollingEnabled = false
            layoutManager = FlexboxLayoutManager(context)
        }
    }

    override fun setState(state: StepQuizFeature.StepQuizState.AttemptLoaded) {
        val resolveState = resolve(resolveState, state)
        this.resolveState = resolveState
        if (resolveState == ResolveState.RESOLVE_SUCCEED) {
            val fillBlanksData = FillBlanksItemMapper.map(
                state.attempt,
                (state.submissionState as? StepQuizFeature.SubmissionState.Loaded)?.submission
            )
            fillBlanksAdapter.items = fillBlanksData?.fillBlanks ?: emptyList()
            binding.root.post { binding.stepQuizFillBlanksRecycler.requestLayout() }
        }
    }

    private fun resolve(
        currentResolveState: ResolveState,
        state: StepQuizFeature.StepQuizState.AttemptLoaded
    ): ResolveState =
        if (currentResolveState == ResolveState.NOT_RESOLVED) {
            val dataset = state.attempt.dataset
            if (dataset != null) {
                try {
                    FillBlanksResolver.resolve(dataset)
                    ResolveState.RESOLVE_SUCCEED
                } catch (e: InvalidFillBlanksConfigException) {
                    ResolveState.RESOLVE_FAILED
                }
            } else {
                ResolveState.RESOLVE_FAILED
            }
        } else {
            currentResolveState
        }

    override fun createReply(): Reply =
        Reply.fillBlanks(
            blanks = fillBlanksAdapter.items.mapNotNull { item ->
                when (item) {
                    is FillBlanksItem.Input -> item.inputText
                    is FillBlanksItem.Text -> null
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
                textView.setTextIfChanged(textItem.text)
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

    private enum class ResolveState {
        NOT_RESOLVED,
        RESOLVE_SUCCEED,
        RESOLVE_FAILED
    }
}