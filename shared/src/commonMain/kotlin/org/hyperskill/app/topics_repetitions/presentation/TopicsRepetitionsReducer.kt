package org.hyperskill.app.topics_repetitions.presentation

import kotlin.math.max
import org.hyperskill.app.topics_repetitions.domain.analytic.TopicsRepetitionsClickedRepeatNextTopicHyperskillAnalyticEvent
import org.hyperskill.app.topics_repetitions.domain.analytic.TopicsRepetitionsClickedRepeatTopicHyperskillAnalyticEvent
import org.hyperskill.app.topics_repetitions.domain.analytic.TopicsRepetitionsViewedHyperskillAnalyticEvent
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
                    State.Loading to setOf(Action.Initialize(message.recommendedRepetitionsCount))
                } else {
                    null
                }
            is Message.TopicsRepetitionsLoaded.Success ->
                if (state is State.Loading) {
                    State.Content(
                        message.topicsRepetitions,
                        message.recommendedRepetitionsCount,
                        message.trackTitle,
                        message.remainRepetitionsCount,
                        message.repeatedTotalByCount
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
                if (state is State.Content && state.remainRepetitionsCount > 0) {
                    state.copy(nextTopicsLoading = true) to setOf(Action.FetchNextTopics(nextPage = state.page.inc()))
                } else {
                    null
                }
            is Message.NextTopicsRepetitionsLoaded.Success ->
                if (state is State.Content) {
                    state.copy(
                        topicsRepetitions = state.topicsRepetitions + message.nextTopicsRepetitions,
                        page = message.nextPage,
                        nextTopicsLoading = false,
                        remainRepetitionsCount = state.remainRepetitionsCount - message.nextTopicsRepetitions.count()
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
                            repeatedTotalByCount = getNewChartData(
                                oldChartData = state.repeatedTotalByCount.toMutableMap(),
                                repeatedCount = completedRepetition.repeatedCount
                            ),
                            recommendedRepetitionsCount = max(state.recommendedRepetitionsCount.dec(), 0)
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
                state to setOf(
                    Action.ViewAction.NavigateTo.StepScreen(message.stepId),
                    Action.LogAnalyticEvent(TopicsRepetitionsClickedRepeatTopicHyperskillAnalyticEvent())
                )
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