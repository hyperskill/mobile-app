package org.hyperskill.app.android.topics.adapter_delegate

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.ItemTopicBinding
import org.hyperskill.app.topics.domain.model.Topic
import org.hyperskill.app.topics.domain.model.completenessPercentage
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder
import kotlin.math.roundToInt

class TopicAdapterDelegate(
    private val onTopicClick: (topicId: Long) -> Unit
) : AdapterDelegate<Topic, DelegateViewHolder<Topic>>() {

    override fun isForViewType(position: Int, data: Topic): Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<Topic> =
        ViewHolder(createView(parent, R.layout.item_topic))

    inner class ViewHolder(root: View) : DelegateViewHolder<Topic>(root) {

        val binding = ItemTopicBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                itemData?.id?.let(onTopicClick)
            }
        }

        override fun onBind(data: Topic) {
            with(binding) {
                topicTitle.text = data.title

                val progress = data.progress

                topicCompletenessTextView.isVisible = progress != null
                topicCompletenessView.isVisible = progress != null

                if (progress != null) {
                    with(topicCompletenessTextView) {
                        text = when {
                            progress.isSkipped || progress.isCompleted -> null
                            else -> "${progress.completenessPercentage.roundToInt()}%"
                        }
                        val drawableRes: Int = when {
                            progress.isSkipped -> R.drawable.ic_topic_skipped
                            progress.isCompleted -> R.drawable.ic_topic_completed
                            else -> 0
                        }
                        setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            drawableRes, // right
                            0
                        )
                    }
                    topicCompletenessView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        matchConstraintPercentWidth = progress.completenessPercentage / 100
                    }
                }
            }
        }
    }
}