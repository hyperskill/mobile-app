package org.hyperskill.app.android.topics_repetitions.view.delegate

import android.content.Context
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.decoration.itemDecoration
import org.hyperskill.app.android.databinding.LayoutTopicsRepetitionTopicsListBinding
import org.hyperskill.app.android.topics_repetitions.view.model.TopicsRepetitionListItem
import org.hyperskill.app.android.topics_repetitions.view.model.TopicsRepetitionListState
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature
import org.hyperskill.app.topics_repetitions.view.model.ShowMoreButtonState
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class TopicsRepetitionListDelegate(
    private val binding: LayoutTopicsRepetitionTopicsListBinding,
    private val onNewMessage: (TopicsRepetitionsFeature.Message) -> Unit
) {

    private val topicsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        DefaultDelegateAdapter<TopicsRepetitionListItem>().apply {
            addDelegate(topicsAdapterDelegate())
            addDelegate(topicsSkeletonAdapterDelegate())
            addDelegate(topicsHeaderAdapterDelegate())
        }
    }

    init {
        binding.topicsListsShowMoreButton.setOnClickListener {
            onNewMessage(TopicsRepetitionsFeature.Message.ShowMoreButtonClicked)
        }
        setupRecycler()
    }

    private fun setupRecycler() {
        with(binding.topicsListRecyclerView) {
            layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            adapter = topicsAdapter
            isNestedScrollingEnabled = false
            val headerVerticalMargin = resources.getDimensionPixelSize(R.dimen.track_next_topic_header_vertical_item_margin)
            val topicVerticalMargin = resources.getDimensionPixelSize(R.dimen.track_next_topic_vertical_item_margin)
            itemDecoration { position, rect, _ ->
                val item = topicsAdapter.items.getOrNull(position) ?: return@itemDecoration
                when (item) {
                    is TopicsRepetitionListItem.Header -> {
                        with(rect) {
                            top = headerVerticalMargin
                            bottom = headerVerticalMargin
                        }
                    }
                    is TopicsRepetitionListItem.Topic,
                    TopicsRepetitionListItem.LoadingStub -> {
                        val isAfterHeader  = if (position > 0) {
                            topicsAdapter.items.getOrNull(position - 1) is TopicsRepetitionListItem.Header
                        } else {
                            false
                        }
                        if (!isAfterHeader) {
                            rect.top = topicVerticalMargin
                        }
                    }
                }
            }
        }
    }

    fun render(context: Context, previousState: TopicsRepetitionListState?, state: TopicsRepetitionListState) {
        if (previousState == state) return
        with(binding) {
            root.isVisible =
                state.topicsToRepeatFromCurrentTrack.isNotEmpty() || state.topicsToRepeatFromOtherTracks.isNotEmpty()

            topicsListTitleTextView.setTextIfChanged(state.repeatBlockTitle)

            if (previousState?.showMoreButtonState != state.showMoreButtonState) {
                topicsListsShowMoreButton.isVisible =
                    state.showMoreButtonState == ShowMoreButtonState.AVAILABLE
            }

            topicsAdapter.items = buildList {
                if (state.topicsToRepeatFromCurrentTrack.isNotEmpty()) {
                    add(TopicsRepetitionListItem.Header(state.trackTopicsTitle))
                    addAll(state.topicsToRepeatFromCurrentTrack.map(TopicsRepetitionListItem::Topic))
                }
                if (state.topicsToRepeatFromOtherTracks.isNotEmpty()) {
                    add(
                        TopicsRepetitionListItem.Header(
                            context.getString(org.hyperskill.app.R.string.topics_repetitions_repeat_block_other_tracks)
                        )
                    )
                    addAll(state.topicsToRepeatFromOtherTracks.map(TopicsRepetitionListItem::Topic))
                }
                if (state.showMoreButtonState == ShowMoreButtonState.LOADING) {
                    addAll(
                        List(state.topicsToRepeatWillLoadedCount) {
                            TopicsRepetitionListItem.LoadingStub
                        }
                    )
                }
            }
        }
    }

    private fun topicsAdapterDelegate() =
        adapterDelegate<TopicsRepetitionListItem, TopicsRepetitionListItem.Topic>(
            R.layout.item_topic_to_repeat
        ) {
            val title = itemView.findViewById<TextView>(R.id.topicTitle)
            itemView.setOnClickListener {
                item?.let {
                    onNewMessage(TopicsRepetitionsFeature.Message.RepeatTopicClicked(topicId = it.topic.topicId))
                }
            }

            onBind { item ->
                title.setTextIfChanged(item.topic.title)
            }
        }

    private fun topicsSkeletonAdapterDelegate() =
        adapterDelegate<TopicsRepetitionListItem, TopicsRepetitionListItem.LoadingStub>(
            R.layout.item_topics_to_repeat_skeleton
        )

    private fun topicsHeaderAdapterDelegate() =
        adapterDelegate<TopicsRepetitionListItem, TopicsRepetitionListItem.Header>(R.layout.item_topics_to_repeat_header) {
            onBind {
                item?.title?.let { title ->
                    (itemView as TextView).text = title
                }
            }
        }
}