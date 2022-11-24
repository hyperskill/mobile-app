package org.hyperskill.app.topics_repetitions.presentation

import kotlinx.coroutines.launch
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Action
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Message
import org.hyperskill.app.topics.domain.interactor.TopicsInteractor
import org.hyperskill.app.topics_repetitions.domain.interactor.TopicsRepetitionsInteractor
import org.hyperskill.app.topics_repetitions.domain.model.Repetition
import org.hyperskill.app.topics_repetitions.view.model.TopicToRepeat
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class TopicsRepetitionsActionDispatcher(
    config: ActionDispatcherOptions,
    private val topicsRepetitionsInteractor: TopicsRepetitionsInteractor,
    private val topicsInteractor: TopicsInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val profileInteractor: ProfileInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    companion object {
        const val TOPICS_PAGINATION_SIZE = 5
    }

    init {
        actionScope.launch {
            topicsRepetitionsInteractor.solvedStepsSharedFlow.collect { id ->
                onNewMessage(Message.StepCompleted(id))
            }
        }
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.Initialize -> {
                val topicsRepetitions = topicsRepetitionsInteractor
                    .getCurrentTrackTopicsRepetitions()
                    .getOrElse {
                        onNewMessage(Message.TopicsRepetitionsLoaded.Error)
                        return
                    }

                val (remainingRepetitions, topicsToRepeat) = loadNextTopics(
                    topicsRepetitions.repetitions.sortedBy { it.nextRepeatAt }
                ).getOrElse {
                    onNewMessage(Message.TopicsRepetitionsLoaded.Error)
                    return
                }

                val currentProfile = profileInteractor
                    .getCurrentProfile()
                    .getOrElse {
                        onNewMessage(Message.TopicsRepetitionsLoaded.Error)
                        return
                    }

                onNewMessage(
                    Message.TopicsRepetitionsLoaded.Success(
                        topicsRepetitions = topicsRepetitions.copy(repetitions = remainingRepetitions),
                        topicsToRepeat = topicsToRepeat,
                        recommendedTopicsToRepeatCount = currentProfile.recommendedRepetitionsCount,
                        trackTitle = currentProfile.trackTitle ?: ""
                    )
                )
            }
            is Action.FetchNextTopics -> {
                val (remainingRepetitions, topicsToRepeat) = loadNextTopics(action.topicsRepetitions.repetitions)
                    .getOrElse {
                        onNewMessage(Message.NextTopicsLoaded.Error)
                        return
                    }

                onNewMessage(
                    Message.NextTopicsLoaded.Success(
                        action.topicsRepetitions.copy(repetitions = remainingRepetitions),
                        topicsToRepeat
                    )
                )
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            is Action.NotifyTopicRepeated ->
                topicsRepetitionsInteractor.topicRepeatedMutableSharedFlow.emit(Unit)
        }
    }

    /**
     * Load next TOPICS_PAGINATION_SIZE topics and remove
     * TOPICS_PAGINATION_SIZE repetitions from passed parameter
     *
     * @param repetitions repetitions to load next topics
     * @return
     */
    private suspend fun loadNextTopics(repetitions: List<Repetition>): Result<Pair<List<Repetition>, List<TopicToRepeat>>> =
        kotlin.runCatching {
            val firstRepetitions = repetitions.take(TOPICS_PAGINATION_SIZE)

            val topicsIds = firstRepetitions.map { it.topicId }

            val progressById = progressesInteractor
                .getTopicsProgresses(topicsIds)
                .getOrThrow()
                .associateBy { it.id }

            val topicsById = topicsInteractor
                .getTopics(topicsIds)
                .getOrThrow()
                .associateBy { it.id }

            val topicsToRepeat = firstRepetitions
                .map { repetition ->
                    val topic = topicsById[repetition.topicId]
                    TopicToRepeat(
                        topicId = repetition.topicId,
                        title = topic?.title ?: "",
                        stepId = repetition.steps.first(),
                        repeatedCount = progressById[topic?.progressId]?.repeatedCount ?: 0
                    )
                }
            return Result.success(Pair(repetitions.drop(TOPICS_PAGINATION_SIZE), topicsToRepeat))
        }
}