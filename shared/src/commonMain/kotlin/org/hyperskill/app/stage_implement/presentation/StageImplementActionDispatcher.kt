package org.hyperskill.app.stage_implement.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.projects.domain.interactor.ProjectsInteractor
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
    private val stepsInteractor: StepInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        // TODO: Sentry
        when (action) {
            is Action.FetchStageImplement -> coroutineScope {
                val projectResult = async { projectsInteractor.getProject(action.projectId) }
                val stageResult = async { stagesInteractor.getStage(action.stageId) }

                val project = projectResult.await()
                    .getOrElse {
                        onNewMessage(Message.FetchStageImplementResult.NetworkError)
                        return@coroutineScope
                    }
                if (project.isDeprecated) {
                    onNewMessage(Message.FetchStageImplementResult.Deprecated(project))
                    return@coroutineScope
                }

                val stage = stageResult.await()
                    .getOrElse {
                        onNewMessage(Message.FetchStageImplementResult.NetworkError)
                        return@coroutineScope
                    }

                val step = stepsInteractor
                    .getStep(stage.stepId)
                    .getOrElse {
                        onNewMessage(Message.FetchStageImplementResult.NetworkError)
                        return@coroutineScope
                    }

                if (step.isIdeRequired()) {
                    onNewMessage(Message.FetchStageImplementResult.Unsupported)
                } else {
                    onNewMessage(Message.FetchStageImplementResult.Success(project, stage, step))
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