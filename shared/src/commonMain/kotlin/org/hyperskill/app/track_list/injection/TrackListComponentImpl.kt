package org.hyperskill.app.track_list.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.track_list.presentation.TrackListFeature
import org.hyperskill.app.track_list.view.TrackListViewState
import org.hyperskill.app.track_list.view.TrackListViewStateMapper
import ru.nobird.app.presentation.redux.feature.Feature

class TrackListComponentImpl(
    private val appGraph: AppGraph
) : TrackListComponent {
    override val trackListFeature: Feature<TrackListViewState, TrackListFeature.Message, TrackListFeature.Action>
        get() = TrackListFeatureBuilder.build(
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.buildTrackDataComponent().trackInteractor,
            appGraph.buildProgressesDataComponent().progressesInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.buildFreemiumDataComponent().freemiumInteractor,
            appGraph.stateRepositoriesComponent.currentStudyPlanStateRepository,
            TrackListViewStateMapper(appGraph.commonComponent.resourceProvider)
        )
}