package org.hyperskill.app.stage_implement.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.stage_implement.presentation.StageImplementActionDispatcher
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature
import org.hyperskill.app.stage_implement.presentation.StageImplementReducer
import org.hyperskill.app.stage_implement.view.mapper.StageImplementViewStateMapper
import org.hyperskill.app.stages.domain.interactor.StagesInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object StageImplementFeatureBuilder {
    fun build(
        projectId: Long,
        stageId: Long,
        stagesInteractor: StagesInteractor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        resourceProvider: ResourceProvider
    ): Feature<StageImplementFeature.ViewState, StageImplementFeature.Message, StageImplementFeature.Action> {
        val analyticRoute = HyperskillAnalyticRoute.Projects.Stages.Implement(projectId = projectId, stageId = stageId)
        val stageImplementReducer = StageImplementReducer(analyticRoute)

        val stageImplementActionDispatcher = StageImplementActionDispatcher(
            ActionDispatcherOptions(),
            stagesInteractor,
            analyticInteractor,
            sentryInteractor
        )

        val stageImplementViewStateMapper = StageImplementViewStateMapper(resourceProvider)

        return ReduxFeature(StageImplementFeature.State.Idle, stageImplementReducer)
            .wrapWithActionDispatcher(stageImplementActionDispatcher)
            .transformState(stageImplementViewStateMapper::mapState)
    }
}