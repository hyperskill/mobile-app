package org.hyperskill.app.android.topics_repetitions.view.delegate

import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.decoration.HorizontalMarginItemDecoration
import org.hyperskill.app.android.core.view.ui.adapter.decoration.VerticalMarginItemDecoration
import org.hyperskill.app.android.databinding.LayoutTopicsRepetitionTopicsListBinding
import org.hyperskill.app.android.topics_repetitions.view.model.TopicsRepetitionListItem
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature
import org.hyperskill.app.topics_repetitions.view.model.ShowMoreButtonState
import org.hyperskill.app.topics_repetitions.view.model.TopicToRepeat
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class TopicsRepetitionListDelegate(
    private val binding: LayoutTopicsRepetitionTopicsListBinding,
    private val onNewMessage: (TopicsRepetitionsFeature.Message) -> Unit
) {

    private val topicsDelegate by lazy(LazyThreadSafetyMode.NONE) {
        DefaultDelegateAdapter<TopicsRepetitionListItem>().apply {
            addDelegate(topicsAdapterDelegate())
            addDelegate(topicsSkeletonAdapterDelegate())
        }
    }

    init {
        setupLayout(binding)
    }

    private fun setupLayout(binding: LayoutTopicsRepetitionTopicsListBinding) {
        with(binding) {
            topicsListsShowMoreButton.setOnClickListener {
                onNewMessage(TopicsRepetitionsFeature.Message.ShowMoreButtonClicked)
            }
            with(topicsListRecyclerView) {
                layoutManager =
                    LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
                adapter = topicsDelegate
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
    }

    fun render(
        repeatBlockTitle: String,
        trackTopicsTitle: String,
        topicsToRepeat: List<TopicToRepeat>,
        showMoreButtonState: ShowMoreButtonState,
        topicsToRepeatWillLoadedCount: Int
    ) {
        with(binding) {
            root.isVisible = topicsToRepeat.isNotEmpty()
            topicsListTitleTextView.setTextIfChanged(repeatBlockTitle)
            topicsListTrackTitleTextView.setTextIfChanged(trackTopicsTitle)
            topicsListsShowMoreButton.isVisible = showMoreButtonState == ShowMoreButtonState.AVAILABLE
            topicsListRecyclerView.isVisible = topicsToRepeat.isNotEmpty()
            if (topicsToRepeat.isNotEmpty()) {
                topicsDelegate.items = buildList {
                    addAll(topicsToRepeat.map(TopicsRepetitionListItem::Topic))
                    if (showMoreButtonState == ShowMoreButtonState.LOADING) {
                        addAll(
                            List(topicsToRepeatWillLoadedCount) {
                                TopicsRepetitionListItem.LoadingStub
                            }
                        )
                    }
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
                    onNewMessage(TopicsRepetitionsFeature.Message.RepeatTopicClicked(stepId = it.topic.stepId))
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
}