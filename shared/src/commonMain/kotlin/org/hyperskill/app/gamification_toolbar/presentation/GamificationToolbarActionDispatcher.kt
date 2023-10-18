package org.hyperskill.app.gamification_toolbar.presentation

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.gamification_toolbar.domain.repository.GamificationToolbarRepository
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Action
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Message
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.domain.repository.observeHypercoinsBalance
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.step_completion.domain.flow.TopicCompletedFlow
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository
import org.hyperskill.app.streaks.domain.flow.StreakFlow
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class GamificationToolbarActionDispatcher(
    config: ActionDispatcherOptions,
    submissionRepository: SubmissionRepository,
    streakFlow: StreakFlow,
    currentProfileStateRepository: CurrentProfileStateRepository,
    currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
    topicCompletedFlow: TopicCompletedFlow,
    private val gamificationToolbarRepository: GamificationToolbarRepository,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        submissionRepository.solvedStepsSharedFlow
            .onEach { onNewMessage(Message.StepSolved) }
            .launchIn(actionScope)

        currentProfileStateRepository.observeHypercoinsBalance()
            .onEach { hypercoinsBalance ->
                onNewMessage(Message.HypercoinsBalanceChanged(hypercoinsBalance))
            }
            .launchIn(actionScope)

        streakFlow.observe()
            .distinctUntilChanged()
            .onEach { streak ->
                onNewMessage(Message.StreakChanged(streak))
            }
            .launchIn(actionScope)

        currentStudyPlanStateRepository.changes
            .distinctUntilChanged()
            .onEach { studyPlan ->
                onNewMessage(Message.StudyPlanChanged(studyPlan))
            }
            .launchIn(actionScope)

        topicCompletedFlow.observe()
            .distinctUntilChanged()
            .onEach {
                onNewMessage(Message.TopicCompleted)
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchGamificationToolbarData -> {
                val sentryTransaction = action.screen.fetchContentSentryTransaction
                sentryInteractor.startTransaction(sentryTransaction)

                val gamificationToolbarData = gamificationToolbarRepository
                    .getGamificationToolbarData()
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        return onNewMessage(Message.FetchGamificationToolbarDataError)
                    }

                sentryInteractor.finishTransaction(sentryTransaction)

                onNewMessage(
                    Message.FetchGamificationToolbarDataSuccess(gamificationToolbarData)
                )
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {
                // no op
            }
        }
    }
}