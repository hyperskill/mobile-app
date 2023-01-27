package org.hyperskill.app.topics_repetitions.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetition
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsActionDispatcher
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature
import org.hyperskill.app.topics_repetitions.view.model.RepetitionsStatus
import org.hyperskill.app.topics_repetitions.view.model.ShowMoreButtonState
import org.hyperskill.app.topics_repetitions.view.model.TopicToRepeat
import org.hyperskill.app.topics_repetitions.view.model.TopicsRepetitionsViewData
import kotlin.math.min

class TopicsRepetitionsViewDataMapper(
    private val resourceProvider: ResourceProvider
) {
    fun mapStateToViewData(state: TopicsRepetitionsFeature.State.Content): TopicsRepetitionsViewData =
        TopicsRepetitionsViewData(
            repetitionsStatus = getRepetitionsStatus(
                state.topicRepetitionStatistics.recommendTodayCount,
                state.topicRepetitionStatistics.totalCount,
                state.topicsRepetitions.firstOrNull()
            ),
            chartData = state.topicRepetitionStatistics.repeatedTotalByCount.toList()
                .sortedBy { it.first }
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
                    state.topicRepetitionStatistics.repeatedTotalByCount.values.sum(),
                    state.topicRepetitionStatistics.repeatedTotalByCount.values.sum()
                )
            ),
            repeatBlockTitle = mapRepetitionsCountToRepeatBlockTitle(state.topicRepetitionStatistics.totalCount),
            trackTopicsTitle = resourceProvider.getString(
                SharedResources.strings.topics_repetitions_repeat_block_current_track,
                state.trackTitle
            ),
            topicsToRepeatFromCurrentTrack = mapTopicsRepetitionsToTopicsToRepeat(state.topicsRepetitions.filter { it.isInCurrentTrack }),
            topicsToRepeatFromOtherTracks = mapTopicsRepetitionsToTopicsToRepeat(state.topicsRepetitions.filter { !it.isInCurrentTrack }),
            showMoreButtonState = if (state.isLoadingNextTopics) {
                ShowMoreButtonState.LOADING
            } else if (state.hasNextTopicsToLoad) {
                ShowMoreButtonState.AVAILABLE
            } else {
                ShowMoreButtonState.EMPTY
            },
            topicsToRepeatWillLoadedCount = if (state.topicsRepetitions.size % TopicsRepetitionsActionDispatcher.TOPICS_PAGINATION_SIZE != 0) {
                1
            } else {
                min(
                    state.topicRepetitionStatistics.totalCount - state.topicsRepetitions.count(),
                    TopicsRepetitionsActionDispatcher.TOPICS_PAGINATION_SIZE,
                )
            }

        )

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
                title = topicRepetition.topicTitle
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