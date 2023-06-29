package org.hyperskill.app.progresses.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.progresses.presentation.ProgressScreenFeature.Action
import org.hyperskill.app.progresses.presentation.ProgressScreenFeature.Message
import org.hyperskill.app.progresses.view.ProgressScreenViewState
import org.hyperskill.app.progresses.view.ProgressScreenViewStateMapper
import ru.nobird.app.presentation.redux.feature.Feature

class ProgressScreenComponentImpl(
    private val appGraph: AppGraph
) : ProgressScreenComponent {
    override val progressScreenFeature: Feature<ProgressScreenViewState, Message, Action>
        get() = ProgressScreenFeatureBuilder.build(
            appGraph.stateRepositoriesComponent.currentStudyPlanStateRepository,
            trackInteractor = appGraph.buildTrackDataComponent().trackInteractor,
            projectsRepository = appGraph.buildProjectsDataComponent().projectsRepository,
            progressesInteractor = appGraph.buildProgressesDataComponent().progressesInteractor,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            viewStateMapper = ProgressScreenViewStateMapper(appGraph.commonComponent.dateFormatter)
        )
}