package org.hyperskill.app.progresses.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.progresses.presentation.ProgressScreenActionDispatcher
import org.hyperskill.app.progresses.presentation.ProgressScreenFeature
import org.hyperskill.app.progresses.presentation.ProgressScreenReducer
import org.hyperskill.app.progresses.view.ProgressScreenViewState
import org.hyperskill.app.progresses.view.ProgressScreenViewStateMapper
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object ProgressScreenFeatureBuilder {
    fun build(
        currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
        trackInteractor: TrackInteractor,
        projectsRepository: ProjectsRepository,
        progressesInteractor: ProgressesInteractor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        viewStateMapper: ProgressScreenViewStateMapper
    ): Feature<ProgressScreenViewState, ProgressScreenFeature.Message, ProgressScreenFeature.Action> {
        val actionDispatcher = ProgressScreenActionDispatcher(
            config = ActionDispatcherOptions(),
            currentStudyPlanStateRepository = currentStudyPlanStateRepository,
            trackInteractor = trackInteractor,
            projectsRepository = projectsRepository,
            progressesInteractor = progressesInteractor,
            analyticInteractor = analyticInteractor,
            sentryInteractor = sentryInteractor
        )
        return ReduxFeature(
            initialState = ProgressScreenFeature.State(
                trackProgressState = ProgressScreenFeature.TrackProgressState.Idle,
                projectProgressState = ProgressScreenFeature.ProjectProgressState.Idle,
                isTrackProgressRefreshing = false,
                isProjectProgressRefreshing = false
            ),
            reducer = ProgressScreenReducer()
        ).wrapWithActionDispatcher(actionDispatcher)
            .transformState {
                viewStateMapper.map(it)
            }
    }
}