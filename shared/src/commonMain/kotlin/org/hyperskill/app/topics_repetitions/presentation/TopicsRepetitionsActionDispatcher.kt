package org.hyperskill.app.topics_repetitions.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.topics_repetitions.domain.interactor.TopicsRepetitionsInteractor
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Action
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class TopicsRepetitionsActionDispatcher(
    config: ActionDispatcherOptions,
    private val topicsRepetitionsInteractor: TopicsRepetitionsInteractor,
    private val profileInteractor: ProfileInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor
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
                val sentryTransaction =
                    HyperskillSentryTransactionBuilder.buildTopicsRepetitionsScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                val topicsRepetitionsResult = actionScope.async {
                    topicsRepetitionsInteractor.getTopicsRepetitions(pageSize = TOPICS_PAGINATION_SIZE)
                }
                val topicsRepetitionsStatisticsResult = actionScope.async {
                    topicsRepetitionsInteractor.getTopicsRepetitionStatistics()
                }
                val currentProfileResult = actionScope.async {
                    profileInteractor.getCurrentProfile()
                }

                val topicsRepetitions = topicsRepetitionsResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    return onNewMessage(Message.TopicsRepetitionsLoaded.Error)
                }
                val topicsRepetitionsStatistics = topicsRepetitionsStatisticsResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    return onNewMessage(Message.TopicsRepetitionsLoaded.Error)
                }
                val currentProfile = currentProfileResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    return onNewMessage(Message.TopicsRepetitionsLoaded.Error)
                }

                sentryInteractor.finishTransaction(sentryTransaction)

                onNewMessage(
                    Message.TopicsRepetitionsLoaded.Success(
                        topicsRepetitions = topicsRepetitions,
                        recommendedRepetitionsCount = action.recommendedRepetitionsCount,
                        trackTitle = currentProfile.trackTitle ?: "",
                        remainRepetitionsCount = topicsRepetitionsStatistics.totalCount - topicsRepetitions.count(),
                        repeatedTotalByCount = topicsRepetitionsStatistics.repeatedTotalByCount
                    )
                )
            }
            is Action.FetchNextTopics -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildTopicsRepetitionsFetchNextTopics()
                sentryInteractor.startTransaction(sentryTransaction)

                val nextTopicsRepetitions = topicsRepetitionsInteractor
                    .getTopicsRepetitions(pageSize = TOPICS_PAGINATION_SIZE, page = action.nextPage)
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        onNewMessage(Message.NextTopicsRepetitionsLoaded.Error)
                        return
                    }

                sentryInteractor.finishTransaction(sentryTransaction)

                onNewMessage(
                    Message.NextTopicsRepetitionsLoaded.Success(
                        nextTopicsRepetitions,
                        action.nextPage
                    )
                )
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            is Action.NotifyTopicRepeated ->
                topicsRepetitionsInteractor.topicRepeatedMutableSharedFlow.emit(Unit)
            else -> {}
        }
    }
}