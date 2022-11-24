package org.hyperskill.app.topics_repetitions.presentation

import org.hyperskill.app.topics_repetitions.domain.analytic.TopicsRepetitionsClickedRepeatNextTopicHyperskillAnalyticEvent
import org.hyperskill.app.topics_repetitions.domain.analytic.TopicsRepetitionsClickedRepeatTopicHyperskillAnalyticEvent
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.State
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Message
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Action
import ru.nobird.app.presentation.redux.reducer.StateReducer
import kotlin.math.max

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
                        message.topicsToRepeat,
                        message.recommendedTopicsToRepeatCount,
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
                if (state is State.Content && state.topicsRepetitions.repetitions.isNotEmpty()) {
                    state.copy(nextTopicsLoading = true) to setOf(Action.FetchNextTopics(state.topicsRepetitions))
                } else {
                    null
                }
            is Message.NextTopicsLoaded.Success ->
                if (state is State.Content) {
                    state.copy(
                        topicsRepetitions = message.remainingTopicsRepetitions,
                        topicsToRepeat = state.topicsToRepeat + message.nextTopicsToRepeat,
                        nextTopicsLoading = false
                    ) to emptySet()
                } else {
                    null
                }
            is Message.NextTopicsLoaded.Error ->
                if (state is State.Content) {
                    state.copy(nextTopicsLoading = false) to setOf(Action.ViewAction.ShowNetworkError)
                } else {
                    null
                }
            is Message.TopicRepeated ->
                if (state is State.Content) {
                    val repeatedCount = state.topicsToRepeat
                        .firstOrNull { it.topicId == message.topicId }
                        ?.repeatedCount

                    state.copy(
                        topicsRepetitions = state.topicsRepetitions.copy(
                            repetitionsByCount = getNewChartData(state.topicsRepetitions.repetitionsByCount.toMutableMap(), repeatedCount)
                        ),
                        topicsToRepeat = state.topicsToRepeat.filter { it.topicId != message.topicId },
                        recommendedTopicsToRepeatCount = max(state.recommendedTopicsToRepeatCount.dec(), 0)
                    ) to emptySet()
                } else {
                    null
                }
            is Message.ClickedRepeatNextTopicEventMessage ->
                state to setOf(Action.LogAnalyticEvent(TopicsRepetitionsClickedRepeatNextTopicHyperskillAnalyticEvent()))
            is Message.ClickedRepeatTopicEventMessage ->
                state to setOf(Action.LogAnalyticEvent(TopicsRepetitionsClickedRepeatTopicHyperskillAnalyticEvent()))
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