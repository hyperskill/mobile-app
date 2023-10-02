package org.hyperskill.app.progress_screen.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.progress_screen.presentation.ProgressScreenFeature.Action
import org.hyperskill.app.progress_screen.presentation.ProgressScreenFeature.Message
import org.hyperskill.app.progress_screen.view.ProgressScreenViewState
import org.hyperskill.app.progress_screen.view.ProgressScreenViewStateMapper
import ru.nobird.app.presentation.redux.feature.Feature

class ProgressScreenComponentImpl(
    private val appGraph: AppGraph
) : ProgressScreenComponent {
    override val progressScreenFeature: Feature<ProgressScreenViewState, Message, Action>
        get() = ProgressScreenFeatureBuilder.build(
            appGraph.stateRepositoriesComponent.currentStudyPlanStateRepository,
            appGraph.profileDataComponent.currentProfileStateRepository,
            trackInteractor = appGraph.buildTrackDataComponent().trackInteractor,
            projectsRepository = appGraph.buildProjectsDataComponent().projectsRepository,
            progressesInteractor = appGraph.buildProgressesDataComponent().progressesInteractor,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            viewStateMapper = ProgressScreenViewStateMapper(
                dateFormatter = appGraph.commonComponent.dateFormatter,
                resourceProvider = appGraph.commonComponent.resourceProvider
            ),
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}