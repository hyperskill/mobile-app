package org.hyperskill.app.project_selection.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.Action
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.Message
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.ViewState
import org.hyperskill.app.project_selection.view.mapper.ProjectSelectionListViewStateMapper
import ru.nobird.app.presentation.redux.feature.Feature

class ProjectSelectionListComponentImpl(private val appGraph: AppGraph) : ProjectsSelectionListComponent {

    override fun projectSelectionListFeature(trackId: Long): Feature<ViewState, Message, Action> =
        ProjectSelectionListFeatureBuilder.build(
            trackId = trackId,
            trackRepository = appGraph.buildTrackDataComponent().trackRepository,
            currentStudyPlanStateRepository = appGraph.buildStudyPlanDataComponent().currentStudyPlanStateRepository,
            projectsRepository = appGraph.buildProjectsDataComponent().projectsRepository,
            progressesRepository = appGraph.buildProgressesDataComponent().progressesRepository,
            profileInteractor = appGraph.buildProfileDataComponent().profileInteractor,
            viewStateMapper = ProjectSelectionListViewStateMapper(appGraph.commonComponent.resourceProvider),
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor
        )
}