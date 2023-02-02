package org.hyperskill.app.topics_to_discover_next.presentation

import org.hyperskill.app.topics_to_discover_next.domain.analytic.TopicsToDiscoverNextClickedHyperskillAnalyticEvent
import org.hyperskill.app.topics_to_discover_next.domain.model.TopicsToDiscoverNextScreen
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextFeature.Action
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextFeature.Message
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class TopicsToDiscoverNextReducer(
    private val screen: TopicsToDiscoverNextScreen
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Empty || state is State.Content || state is State.Error))
                ) {
                    State.Loading to setOf(Action.FetchTopicsToDiscoverNext(screen.sentryTransaction))
                } else {
                    null
                }
            is Message.FetchTopicsToDiscoverNextError ->
                State.Error to emptySet()
            is Message.FetchTopicsToDiscoverNextSuccess ->
                if (message.topicsToDiscoverNext.isEmpty()) {
                    State.Empty to emptySet()
                } else {
                    State.Content(message.topicsToDiscoverNext) to emptySet()
                }
            is Message.PullToRefresh ->
                when (state) {
                    is State.Content ->
                        if (state.isRefreshing) null
                        else state.copy(isRefreshing = true) to setOf(
                            Action.FetchTopicsToDiscoverNext(screen.sentryTransaction)
                        )
                    is State.Error, State.Empty ->
                        State.Loading to setOf(Action.FetchTopicsToDiscoverNext(screen.sentryTransaction))
                    else ->
                        null
                }
            is Message.TopicRepeated ->
                if (state is State.Content) {
                    val newTopicsToDiscoverNext = state.topicsToDiscoverNext.filter { it.id != message.topicId }
                    if (newTopicsToDiscoverNext.isEmpty()) {
                        State.Loading to setOf(Action.FetchTopicsToDiscoverNext(screen.sentryTransaction))
                    } else {
                        state.copy(topicsToDiscoverNext = newTopicsToDiscoverNext) to emptySet()
                    }
                } else {
                    null
                }
            is Message.TopicToDiscoverNextClicked -> {
                val targetTopic = (state as? State.Content)
                    ?.topicsToDiscoverNext?.firstOrNull { it.id == message.topicId }

                if (targetTopic?.theoryId != null) {
                    state to setOf(
                        Action.ViewAction.ShowStepScreen(targetTopic.theoryId),
                        Action.LogAnalyticEvent(
                            TopicsToDiscoverNextClickedHyperskillAnalyticEvent(
                                analyticRoute = screen.analyticRoute,
                                topicId = targetTopic.id,
                                theoryId = targetTopic.theoryId
                            )
                        )
                    )
                } else {
                    null
                }
            }
        } ?: (state to emptySet())
}