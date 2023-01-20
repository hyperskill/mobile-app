package org.hyperskill.app.track.presentation

import kotlinx.coroutines.async
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.learning_activities.domain.interactor.LearningActivitiesInteractor
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.topics.domain.interactor.TopicsInteractor
import org.hyperskill.app.topics.domain.model.Topic
import org.hyperskill.app.topics.domain.model.TopicProgress
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.presentation.TrackFeature.Action
import org.hyperskill.app.track.presentation.TrackFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class TrackActionDispatcher(
    config: ActionDispatcherOptions,
    private val trackInteractor: TrackInteractor,
    private val profileInteractor: ProfileInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val learningActivitiesInteractor: LearningActivitiesInteractor,
    private val topicsInteractor: TopicsInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val urlPathProcessor: UrlPathProcessor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    companion object {
        private const val TOPICS_TO_DISCOVER_NEXT_LEARNING_ACTIVITIES_PAGE_SIZE = 20
        private const val TOPICS_TO_DISCOVER_NEXT_PREFIX_COUNT = 10
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchTrack -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildTrackScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                val trackId = profileInteractor
                    .getCurrentProfile(sourceType = DataSourceType.CACHE)
                    .map { it.trackId }
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        onNewMessage(Message.TrackFailure)
                        return
                    } ?: return

                val trackResult = actionScope.async { trackInteractor.getTrack(trackId) }
                val trackProgressResult = actionScope.async { progressesInteractor.getTrackProgress(trackId) }
                val studyPlanResult = actionScope.async { trackInteractor.getStudyPlanByTrackId(trackId) }

                val track = trackResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    onNewMessage(Message.TrackFailure)
                    return
                }
                val topicsToDiscoverNext = getTopicsToDiscoverNext(track).getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    onNewMessage(Message.TrackFailure)
                    return
                }
                val trackProgress = trackProgressResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    onNewMessage(Message.TrackFailure)
                    return
                } ?: return
                val studyPlan = studyPlanResult.await().getOrNull()

                sentryInteractor.finishTransaction(sentryTransaction)

                onNewMessage(Message.TrackSuccess(track, trackProgress, studyPlan, topicsToDiscoverNext))
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            is Action.GetMagicLink ->
                getLink(action.path, ::onNewMessage)
            else -> {}
        }
    }

    private suspend fun getTopicsToDiscoverNext(track: Track): Result<List<Topic>> =
        kotlin.runCatching {
            if (track.isCompleted) {
                return Result.success(emptyList())
            }

            val learningActivities = learningActivitiesInteractor
                .getUncompletedTopicsLearningActivities(
                    pageSize = TOPICS_TO_DISCOVER_NEXT_LEARNING_ACTIVITIES_PAGE_SIZE
                )
                .map { it.learningActivities }
                .getOrThrow()

            if (learningActivities.isEmpty()) {
                return Result.success(emptyList())
            }

            val topicsIds = learningActivities.map { it.targetId }
            val topics = topicsInteractor
                .getTopics(topicsIds)
                .getOrThrow()

            val progressById: Map<String, TopicProgress> = progressesInteractor
                .getTopicsProgresses(topicsIds)
                .getOrThrow()
                .associateBy { it.id }

            val topicsByStagePosition: Map<Int, List<Topic>> = topics
                .map { it.copy(progress = progressById[it.progressId]) }
                .filter { it.progress?.stagePosition != null }
                .groupBy { it.progress!!.stagePosition!! }

            val minStagePositionKey = topicsByStagePosition.keys.minOrNull()

            return if (minStagePositionKey != null) {
                Result.success(
                    topicsByStagePosition
                        .getValue(minStagePositionKey)
                        .take(TOPICS_TO_DISCOVER_NEXT_PREFIX_COUNT)
                )
            } else {
                Result.success(topics.take(TOPICS_TO_DISCOVER_NEXT_PREFIX_COUNT))
            }
        }

    private suspend fun getLink(path: HyperskillUrlPath, onNewMessage: (Message) -> Unit): Unit =
        urlPathProcessor.processUrlPath(path)
            .fold(
                onSuccess = { url ->
                    onNewMessage(Message.GetMagicLinkReceiveSuccess(url))
                },
                onFailure = {
                    onNewMessage(Message.GetMagicLinkReceiveFailure)
                }
            )
}