package org.hyperskill.app.android.step_quiz_parsons.view.adapter

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.presentation.highlight.prettify.PrettifyParser
import org.hyperskill.app.android.code.presentation.model.extensionForLanguage
import org.hyperskill.app.android.code.view.applyPrettifyParseResults
import org.hyperskill.app.android.code.view.model.themes.CodeTheme
import org.hyperskill.app.android.core.extensions.DefaultTapUpListener
import org.hyperskill.app.android.databinding.ItemStepQuizParsonsLineBinding
import org.hyperskill.app.android.step_quiz_parsons.view.model.UiParsonsLine
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class ParsonsLinesAdapterDelegate(
    private val codeTheme: CodeTheme,
    private val onLineClick: (Int) -> Unit
) : AdapterDelegate<UiParsonsLine, DelegateViewHolder<UiParsonsLine>>() {

    override fun isForViewType(position: Int, data: UiParsonsLine): Boolean =
        true

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<UiParsonsLine> =
        ViewHolder(createView(parent, R.layout.item_step_quiz_parsons_line))

    private val prettifyParser: PrettifyParser = PrettifyParser()

    @SuppressLint("ClickableViewAccessibility")
    private inner class ViewHolder(root: View) : DelegateViewHolder<UiParsonsLine>(root) {
        private val viewBinding: ItemStepQuizParsonsLineBinding by viewBinding(ItemStepQuizParsonsLineBinding::bind)

        private val selectedBackground =
            ContextCompat.getDrawable(context, R.drawable.step_quiz_parsons_line_selected_background)
        private val notSelectedBackground =
            ContextCompat.getDrawable(context, R.drawable.step_quiz_parsons_line_background)

        private val tabString = context.getString(R.string.step_quiz_parsons_tab)

        private val gestureDetector = GestureDetector(
            context,
            object : DefaultTapUpListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onLineClick(position)
                    }
                    return false
                }
            }
        )

        private val onTouchListener = View.OnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false
        }

        init {
            with(viewBinding.root) {
                isVerticalScrollBarEnabled = false
                isHorizontalScrollBarEnabled = false
            }
        }

        override fun onBind(data: UiParsonsLine) {
            with(viewBinding) {
                // OnTouchListener is used instead of onClickListener
                // because horizontalScrollView doesn't trigger onClick listener
                root.setOnTouchListener(if (data.isClickable) onTouchListener else null)
                stepQuizParsonsLineTabs.text = buildString {
                    repeat(data.tabsCount) {
                        append(tabString)
                    }
                }
                if (stepQuizParsonsLineTextView.text.toString() != data.formattedText.toString()) {
                    stepQuizParsonsLineTextView.text =
                        data.formattedText.applyPrettifyParseResults(
                            prettifyParseResults = prettifyParser.parse(
                                extensionForLanguage(data.langName),
                                data.originText
                            ),
                            start = 0,
                            end = data.formattedText.lastIndex,
                            theme = codeTheme
                        )
                }
                root.background = if (data.isSelected) {
                    selectedBackground
                } else {
                    notSelectedBackground
                }
            }
        }
    }
}