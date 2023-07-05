package org.hyperskill.app.stage_implement.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.progress_screen.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.stage_implement.presentation.StageImplementActionDispatcher
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature
import org.hyperskill.app.stage_implement.presentation.StageImplementReducer
import org.hyperskill.app.stage_implement.view.mapper.StageImplementViewStateMapper
import org.hyperskill.app.stages.domain.interactor.StagesInteractor
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object StageImplementFeatureBuilder {
    fun build(
        projectId: Long,
        stageId: Long,
        stagesInteractor: StagesInteractor,
        progressesInteractor: ProgressesInteractor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        resourceProvider: ResourceProvider,
        currentProfileStateRepository: CurrentProfileStateRepository,
        submissionRepository: SubmissionRepository
    ): Feature<StageImplementFeature.ViewState, StageImplementFeature.Message, StageImplementFeature.Action> {
        val analyticRoute = HyperskillAnalyticRoute.Projects.Stages.Implement(projectId = projectId, stageId = stageId)
        val stageImplementReducer = StageImplementReducer(analyticRoute)

        val stageImplementActionDispatcher = StageImplementActionDispatcher(
            ActionDispatcherOptions(),
            submissionRepository,
            currentProfileStateRepository,
            stagesInteractor,
            progressesInteractor,
            analyticInteractor,
            sentryInteractor,
            resourceProvider,
        )

        val stageImplementViewStateMapper = StageImplementViewStateMapper(resourceProvider)

        return ReduxFeature(StageImplementFeature.State.Idle, stageImplementReducer)
            .wrapWithActionDispatcher(stageImplementActionDispatcher)
            .transformState(stageImplementViewStateMapper::mapState)
    }
}