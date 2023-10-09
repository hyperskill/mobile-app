package org.hyperskill.app.project_selection.details.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.Action
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.Message
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

class ProjectSelectionDetailsComponentImpl(
    private val appGraph: AppGraph
) : ProjectSelectionDetailsComponent {
    override fun projectSelectionDetailsFeature(
        projectSelectionDetailsParams: ProjectSelectionDetailsParams
    ): Feature<ViewState, Message, Action> {
        val profileComponent = appGraph.profileDataComponent
        return ProjectSelectionDetailsFeatureBuilder.build(
            params = projectSelectionDetailsParams,
            trackRepository = appGraph.buildTrackDataComponent().trackRepository,
            projectsRepository = appGraph.buildProjectsDataComponent().projectsRepository,
            progressesRepository = appGraph.buildProgressesDataComponent().progressesRepository,
            providersRepository = appGraph.buildProvidersDataComponent().providersRepository,
            profileRepository = profileComponent.profileRepository,
            currentProfileStateRepository = profileComponent.currentProfileStateRepository,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            resourceProvider = appGraph.commonComponent.resourceProvider,
            numbersFormatter = appGraph.commonComponent.numbersFormatter,
            dateFormatter = appGraph.commonComponent.dateFormatter,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
    }
}