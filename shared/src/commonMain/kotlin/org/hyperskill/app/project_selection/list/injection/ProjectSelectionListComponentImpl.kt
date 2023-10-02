package org.hyperskill.app.project_selection.list.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.Action
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.Message
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.ViewState
import org.hyperskill.app.project_selection.list.view.mapper.ProjectSelectionListViewStateMapper
import ru.nobird.app.presentation.redux.feature.Feature

class ProjectSelectionListComponentImpl(private val appGraph: AppGraph) : ProjectSelectionListComponent {

    override fun projectSelectionListFeature(
        params: ProjectSelectionListParams
    ): Feature<ViewState, Message, Action> =
        ProjectSelectionListFeatureBuilder.build(
            params = params,
            trackRepository = appGraph.buildTrackDataComponent().trackRepository,
            currentStudyPlanStateRepository = appGraph.buildStudyPlanDataComponent().currentStudyPlanStateRepository,
            projectsRepository = appGraph.buildProjectsDataComponent().projectsRepository,
            progressesRepository = appGraph.buildProgressesDataComponent().progressesRepository,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            viewStateMapper = ProjectSelectionListViewStateMapper(
                resourceProvider = appGraph.commonComponent.resourceProvider,
                numbersFormatter = appGraph.commonComponent.numbersFormatter,
                dateFormatter = appGraph.commonComponent.dateFormatter
            ),
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}