package org.hyperskill.app.step.presentation

import co.touchlab.kermit.Logger
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.presentation.StepFeature.Action
import org.hyperskill.app.step.presentation.StepFeature.InternalAction
import org.hyperskill.app.step.presentation.StepFeature.InternalMessage
import org.hyperskill.app.step.presentation.StepFeature.Message
import org.hyperskill.app.step_completion.domain.flow.StepCompletedFlow
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class StepActionDispatcher(
    config: ActionDispatcherOptions,
    stepCompletedFlow: StepCompletedFlow,
    private val stepInteractor: StepInteractor,
    private val nextLearningActivityStateRepository: NextLearningActivityStateRepository,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val logger: Logger
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    companion object {
        private val LOG_STEP_SOLVING_TIME_DELAY_DURATION: Duration = 10.toDuration(DurationUnit.SECONDS)
    }

    init {
        stepCompletedFlow
            .observe()
            .onEach { stepId ->
                onNewMessage(InternalMessage.StepCompleted(stepId))
            }
            .launchIn(actionScope)
    }

    private var logStepSolvingTimeJob: Job? = null
    private var stepSolvingInitialTime: Instant? = null

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchStep ->
                handleFetchStepAction(action, ::onNewMessage)
            is InternalAction.ViewStep ->
                stepInteractor.viewStep(action.stepId, action.stepContext)
            is InternalAction.UpdateNextLearningActivityState ->
                handleUpdateNextLearningActivityStateAction(action)
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            is InternalAction.StartSolvingTimer ->
                handleStartStepSolvingTimeLogging()
            is InternalAction.StopSolvingTimer ->
                handleStopStepSolvingTimeLogging()
            is InternalAction.LogSolvingTime ->
                handleLogSolvingTime(action, stepSolvingInitialTime)
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchStepAction(
        action: InternalAction.FetchStep,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildStepScreenRemoteDataLoading(),
            onError = { Message.StepLoaded.Error }
        ) {
            val step = stepInteractor
                .getStep(action.stepRoute.stepId)
                .getOrThrow()
            Message.StepLoaded.Success(step)
        }.let(onNewMessage)
    }

    private suspend fun handleUpdateNextLearningActivityStateAction(
        action: InternalAction.UpdateNextLearningActivityState
    ) {
        val currentNextLearningActivityState = nextLearningActivityStateRepository
            .getStateWithSource(forceUpdate = false)
            .getOrElse { return }

        if (currentNextLearningActivityState.usedDataSourceType == DataSourceType.REMOTE) {
            return
        }

        val isInTheSameTopic = currentNextLearningActivityState.state?.topicId == action.step.topic
        val isStepNotCurrent = currentNextLearningActivityState.state?.targetId != action.step.id

        if (isInTheSameTopic && isStepNotCurrent) {
            nextLearningActivityStateRepository.reloadState()
        }
    }

    private suspend fun handleStartStepSolvingTimeLogging() {
        if (logStepSolvingTimeJob != null) return
        stepSolvingInitialTime = Clock.System.now()
        coroutineScope {
            logStepSolvingTimeJob = launchStepSolvingTimer().apply {
                invokeOnCompletion(::onStepSolvingTimerCancellation)
            }
        }
    }

    private fun CoroutineScope.launchStepSolvingTimer(): Job =
        launch {
            while (isActive) {
                delay(LOG_STEP_SOLVING_TIME_DELAY_DURATION)
                onNewMessage(InternalMessage.SolvingTimerFired)
            }
        }

    private fun onStepSolvingTimerCancellation(e: Throwable?) {
        logStepSolvingTimeJob = null
        if (e != null && e !is CancellationException) {
            logger.e(e) { "Log step solving time is failed!" }
        }
    }

    private fun handleStopStepSolvingTimeLogging() {
        logStepSolvingTimeJob?.cancel()
    }

    private suspend fun handleLogSolvingTime(
        action: InternalAction.LogSolvingTime,
        stepSolvingInitialTime: Instant?
    ) {
        if (stepSolvingInitialTime == null) return
        val now = Clock.System.now()
        val duration = now - stepSolvingInitialTime
        this.stepSolvingInitialTime = now

        /**
         * Is used to be not canceled if the action scope is canceled.
         */
        withContext(NonCancellable) {
            stepInteractor.logStepSolvingTime(
                stepId = action.stepId,
                duration = duration
            ).onFailure { e ->
                logger.e(e) { "Failed to log step solving time" }
            }
        }
    }
}