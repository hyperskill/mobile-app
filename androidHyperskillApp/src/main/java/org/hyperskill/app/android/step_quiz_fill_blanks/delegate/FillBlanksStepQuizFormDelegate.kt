package org.hyperskill.app.android.step_quiz_fill_blanks.delegate

import android.widget.TextView
import androidx.core.view.updateLayoutParams
import com.google.android.flexbox.FlexboxLayoutManager
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutStepQuizFillBlanksBindingBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksItem
import org.hyperskill.app.step_quiz_fill_blanks.model.InvalidFillBlanksConfigException
import org.hyperskill.app.step_quiz_fill_blanks.presentation.FillBlanksItemMapper
import org.hyperskill.app.step_quiz_fill_blanks.presentation.FillBlanksResolver
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class FillBlanksStepQuizFormDelegate(
    private val binding: LayoutStepQuizFillBlanksBindingBinding
) : StepQuizFormDelegate {

    private val fillBlanksAdapter = DefaultDelegateAdapter<FillBlanksItem>().apply {
        addDelegate(textAdapterDelegate())
        addDelegate(
            inputAdapterDelegate { _, _ -> }
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
        resolve(state)
        if (resolveState == ResolveState.RESOLVE_SUCCEED) {
            val fillBlanksData = FillBlanksItemMapper.map(
                state.attempt,
                (state.submissionState as? StepQuizFeature.SubmissionState.Loaded)?.submission
            )
            fillBlanksAdapter.items = fillBlanksData?.fillBlanks ?: emptyList()
            binding.root.post { binding.stepQuizFillBlanksRecycler.requestLayout() }
        }
    }

    private fun resolve(state: StepQuizFeature.StepQuizState.AttemptLoaded) {
        if (resolveState == ResolveState.NOT_RESOLVED) {
            val dataset = state.attempt.dataset
            resolveState = if (dataset != null) {
                try {
                    FillBlanksResolver.resolve(dataset)
                    ResolveState.RESOLVE_SUCCEED
                } catch (e: InvalidFillBlanksConfigException) {
                    ResolveState.RESOLVE_FAILED
                }
            } else {
                ResolveState.RESOLVE_FAILED
            }
        }
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

    private fun inputAdapterDelegate(onClick: (Int, String) -> Unit) =
        adapterDelegate<FillBlanksItem, FillBlanksItem.Input>(R.layout.item_step_quiz_fill_blanks_input) {
            val textView = itemView as TextView
            onBind { inputItem ->
                textView.setTextIfChanged(inputItem.inputText ?: "")
            }
        }

    private enum class ResolveState {
        NOT_RESOLVED,
        RESOLVE_SUCCEED,
        RESOLVE_FAILED
    }
}