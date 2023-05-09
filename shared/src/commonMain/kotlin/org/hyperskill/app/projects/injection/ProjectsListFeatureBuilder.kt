package org.hyperskill.app.projects.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.projects.presentation.ProjectsListActionDispatcher
import org.hyperskill.app.projects.presentation.ProjectsListFeature
import org.hyperskill.app.projects.presentation.ProjectsListFeature.Action
import org.hyperskill.app.projects.presentation.ProjectsListFeature.Message
import org.hyperskill.app.projects.presentation.ProjectsListFeature.ViewState
import org.hyperskill.app.projects.presentation.ProjectsListReducer
import org.hyperskill.app.projects.view.mapper.ProjectsListViewStateMapper
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.track.domain.repository.TrackRepository
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object ProjectsListFeatureBuilder {
    fun build(
        trackId: Long,
        trackRepository: TrackRepository,
        currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
        projectsRepository: ProjectsRepository,
        viewStateMapper: ProjectsListViewStateMapper
    ): Feature<ViewState, Message, Action> {
        val actionDispatcher = ProjectsListActionDispatcher(
            config = ActionDispatcherOptions(),
            trackRepository = trackRepository,
            currentStudyPlanStateRepository = currentStudyPlanStateRepository,
            projectsRepository = projectsRepository
        )
        return ReduxFeature(
            initialState = ProjectsListFeature.initialState(trackId),
            reducer = ProjectsListReducer()
        ).wrapWithActionDispatcher(actionDispatcher)
            .transformState {
                viewStateMapper.map(it.content)
            }
    }
}