package org.hyperskill.app.stage_implement.presentation

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.SharedResources
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.repository.updateState
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.profile.domain.model.copy
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Action
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Message
import org.hyperskill.app.stages.domain.interactor.StagesInteractor
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class StageImplementActionDispatcher(
    config: ActionDispatcherOptions,
    private val stagesInteractor: StagesInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val resourceProvider: ResourceProvider,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    submissionRepository: SubmissionRepository
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    companion object {
        const val STAGE_COMPLETION_GEMS_REWARD = 15
        const val PROJECT_COMPLETION_GEMS_REWARD = 25
    }

    init {
        submissionRepository.solvedStepsMutableSharedFlow
            .onEach {
                onNewMessage(Message.StepSolved(it))
            }
            .launchIn(actionScope)
    }
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchStageImplement -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStageImplementScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                stagesInteractor.getStage(action.stageId)
                    .fold(
                        onSuccess = { stage ->
                            sentryInteractor.finishTransaction(sentryTransaction)
                            onNewMessage(Message.FetchStageImplementResult.Success(action.projectId, stage))
                        },
                        onFailure = { exception ->
                            sentryInteractor.finishTransaction(sentryTransaction, exception)
                            onNewMessage(Message.FetchStageImplementResult.NetworkError)
                        }
                    )
            }
            is Action.CheckStageCompletionStatus -> {
                val stage = action.stage

                val projectProgress = progressesInteractor
                    .getProjectProgress(stage.projectId, forceLoadFromRemote = true)
                    .getOrElse { return }

                if (projectProgress.isCompleted) {
                    onNewMessage(
                        Message.ProjectCompleted(
                            stageCompletionGemsReward = STAGE_COMPLETION_GEMS_REWARD,
                            projectCompletionGemsReward = PROJECT_COMPLETION_GEMS_REWARD
                        )
                    )
                    currentProfileStateRepository.updateState { currentProfile ->
                        currentProfile.copy(
                            hypercoinsBalance = currentProfile.gamification.hypercoinsBalance +
                                STAGE_COMPLETION_GEMS_REWARD +
                                PROJECT_COMPLETION_GEMS_REWARD
                        )
                    }
                } else if (projectProgress.completedStages.contains(stage.id)) {
                    onNewMessage(
                        Message.StageCompleted(
                            title = resourceProvider.getString(
                                SharedResources.strings.stage_completed_modal_title,
                                stage.stepIndex ?: 1,
                                stage.projectStagesCount
                            ),
                            stageCompletionGemsReward = STAGE_COMPLETION_GEMS_REWARD
                        )
                    )

                    currentProfileStateRepository.updateState { currentProfile ->
                        currentProfile.copy(
                            hypercoinsBalance = currentProfile.gamification.hypercoinsBalance +
                                STAGE_COMPLETION_GEMS_REWARD
                        )
                    }
                }
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {
                // no op
            }
        }
    }
}