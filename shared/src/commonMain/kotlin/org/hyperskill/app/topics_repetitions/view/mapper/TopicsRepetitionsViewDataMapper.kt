package org.hyperskill.app.topics_repetitions.view.mapper

import kotlin.math.min
import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetition
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
        val allRepetitionsCount = state.topicsRepetitions.count() + state.remainRepetitionsCount

        return TopicsRepetitionsViewData(
            repetitionsStatus = getRepetitionsStatus(
                state.recommendedRepetitionsCount,
                allRepetitionsCount,
                state.topicsRepetitions.firstOrNull()
            ),
            chartData = state.repeatedTotalByCount.toList().sortedBy { it.first }
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
                    state.repeatedTotalByCount.values.sum(),
                    state.repeatedTotalByCount.values.sum()
                )
            ),
            repeatBlockTitle = mapRepetitionsCountToRepeatBlockTitle(allRepetitionsCount),
            trackTopicsTitle = resourceProvider.getString(
                SharedResources.strings.topics_repetitions_repeat_block_current_track,
                state.trackTitle
            ),
            topicsToRepeatFromCurrentTrack = mapTopicsRepetitionsToTopicsToRepeat(state.topicsRepetitions.filter { it.isInCurrentTrack }),
            topicsToRepeatFromOtherTracks = mapTopicsRepetitionsToTopicsToRepeat(state.topicsRepetitions.filter { it.isInCurrentTrack.not() }),
            showMoreButtonState = if (state.nextTopicsLoading) {
                ShowMoreButtonState.LOADING
            } else if (state.remainRepetitionsCount > 0) {
                ShowMoreButtonState.AVAILABLE
            } else {
                ShowMoreButtonState.EMPTY
            },
            topicsToRepeatWillLoadedCount = min(
                state.remainRepetitionsCount,
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

    private fun mapTopicsRepetitionsToTopicsToRepeat(topicsRepetitions: List<TopicRepetition>): List<TopicToRepeat> =
        topicsRepetitions.map { topicRepetition ->
            TopicToRepeat(
                topicId = topicRepetition.topicId,
                title = topicRepetition.topicTitle,
                stepId = topicRepetition.steps.first(),
                repeatedCount = topicRepetition.repeatedCount
            )
        }

    private fun getRepetitionsStatus(
        recommendedRepetitionsCount: Int,
        allRepetitionsCount: Int,
        firstTopicRepetition: TopicRepetition?
    ): RepetitionsStatus =
        if (recommendedRepetitionsCount > 0) {
            RepetitionsStatus.RecommendedTopicsAvailable(
                recommendedRepetitionsCount = recommendedRepetitionsCount,
                repeatButtonText = if (firstTopicRepetition != null)
                    resourceProvider.getString(
                        SharedResources.strings.topics_repetitions_repeat_button_text,
                        firstTopicRepetition.topicTitle
                    )
                else null
            )
        } else if (allRepetitionsCount > 0) {
            RepetitionsStatus.RecommendedTopicsRepeated
        } else {
            RepetitionsStatus.AllTopicsRepeated
        }
}