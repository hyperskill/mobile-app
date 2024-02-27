package org.hyperskill.app.gamification_toolbar.presentation

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.gamification_toolbar.domain.repository.CurrentGamificationToolbarDataStateRepository
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Action
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.InternalAction
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.InternalMessage
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Message
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.step_completion.domain.flow.StepCompletedFlow
import org.hyperskill.app.step_completion.domain.flow.TopicCompletedFlow
import org.hyperskill.app.streaks.domain.flow.StreakFlow
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class GamificationToolbarActionDispatcher(
    config: ActionDispatcherOptions,
    stepCompletedFlow: StepCompletedFlow,
    streakFlow: StreakFlow,
    currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
    topicCompletedFlow: TopicCompletedFlow,
    private val currentGamificationToolbarDataStateRepository: CurrentGamificationToolbarDataStateRepository,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        stepCompletedFlow.observe()
            .onEach { onNewMessage(InternalMessage.StepSolved) }
            .launchIn(actionScope)

        streakFlow.observe()
            .distinctUntilChanged()
            .onEach { streak ->
                onNewMessage(InternalMessage.StreakChanged(streak))
            }
            .launchIn(actionScope)

        currentStudyPlanStateRepository.changes
            .distinctUntilChanged()
            .onEach { studyPlan ->
                onNewMessage(InternalMessage.StudyPlanChanged(studyPlan))
            }
            .launchIn(actionScope)

        topicCompletedFlow.observe()
            .distinctUntilChanged()
            .onEach {
                onNewMessage(InternalMessage.TopicCompleted)
            }
            .launchIn(actionScope)

        currentGamificationToolbarDataStateRepository.changes
            .distinctUntilChanged()
            .onEach { onNewMessage(InternalMessage.GamificationToolbarDataChanged(it)) }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchGamificationToolbarData ->
                handleFetchGamificationToolbarDataAction(action, ::onNewMessage)
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchGamificationToolbarDataAction(
        action: InternalAction.FetchGamificationToolbarData,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            action.screen.fetchContentSentryTransaction,
            onError = { InternalMessage.FetchGamificationToolbarDataError }
        ) {
            currentGamificationToolbarDataStateRepository
                .getState(forceUpdate = action.forceUpdate)
                .getOrThrow()
                .let(InternalMessage::FetchGamificationToolbarDataSuccess)
        }.let(onNewMessage)
    }
}