package org.hyperskill.app.android.topics.delegate

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.decoration.HorizontalMarginItemDecoration
import org.hyperskill.app.android.core.view.ui.adapter.decoration.VerticalMarginItemDecoration
import org.hyperskill.app.android.topics.adapter_delegate.TopicAdapterDelegate
import org.hyperskill.app.topics.domain.model.Topic
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter

class TopicsToDiscoverNextDelegate(
    onTopicClick: (topicId: Long) -> Unit
) {
    private val nextTopicsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        DefaultDelegateAdapter<Topic>().apply {
            addDelegate(TopicAdapterDelegate(onTopicClick))
        }
    }

    fun setup(context: Context, recyclerView: RecyclerView) {
        with(recyclerView) {
            adapter = nextTopicsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = false
            val verticalMargin = resources.getDimensionPixelSize(R.dimen.track_next_topic_vertical_item_margin)
            addItemDecoration(
                VerticalMarginItemDecoration(
                    verticalMargin = verticalMargin,
                    firstItemTopMargin = verticalMargin,
                    lastItemMargin = verticalMargin
                )
            )
            addItemDecoration(
                HorizontalMarginItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.track_next_topic_horizontal_item_margin)
                )
            )
        }
    }

    fun setTopics(topics: List<Topic>) {
        if (topics.isNotEmpty()) {
            nextTopicsAdapter.items = topics
        }
    }
}