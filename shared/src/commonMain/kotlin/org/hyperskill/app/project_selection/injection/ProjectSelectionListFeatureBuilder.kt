package org.hyperskill.app.project_selection.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListActionDispatcher
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.Action
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.Message
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.ViewState
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListReducer
import org.hyperskill.app.project_selection.view.mapper.ProjectSelectionListViewStateMapper
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.track.domain.repository.TrackRepository
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object ProjectSelectionListFeatureBuilder {
    fun build(
        trackId: Long,
        trackRepository: TrackRepository,
        currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
        projectsRepository: ProjectsRepository,
        progressesRepository: ProgressesRepository,
        profileInteractor: ProfileInteractor,
        viewStateMapper: ProjectSelectionListViewStateMapper,
        sentryInteractor: SentryInteractor,
        analyticInteractor: AnalyticInteractor
    ): Feature<ViewState, Message, Action> {
        val actionDispatcher = ProjectSelectionListActionDispatcher(
            config = ActionDispatcherOptions(),
            trackRepository = trackRepository,
            currentStudyPlanStateRepository = currentStudyPlanStateRepository,
            projectsRepository = projectsRepository,
            progressesRepository = progressesRepository,
            profileInteractor = profileInteractor,
            sentryInteractor = sentryInteractor,
            analyticInteractor = analyticInteractor
        )
        return ReduxFeature(
            initialState = ProjectSelectionListFeature.initialState(trackId),
            reducer = ProjectSelectionListReducer()
        ).wrapWithActionDispatcher(actionDispatcher)
            .transformState {
                viewStateMapper.map(it.content)
            }
    }
}