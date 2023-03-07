package org.hyperskill.app.stage_implement.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.projects.domain.interactor.ProjectsInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Action
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Message
import org.hyperskill.app.stages.domain.interactor.StagesInteractor
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.domain.model.isIdeRequired
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class StageImplementActionDispatcher(
    config: ActionDispatcherOptions,
    private val projectsInteractor: ProjectsInteractor,
    private val stagesInteractor: StagesInteractor,
    private val stepInteractor: StepInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchStageImplement -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStageImplementScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                fetchStageImplement(action.projectId, action.stageId)
                    .fold(
                        onSuccess = { message ->
                            sentryInteractor.finishTransaction(sentryTransaction)
                            onNewMessage(message)
                        },
                        onFailure = { exception ->
                            sentryInteractor.finishTransaction(sentryTransaction, exception)
                            onNewMessage(Message.FetchStageImplementResult.NetworkError)
                        }
                    )
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {
                // no op
            }
        }
    }

    private suspend fun fetchStageImplement(
        projectId: Long,
        stageId: Long
    ): Result<Message.FetchStageImplementResult> =
        coroutineScope {
            kotlin.runCatching {
                val projectResult = async { projectsInteractor.getProject(projectId) }
                val stageResult = async { stagesInteractor.getStage(stageId) }

                val project = projectResult.await()
                    .getOrThrow()
                if (project.isDeprecated) {
                    return@runCatching Message.FetchStageImplementResult.Deprecated(project)
                }

                val stage = stageResult.await()
                    .getOrThrow()

                val step = stepInteractor
                    .getStep(stage.stepId)
                    .getOrThrow()

                if (step.isIdeRequired()) {
                    return@runCatching Message.FetchStageImplementResult.Unsupported
                } else {
                    return@runCatching Message.FetchStageImplementResult.Success(project, stage, step)
                }
            }
        }
}