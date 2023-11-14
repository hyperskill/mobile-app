package org.hyperskill.app.android.step_quiz_fill_blanks.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.view.updateLayoutParams
import com.google.android.flexbox.FlexboxLayoutManager
import org.hyperskill.app.android.R
import org.hyperskill.app.android.step_quiz_fill_blanks.model.FillBlanksUiItem
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class FillBlanksTextItemAdapterDelegate : AdapterDelegate<FillBlanksUiItem, DelegateViewHolder<FillBlanksUiItem>>() {

    override fun isForViewType(position: Int, data: FillBlanksUiItem): Boolean =
        data is FillBlanksUiItem.Text

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<FillBlanksUiItem> =
        ViewHolder(createView(parent, R.layout.item_step_quiz_fill_blanks_text))

    private class ViewHolder(root: View) : DelegateViewHolder<FillBlanksUiItem>(root) {
        private val textView = itemView as TextView

        override fun onBind(data: FillBlanksUiItem) {
            val textItem = data as FillBlanksUiItem.Text
            textView.updateLayoutParams<FlexboxLayoutManager.LayoutParams> {
                isWrapBefore = textItem.origin.startsWithNewLine
            }
            textView.setTextIfChanged(
                HtmlCompat.fromHtml(textItem.origin.text, HtmlCompat.FROM_HTML_MODE_COMPACT)
            )
        }
    }
}