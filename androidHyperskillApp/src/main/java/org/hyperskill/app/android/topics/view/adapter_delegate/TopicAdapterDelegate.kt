package org.hyperskill.app.android.topics.view.adapter_delegate

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.ItemTopicBinding
import org.hyperskill.app.android.topics.view.model.TopicListItem
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder
import ru.nobird.app.core.model.safeCast

class TopicAdapterDelegate(
    private val onTopicClick: (topicId: Long) -> Unit
) : AdapterDelegate<TopicListItem, DelegateViewHolder<TopicListItem>>() {

    override fun isForViewType(position: Int, data: TopicListItem): Boolean =
        data is TopicListItem.Topic

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<TopicListItem> =
        ViewHolder(createView(parent, R.layout.item_topic))

    inner class ViewHolder(root: View) : DelegateViewHolder<TopicListItem>(root) {

        val binding = ItemTopicBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                itemData?.safeCast<TopicListItem.Topic>()?.id?.let(onTopicClick)
            }
        }

        override fun onBind(data: TopicListItem) {
            data as TopicListItem.Topic
            with(binding) {
                topicTitle.text = data.title

                topicCompletenessTextView.isVisible =
                    data.completenessText != null || data.completenessDrawable != 0
                topicCompletenessView.isVisible = data.completenessPercentage > 0f

                with(topicCompletenessTextView) {
                    text = data.completenessText
                    setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        /*right*/ data.completenessDrawable,
                        0
                    )
                }
                topicCompletenessView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    matchConstraintPercentWidth = data.completenessPercentage
                }
            }
        }
    }
}