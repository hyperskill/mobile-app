package org.hyperskill.app.topics_repetitions.presentation

import kotlin.math.max
import org.hyperskill.app.topics_repetitions.domain.analytic.TopicsRepetitionsClickedRepeatNextTopicHyperskillAnalyticEvent
import org.hyperskill.app.topics_repetitions.domain.analytic.TopicsRepetitionsClickedRepeatTopicHyperskillAnalyticEvent
import org.hyperskill.app.topics_repetitions.domain.analytic.TopicsRepetitionsViewedHyperskillAnalyticEvent
import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetitionStatistics
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Action
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Message
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class TopicsRepetitionsReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.NetworkError))
                ) {
                    State.Loading to setOf(Action.Initialize)
                } else {
                    null
                }
            is Message.TopicsRepetitionsLoaded.Success ->
                if (state is State.Loading) {
                    State.Content(
                        message.topicsRepetitions,
                        message.topicRepetitionStatistics,
                        message.trackTitle
                    ) to emptySet()
                } else {
                    null
                }
            is Message.TopicsRepetitionsLoaded.Error ->
                if (state is State.Loading) {
                    State.NetworkError to emptySet()
                } else {
                    null
                }
            is Message.ShowMoreButtonClicked ->
                if (state is State.Content && state.hasNextTopics) {
                    state.copy(nextTopicsLoading = true) to setOf(
                        Action.FetchNextTopics(
                            nextPage = (state.topicsRepetitions.count() / TopicsRepetitionsActionDispatcher.TOPICS_PAGINATION_SIZE) + 1
                        )
                    )
                } else {
                    null
                }
            is Message.NextTopicsRepetitionsLoaded.Success ->
                if (state is State.Content) {
                    state.copy(
                        topicsRepetitions = state.topicsRepetitions + message.nextTopicsRepetitions,
                        nextTopicsLoading = false,
                    ) to emptySet()
                } else {
                    null
                }
            is Message.NextTopicsRepetitionsLoaded.Error ->
                if (state is State.Content) {
                    state.copy(nextTopicsLoading = false) to setOf(Action.ViewAction.ShowNetworkError)
                } else {
                    null
                }
            is Message.StepCompleted ->
                if (state is State.Content) {
                    state.topicsRepetitions.firstOrNull { it.steps.contains(message.stepId) }?.let { completedRepetition ->
                        state.copy(
                            topicsRepetitions = state.topicsRepetitions.filter { it.id != completedRepetition.id },
                            topicRepetitionStatistics = TopicRepetitionStatistics(
                                recommendTodayCount = max(state.topicRepetitionStatistics.recommendTodayCount - 1, 0),
                                repeatedTodayCount = state.topicRepetitionStatistics.repeatedTodayCount + 1,
                                totalCount = state.topicRepetitionStatistics.totalCount - 1,
                                repeatedTotalByCount = getNewChartData(
                                    oldChartData = state.topicRepetitionStatistics.repeatedTotalByCount.toMutableMap(),
                                    repeatedCount = completedRepetition.repeatedCount
                                )
                            )
                        ) to setOf(Action.NotifyTopicRepeated)
                    }
                } else {
                    null
                }
            is Message.RepeatNextTopicClicked -> {
                if (state is State.Content) {
                    state to buildSet {
                        state.topicsRepetitions.firstOrNull()?.let { topicToRepeat ->
                            add(Action.ViewAction.NavigateTo.StepScreen(topicToRepeat.steps.first()))
                        }
                        add(Action.LogAnalyticEvent(TopicsRepetitionsClickedRepeatNextTopicHyperskillAnalyticEvent()))
                    }
                } else {
                    state to emptySet()
                }
            }
            is Message.RepeatTopicClicked -> {
                if (state is State.Content) {
                    state.topicsRepetitions.firstOrNull { it.topicId == message.topicId }?.let { topicRepetition ->
                        state to setOf(
                            Action.ViewAction.NavigateTo.StepScreen(topicRepetition.steps.first()),
                            Action.LogAnalyticEvent(TopicsRepetitionsClickedRepeatTopicHyperskillAnalyticEvent())
                        )
                    }
                } else {
                    null
                }
            }
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(TopicsRepetitionsViewedHyperskillAnalyticEvent()))
        } ?: (state to emptySet())

    private fun getNewChartData(
        oldChartData: MutableMap<String, Int>,
        repeatedCount: Int?
    ): Map<String, Int> {
        val oldCount = repeatedCount?.dec()
        val (oldCountKey, newCountKey) = Pair(oldCount.toString(), repeatedCount.toString())

        if (oldChartData.containsKey(oldCountKey)) {
            oldChartData.set(
                oldCountKey,
                max(oldChartData[oldCountKey]?.dec() ?: 0, 0)
            )
        }
        if (oldChartData.containsKey(newCountKey)) {
            oldChartData.set(
                newCountKey,
                oldChartData[newCountKey]?.inc() ?: 0
            )
        }
        return oldChartData
    }
}