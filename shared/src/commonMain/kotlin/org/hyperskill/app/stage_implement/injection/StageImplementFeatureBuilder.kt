package org.hyperskill.app.stage_implement.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.presentation.wrapWithAnalyticLogger
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.stage_implement.presentation.StageImplementActionDispatcher
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Action
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.InternalAction
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Message
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.ViewState
import org.hyperskill.app.stage_implement.presentation.StageImplementReducer
import org.hyperskill.app.stage_implement.view.mapper.StageImplementViewStateMapper
import org.hyperskill.app.stages.domain.interactor.StagesInteractor
import org.hyperskill.app.step_completion.domain.flow.StepCompletedFlow
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object StageImplementFeatureBuilder {
    private const val LOG_TAG = "StageImplementFeature"

    fun build(
        projectId: Long,
        stageId: Long,
        stagesInteractor: StagesInteractor,
        progressesInteractor: ProgressesInteractor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        resourceProvider: ResourceProvider,
        currentProfileStateRepository: CurrentProfileStateRepository,
        stepCompletedFlow: StepCompletedFlow,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<ViewState, Message, Action> {
        val analyticRoute = HyperskillAnalyticRoute.Projects.Stages.Implement(
            projectId = projectId,
            stageId = stageId
        )
        val stageImplementReducer = StageImplementReducer(analyticRoute)
            .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val stageImplementActionDispatcher = StageImplementActionDispatcher(
            ActionDispatcherOptions(),
            stepCompletedFlow,
            currentProfileStateRepository,
            stagesInteractor,
            progressesInteractor,
            sentryInteractor,
            resourceProvider,
        )

        val stageImplementViewStateMapper = StageImplementViewStateMapper(resourceProvider)

        return ReduxFeature(StageImplementFeature.State.Idle, stageImplementReducer)
            .wrapWithActionDispatcher(stageImplementActionDispatcher)
            .transformState(stageImplementViewStateMapper::mapState)
            .wrapWithAnalyticLogger(analyticInteractor) {
                (it as? InternalAction.LogAnalyticEvent)?.analyticEvent
            }
    }
}