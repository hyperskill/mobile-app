package org.hyperskill.app.topics_repetitions.view.mapper

import kotlin.math.min
import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsActionDispatcher
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature
import org.hyperskill.app.topics_repetitions.view.model.RepetitionsStatus
import org.hyperskill.app.topics_repetitions.view.model.ShowMoreButtonState
import org.hyperskill.app.topics_repetitions.view.model.TopicToRepeat
import org.hyperskill.app.topics_repetitions.view.model.TopicsRepetitionsViewData

class TopicsRepetitionsViewDataMapper(
    private val resourceProvider: ResourceProvider
) {
    fun mapStateToViewData(state: TopicsRepetitionsFeature.State.Content): TopicsRepetitionsViewData {
        val allRepetitionsCount = state.topicsRepetitions.repetitions.count() + state.topicsToRepeat.count()

        return TopicsRepetitionsViewData(
            repetitionsStatus = getRepetitionsStatus(
                state.recommendedRepetitionsCount,
                allRepetitionsCount,
                state.topicsToRepeat.firstOrNull()
            ),
            chartData = state.topicsRepetitions.repetitionsByCount.toList().sortedBy { it.first }
                .map {
                    Pair(
                        resourceProvider.getQuantityString(
                            SharedResources.plurals.times_repetitions_chart,
                            it.first.toInt(),
                            it.first.toInt()
                        ),
                        it.second
                    )
                },
            chartDescription = resourceProvider.getString(
                SharedResources.strings.topics_repetitions_chart_description,
                resourceProvider.getQuantityString(
                    SharedResources.plurals.topics,
                    state.topicsRepetitions.repetitionsByCount.values.sum(),
                    state.topicsRepetitions.repetitionsByCount.values.sum()
                )
            ),
            repeatBlockTitle = mapRepetitionsCountToRepeatBlockTitle(allRepetitionsCount),
            trackTopicsTitle = mapStateToTrackTopicsTitle(state),
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

    private fun mapRepetitionsCountToRepeatBlockTitle(repetitionsCount: Int): String =
        if (repetitionsCount == 0) {
            resourceProvider.getString(SharedResources.strings.topics_repetitions_repeat_block_empty_title)
        } else {
            resourceProvider.getString(
                SharedResources.strings.topics_repetitions_repeat_block_title,
                resourceProvider.getQuantityString(
                    SharedResources.plurals.topics,
                    repetitionsCount,
                    repetitionsCount
                )
            )
        }

    private fun mapStateToTrackTopicsTitle(state: TopicsRepetitionsFeature.State.Content): String {
        val count = state.topicsRepetitions.repetitions.count() + state.topicsToRepeat.count()

        return if (count == 0) {
            resourceProvider.getString(
                SharedResources.strings.topics_repetitions_repeat_block_empty_current_track,
                state.trackTitle
            )
        } else {
            resourceProvider.getString(
                SharedResources.strings.topics_repetitions_repeat_block_current_track,
                state.trackTitle
            )
        }
    }

    private fun getRepetitionsStatus(
        recommendedRepetitionsCount: Int,
        allRepetitionsCount: Int,
        firstTopicToRepeat: TopicToRepeat?
    ): RepetitionsStatus =
        if (recommendedRepetitionsCount > 0) {
            RepetitionsStatus.RecommendedTopicsAvailable(
                recommendedRepetitionsCount = recommendedRepetitionsCount,
                repeatButtonText = if (firstTopicToRepeat != null)
                    resourceProvider.getString(
                        SharedResources.strings.topics_repetitions_repeat_button_text,
                        firstTopicToRepeat.title
                    )
                else null
            )
        } else if (allRepetitionsCount > 0) {
            RepetitionsStatus.RecommendedTopicsRepeated
        } else {
            RepetitionsStatus.AllTopicsRepeated
        }
}