package org.hyperskill.app.stage_implement.presentation

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.SharedResources
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.repository.updateState
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.hypercoins.domain.HypercoinsAwards
import org.hyperskill.app.profile.domain.model.copy
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Action
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.InternalAction
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.InternalMessage
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Message
import org.hyperskill.app.stages.domain.interactor.StagesInteractor
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class StageImplementActionDispatcher(
    config: ActionDispatcherOptions,
    submissionRepository: SubmissionRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val stagesInteractor: StagesInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val resourceProvider: ResourceProvider
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        submissionRepository.solvedStepsMutableSharedFlow
            .onEach { onNewMessage(InternalMessage.StepSolved(it)) }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchStageImplement -> {
                handleFetchStageImplementAction(action, ::onNewMessage)
            }
            is InternalAction.CheckStageCompletionStatus -> {
                handleCheckStageCompletionStatusAction(action, ::onNewMessage)
            }
            is InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.analyticEvent)
            }
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchStageImplementAction(
        action: InternalAction.FetchStageImplement,
        onNewMessage: (Message) -> Unit
    ) {
        val sentryTransaction = HyperskillSentryTransactionBuilder.buildStageImplementScreenRemoteDataLoading()
        sentryInteractor.startTransaction(sentryTransaction)

        stagesInteractor.getStage(action.stageId)
            .fold(
                onSuccess = { stage ->
                    sentryInteractor.finishTransaction(sentryTransaction)
                    onNewMessage(InternalMessage.FetchStageImplementSuccess(action.projectId, stage))
                },
                onFailure = { exception ->
                    sentryInteractor.finishTransaction(sentryTransaction, exception)
                    onNewMessage(InternalMessage.FetchStageImplementFailure)
                }
            )
    }

    private suspend fun handleCheckStageCompletionStatusAction(
        action: InternalAction.CheckStageCompletionStatus,
        onNewMessage: (Message) -> Unit
    ) {
        val stage = action.stage

        val projectProgress = progressesInteractor
            .getProjectProgress(projectId = stage.projectId, forceLoadFromRemote = true)
            .getOrElse { return }

        if (projectProgress.isCompleted) {
            onNewMessage(
                InternalMessage.ProjectCompleted(
                    stageAward = HypercoinsAwards.STAGE_COMPLETED,
                    projectAward = HypercoinsAwards.PROJECT_COMPLETED
                )
            )

            currentProfileStateRepository.updateState { currentProfile ->
                val newHypercoinsBalance =
                    currentProfile.gamification.hypercoinsBalance + HypercoinsAwards.PROJECT_COMPLETED_COMPOUND_SUM
                currentProfile.copy(hypercoinsBalance = newHypercoinsBalance)
            }
        } else if (projectProgress.completedStages.contains(stage.id)) {
            onNewMessage(
                InternalMessage.StageCompleted(
                    title = resourceProvider.getString(
                        SharedResources.strings.stage_completed_modal_title,
                        stage.stepIndex ?: 1,
                        stage.projectStagesCount
                    ),
                    stageAward = HypercoinsAwards.STAGE_COMPLETED
                )
            )

            currentProfileStateRepository.updateState { currentProfile ->
                val newHypercoinsBalance =
                    currentProfile.gamification.hypercoinsBalance + HypercoinsAwards.STAGE_COMPLETED
                currentProfile.copy(hypercoinsBalance = newHypercoinsBalance)
            }
        }
    }
}