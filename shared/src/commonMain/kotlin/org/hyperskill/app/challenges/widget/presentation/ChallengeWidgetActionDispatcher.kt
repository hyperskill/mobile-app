package org.hyperskill.app.challenges.widget.presentation

import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.challenges.domain.repository.ChallengesRepository
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.Action
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.InternalAction
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.InternalMessage
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.Message
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.magic_links.domain.interactor.MagicLinksInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.step_completion.domain.flow.DailyStepCompletedFlow
import org.hyperskill.app.step_completion.domain.flow.TopicCompletedFlow
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ChallengeWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    solvedStepsSharedFlow: SharedFlow<Long>,
    topicCompletedFlow: TopicCompletedFlow,
    dailyStepCompletedFlow: DailyStepCompletedFlow,
    private val challengesRepository: ChallengesRepository,
    private val magicLinksInteractor: MagicLinksInteractor,
    private val sentryInteractor: SentryInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    private var timerJob: Job? = null

    companion object {
        private val TIMER_TICK_INTERVAL = 1.toDuration(DurationUnit.MINUTES)
    }

    init {
        solvedStepsSharedFlow
            .distinctUntilChanged()
            .onEach {
                onNewMessage(InternalMessage.StepSolved)
            }
            .launchIn(actionScope)

        dailyStepCompletedFlow.observe()
            .distinctUntilChanged()
            .onEach {
                onNewMessage(InternalMessage.DailyStepCompleted)
            }
            .launchIn(actionScope)

        topicCompletedFlow.observe()
            .distinctUntilChanged()
            .onEach {
                onNewMessage(InternalMessage.TopicCompleted)
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            InternalAction.FetchChallenges ->
                handleFetchChallengesAction(::onNewMessage)
            is InternalAction.CreateMagicLink ->
                handleCreateMagicLinkAction(action, ::onNewMessage)
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            InternalAction.LaunchTimer ->
                handleLaunchTimerAction(::onNewMessage)
            InternalAction.StopTimer ->
                handleStopTimerAction()
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchChallengesAction(onNewMessage: (Message) -> Unit) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildChallengeWidgetFeatureFetchChallenges(),
            onError = { InternalMessage.FetchChallengesError }
        ) {
            challengesRepository
                .getChallenges()
                .getOrThrow()
                .let(InternalMessage::FetchChallengesSuccess)
        }.let(onNewMessage)
    }

    private suspend fun handleCreateMagicLinkAction(
        action: InternalAction.CreateMagicLink,
        onNewMessage: (Message) -> Unit
    ) {
        magicLinksInteractor
            .createMagicLink(nextUrl = action.nextUrl)
            .fold(
                onSuccess = { magicLink ->
                    InternalMessage.CreateMagicLinkSuccess(url = magicLink.url)
                },
                onFailure = {
                    InternalMessage.CreateMagicLinkError
                }
            )
            .let(onNewMessage)
    }

    private fun handleLaunchTimerAction(onNewMessage: (Message) -> Unit) {
        if (timerJob != null) {
            return
        }

        timerJob = flow {
            while (true) {
                delay(TIMER_TICK_INTERVAL)
                emit(Unit)
            }
        }.onEach {
            onNewMessage(InternalMessage.TimerTick)
        }.launchIn(actionScope)
    }

    private fun handleStopTimerAction() {
        timerJob?.cancel()
        timerJob = null
    }
}