package org.hyperskill.app.android.step_quiz_parsons.view.adapter

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.DefaultTapUpListener
import org.hyperskill.app.android.databinding.ItemStepQuizParsonsLineBinding
import org.hyperskill.app.android.step_quiz_parsons.view.model.UiParsonsLine
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class ParsonsLinesAdapterDelegate(
    @ColorInt private val codeTextColor: Int,
    private val onLineClick: (Int) -> Unit
) : AdapterDelegate<UiParsonsLine, DelegateViewHolder<UiParsonsLine>>() {

    override fun isForViewType(position: Int, data: UiParsonsLine): Boolean =
        true

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<UiParsonsLine> =
        ViewHolder(createView(parent, R.layout.item_step_quiz_parsons_line))

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
            viewBinding.stepQuizParsonsLineTextView.setTextColor(codeTextColor)
        }

        override fun onBind(data: UiParsonsLine) {
            with(viewBinding) {
                root.setOnTouchListener(if (data.isClickable) onTouchListener else null)
                stepQuizParsonsLineTabs.text = buildString {
                    repeat(data.tabsCount) {
                        append(tabString)
                    }
                }
                stepQuizParsonsLineTextView.text = data.text
                root.background = if (data.isSelected) {
                    selectedBackground
                } else {
                    notSelectedBackground
                }
            }
        }
    }
}