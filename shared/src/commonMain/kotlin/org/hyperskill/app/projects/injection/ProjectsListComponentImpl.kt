package org.hyperskill.app.projects.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.projects.presentation.ProjectsListFeature.Action
import org.hyperskill.app.projects.presentation.ProjectsListFeature.Message
import org.hyperskill.app.projects.presentation.ProjectsListFeature.ViewState
import org.hyperskill.app.projects.view.mapper.ProjectsListViewStateMapper
import ru.nobird.app.presentation.redux.feature.Feature

class ProjectsListComponentImpl(private val appGraph: AppGraph) : ProjectsListComponent {

    override fun projectsListFeature(trackId: Long): Feature<ViewState, Message, Action> =
        ProjectsListFeatureBuilder.build(
            trackId = trackId,
            trackRepository = appGraph.buildTrackDataComponent().trackRepository,
            currentStudyPlanStateRepository = appGraph.buildStudyPlanDataComponent().currentStudyPlanStateRepository,
            projectsRepository = appGraph.buildProjectsDataComponent().projectsRepository,
            progressesRepository = appGraph.buildProgressesDataComponent().progressesRepository,
            profileInteractor = appGraph.buildProfileDataComponent().profileInteractor,
            viewStateMapper = ProjectsListViewStateMapper(appGraph.commonComponent.resourceProvider)
        )
}