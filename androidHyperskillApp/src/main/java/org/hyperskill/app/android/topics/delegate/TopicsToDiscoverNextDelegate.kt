package org.hyperskill.app.android.topics.delegate

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.decoration.VerticalMarginItemDecoration
import org.hyperskill.app.android.topics.adapter_delegate.TopicAdapterDelegate
import org.hyperskill.app.android.topics.view.TopicListItem
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextFeature
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter

class TopicsToDiscoverNextDelegate(
    private val loadingItems: Int,
    onTopicClick: (topicId: Long) -> Unit
) {
    private val nextTopicsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        DefaultDelegateAdapter<TopicListItem>().apply {
            addDelegate(TopicAdapterDelegate(onTopicClick))
            addDelegate(loadingAdapterDelegate())
        }
    }

    fun setup(context: Context, recyclerView: RecyclerView) {
        with(recyclerView) {
            adapter = nextTopicsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = false
            addItemDecoration(
                VerticalMarginItemDecoration(resources.getDimensionPixelSize(R.dimen.track_next_topic_vertical_item_margin))
            )
        }
    }

    fun render(state: TopicsToDiscoverNextFeature.State) {
        nextTopicsAdapter.items = when (state) {
            TopicsToDiscoverNextFeature.State.Idle,
            TopicsToDiscoverNextFeature.State.Loading -> {
                List(loadingItems) {
                    TopicListItem.LoadingPlaceholder
                }
            }
            is TopicsToDiscoverNextFeature.State.Content -> {
                state.topicsToDiscoverNext.map(TopicListItem::fromDomainTopic)
            }
            TopicsToDiscoverNextFeature.State.Error,
            TopicsToDiscoverNextFeature.State.Empty -> emptyList()
        }
    }

    private fun loadingAdapterDelegate() =
        adapterDelegate<TopicListItem, TopicListItem.LoadingPlaceholder>(R.layout.item_topics_list_placeholder)
}