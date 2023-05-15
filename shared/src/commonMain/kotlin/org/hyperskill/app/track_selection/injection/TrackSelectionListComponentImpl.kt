package org.hyperskill.app.track_selection.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.Action
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.Message
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.ViewState
import org.hyperskill.app.track_selection.view.TrackSelectionListViewStateMapper
import ru.nobird.app.presentation.redux.feature.Feature

class TrackSelectionListComponentImpl(
    private val appGraph: AppGraph
) : TrackSelectionListComponent {
    override val trackListFeature: Feature<ViewState, Message, Action>
        get() = TrackSelectionListFeatureBuilder.build(
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.buildTrackDataComponent().trackInteractor,
            appGraph.buildProgressesDataComponent().progressesInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.stateRepositoriesComponent.currentStudyPlanStateRepository,
            TrackSelectionListViewStateMapper(
                resourceProvider = appGraph.commonComponent.resourceProvider,
                numbersFormatter = appGraph.commonComponent.numbersFormatter
            )
        )
}