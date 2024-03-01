package org.hyperskill.app.home.presentation

import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.utils.DateTimeUtils
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.home.presentation.HomeFeature.Action
import org.hyperskill.app.home.presentation.HomeFeature.InternalAction
import org.hyperskill.app.home.presentation.HomeFeature.InternalMessage
import org.hyperskill.app.home.presentation.HomeFeature.Message
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step_completion.domain.flow.StepCompletedFlow
import org.hyperskill.app.step_completion.domain.flow.TopicCompletedFlow
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.subscriptions.domain.repository.areProblemsLimited
import org.hyperskill.app.topics_repetitions.domain.flow.TopicRepeatedFlow
import org.hyperskill.app.topics_repetitions.domain.interactor.TopicsRepetitionsInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class HomeActionDispatcher(
    config: ActionDispatcherOptions,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val topicsRepetitionsInteractor: TopicsRepetitionsInteractor,
    private val stepInteractor: StepInteractor,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val dateFormatter: SharedDateFormatter,
    topicRepeatedFlow: TopicRepeatedFlow,
    topicCompletedFlow: TopicCompletedFlow,
    stepCompletedFlow: StepCompletedFlow
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    private var isTimerLaunched: Boolean = false

    companion object {
        private val DELAY_ONE_MINUTE = 1.toDuration(DurationUnit.MINUTES)
    }

    init {
        stepCompletedFlow.observe()
            .onEach { onNewMessage(InternalMessage.StepQuizSolved(it)) }
            .launchIn(actionScope)

        topicRepeatedFlow.observe()
            .onEach { onNewMessage(InternalMessage.TopicRepeated) }
            .launchIn(actionScope)

        topicCompletedFlow.observe()
            .onEach { onNewMessage(InternalMessage.TopicCompleted) }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchHomeScreenData ->
                handleFetchHomeScreenData(::onNewMessage)
            is InternalAction.FetchProblemOfDayState ->
                handleFetchProblemOfDayState(::onNewMessage)
            is InternalAction.LaunchTimer -> {
                if (isTimerLaunched) {
                    return
                }

                isTimerLaunched = true

                flow {
                    var nextProblemIn = DateTimeUtils.secondsUntilStartOfNextDayInNewYork()

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
                        onNewMessage(
                            Message.HomeNextProblemInUpdate(dateFormatter.formatHoursWithMinutesCount(seconds))
                        )
                    }
                    .launchIn(actionScope)
            }
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchHomeScreenData(onNewMessage: (Message) -> Unit) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildHomeScreenRemoteDataLoading(),
            onError = { setOf(Message.HomeFailure) }
        ) {
            coroutineScope {
                val currentProfile = currentProfileStateRepository
                    .getState(forceUpdate = true) // ALTAPPS-303: Get from remote to get a relevant problem of the day
                    .getOrThrow()

                val problemOfDayStateResult = async { getProblemOfDayState(currentProfile.dailyStep) }
                val repetitionsStateResult = async { getRepetitionsState() }
                val areProblemsLimited = async { currentSubscriptionStateRepository.areProblemsLimited() }

                setOf(
                    Message.HomeSuccess(
                        problemOfDayState = problemOfDayStateResult.await().getOrThrow(),
                        repetitionsState = repetitionsStateResult.await().getOrThrow(),
                        areProblemsLimited = areProblemsLimited.await()
                    ),
                    Message.ReadyToLaunchNextProblemInTimer
                )
            }
        }.forEach(onNewMessage)
    }

    private suspend fun handleFetchProblemOfDayState(onNewMessage: (Message) -> Unit) {
        val currentProfile = currentProfileStateRepository
            .getState(forceUpdate = true)
            .getOrElse { return onNewMessage(InternalMessage.FetchProblemOfDayStateResultError) }

        getProblemOfDayState(currentProfile.dailyStep)
            .fold(
                onSuccess = {
                    onNewMessage(InternalMessage.FetchProblemOfDayStateResultSuccess(it))
                },
                onFailure = {
                    onNewMessage(InternalMessage.FetchProblemOfDayStateResultError)
                }
            )
    }

    private suspend fun getProblemOfDayState(dailyStepId: Long?): Result<HomeFeature.ProblemOfDayState> =
        if (dailyStepId == null) {
            Result.success(HomeFeature.ProblemOfDayState.Empty)
        } else {
            val nextProblemIn = DateTimeUtils.secondsUntilStartOfNextDayInNewYork()
            stepInteractor
                .getStep(dailyStepId)
                .map { step ->
                    if (step.isCompleted) {
                        HomeFeature.ProblemOfDayState.Solved(
                            step = step,
                            nextProblemIn = dateFormatter.formatHoursWithMinutesCount(nextProblemIn)
                        )
                    } else {
                        HomeFeature.ProblemOfDayState.NeedToSolve(
                            step,
                            dateFormatter.formatHoursWithMinutesCount(nextProblemIn)
                        )
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
}