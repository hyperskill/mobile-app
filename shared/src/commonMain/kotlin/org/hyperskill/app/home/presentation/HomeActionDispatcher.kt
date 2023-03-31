package org.hyperskill.app.home.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.view.mapper.DateFormatter
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.home.domain.interactor.HomeInteractor
import org.hyperskill.app.home.presentation.HomeFeature.Action
import org.hyperskill.app.home.presentation.HomeFeature.Message
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.topics_repetitions.domain.flow.TopicRepeatedFlow
import org.hyperskill.app.topics_repetitions.domain.interactor.TopicsRepetitionsInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class HomeActionDispatcher(
    config: ActionDispatcherOptions,
    homeInteractor: HomeInteractor,
    private val profileInteractor: ProfileInteractor,
    private val topicsRepetitionsInteractor: TopicsRepetitionsInteractor,
    private val stepInteractor: StepInteractor,
    private val freemiumInteractor: FreemiumInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val urlPathProcessor: UrlPathProcessor,
    private val dateFormatter: DateFormatter,
    topicRepeatedFlow: TopicRepeatedFlow
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    private var isTimerLaunched: Boolean = false

    companion object {
        private val DELAY_ONE_MINUTE = 1.toDuration(DurationUnit.MINUTES)

        fun calculateNextProblemIn(): Long {
            val tzNewYork = TimeZone.of("America/New_York")
            val nowInNewYork = Clock.System.now().toLocalDateTime(tzNewYork).toInstant(tzNewYork)
            val tomorrowInNewYork = nowInNewYork.plus(1, DateTimeUnit.DAY, tzNewYork).toLocalDateTime(tzNewYork)
            val startOfTomorrow =
                LocalDateTime(tomorrowInNewYork.year, tomorrowInNewYork.month, tomorrowInNewYork.dayOfMonth, 0, 0, 0, 0)
            return (startOfTomorrow.toInstant(tzNewYork) - nowInNewYork).inWholeSeconds
        }
    }

    init {
        homeInteractor.solvedStepsSharedFlow
            .onEach { onNewMessage(Message.StepQuizSolved(it)) }
            .launchIn(actionScope)

        topicRepeatedFlow.observe()
            .onEach { onNewMessage(Message.TopicRepeated) }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchHomeScreenData -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildHomeScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                val currentProfile = profileInteractor
                    .getCurrentProfile(DataSourceType.REMOTE) // ALTAPPS-303: Get from remote to get relevant problem of the day
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        return onNewMessage(Message.HomeFailure)
                    }

                val problemOfDayStateResult = actionScope.async { getProblemOfDayState(currentProfile.dailyStep) }
                val repetitionsStateResult = actionScope.async { getRepetitionsState() }
                val isFreemiumEnabledResult = actionScope.async { freemiumInteractor.isFreemiumEnabled() }

                val problemOfDayState = problemOfDayStateResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    return onNewMessage(Message.HomeFailure)
                }
                val repetitionsState = repetitionsStateResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    return onNewMessage(Message.HomeFailure)
                }
                val isFreemiumEnabled = isFreemiumEnabledResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    return onNewMessage(Message.HomeFailure)
                }

                sentryInteractor.finishTransaction(sentryTransaction)

                onNewMessage(Message.HomeSuccess(problemOfDayState, repetitionsState, isFreemiumEnabled))
                onNewMessage(Message.ReadyToLaunchNextProblemInTimer)
            }
            is Action.LaunchTimer -> {
                if (isTimerLaunched) {
                    return
                }

                isTimerLaunched = true

                flow {
                    var nextProblemIn = calculateNextProblemIn()

                    while (nextProblemIn > 0) {
                        delay(DELAY_ONE_MINUTE)
                        nextProblemIn -= DELAY_ONE_MINUTE.inWholeSeconds
                        emit(nextProblemIn)
                    }
                }
                    .onCompletion {
                        isTimerLaunched = false
                        onNewMessage(Message.NextProblemInTimerStopped)
                    }
                    .onEach { seconds ->
                        onNewMessage(Message.HomeNextProblemInUpdate(dateFormatter.hoursWithMinutesCount(seconds)))
                    }
                    .launchIn(actionScope)
            }
            is Action.GetMagicLink ->
                getLink(action.path, ::onNewMessage)
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {
                // no op
            }
        }
    }

    private suspend fun getProblemOfDayState(dailyStepId: Long?): Result<HomeFeature.ProblemOfDayState> =
        if (dailyStepId == null) {
            Result.success(HomeFeature.ProblemOfDayState.Empty)
        } else {
            val nextProblemIn = calculateNextProblemIn()
            stepInteractor
                .getStep(dailyStepId)
                .map { step ->
                    if (step.isCompleted) {
                        HomeFeature.ProblemOfDayState.Solved(step, dateFormatter.hoursWithMinutesCount(nextProblemIn))
                    } else {
                        HomeFeature.ProblemOfDayState.NeedToSolve(step, dateFormatter.hoursWithMinutesCount(nextProblemIn))
                    }
                }
        }

    private suspend fun getRepetitionsState(): Result<HomeFeature.RepetitionsState> =
        topicsRepetitionsInteractor
            .getTopicsRepetitionStatistics()
            .map { statistics ->
                if (statistics.recommendTodayCount > 0 || statistics.repeatedTodayCount > 0) {
                    HomeFeature.RepetitionsState.Available(statistics.recommendTodayCount)
                } else {
                    HomeFeature.RepetitionsState.Empty
                }
            }

    private suspend fun getLink(
        path: HyperskillUrlPath,
        onNewMessage: (Message) -> Unit
    ) {
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
}