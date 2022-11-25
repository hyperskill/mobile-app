package org.hyperskill.app.topics_repetitions.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsActionDispatcher
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature
import org.hyperskill.app.topics_repetitions.view.model.ShowMoreButtonState
import org.hyperskill.app.topics_repetitions.view.model.TopicsRepetitionsViewData
import kotlin.math.min

class TopicsRepetitionsViewDataMapper(
    private val resourceProvider: ResourceProvider
) {
    fun mapStateToViewData(state: TopicsRepetitionsFeature.State.Content): TopicsRepetitionsViewData =
        TopicsRepetitionsViewData(
            recommendedTopicsToRepeatCount = state.recommendedTopicsToRepeatCount,
            repeatButtonText = if (state.topicsToRepeat.isNotEmpty()) {
                resourceProvider.getString(
                    SharedResources.strings.topics_repetitions_repeat_button_text,
                    state.topicsToRepeat.first().title
                )
            } else null,
            chartData = state.topicsRepetitions.repetitionsByCount.mapKeys {
                resourceProvider.getQuantityString(SharedResources.plurals.times, it.key.toInt(), it.key.toInt())
            }.toList().sortedBy { it.first },
            chartDescription = resourceProvider.getString(
                SharedResources.strings.topics_repetitions_chart_description,
                resourceProvider.getQuantityString(
                    SharedResources.plurals.topics,
                    state.topicsRepetitions.repetitionsByCount.values.sum(),
                    state.topicsRepetitions.repetitionsByCount.values.sum()
                )
            ),
            repeatBlockTitle = resourceProvider.getString(
                SharedResources.strings.topics_repetitions_repeat_block_title,
                resourceProvider.getQuantityString(
                    SharedResources.plurals.topics,
                    state.topicsRepetitions.repetitions.count() + state.topicsToRepeat.count(),
                    state.topicsRepetitions.repetitions.count() + state.topicsToRepeat.count()
                )
            ),
            trackTopicsTitle = resourceProvider.getString(
                SharedResources.strings.topics_repetitions_repeat_block_current_track,
                state.trackTitle
            ),
            topicsToRepeat = state.topicsToRepeat,
            showMoreButtonState = if (state.nextTopicsLoading) {
                ShowMoreButtonState.LOADING
            } else if (state.topicsRepetitions.repetitions.isNotEmpty()) {
                ShowMoreButtonState.AVAILABLE
            } else {
                ShowMoreButtonState.EMPTY
            },
            topicsToRepeatWillLoadedCount = min(
                state.topicsRepetitions.repetitions.count(),
                TopicsRepetitionsActionDispatcher.TOPICS_PAGINATION_SIZE
            )
        )
}