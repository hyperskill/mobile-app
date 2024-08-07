package org.hyperskill.app.track_selection.list.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Action
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Message
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.ViewState
import org.hyperskill.app.track_selection.list.view.TrackSelectionListViewStateMapper
import ru.nobird.app.presentation.redux.feature.Feature

internal class TrackSelectionListComponentImpl(
    private val appGraph: AppGraph
) : TrackSelectionListComponent {
    override fun trackSelectionListFeature(
        params: TrackSelectionListParams
    ): Feature<ViewState, Message, Action> {
        val trackSelectionDetailsDataComponent = appGraph.buildTrackSelectionDetailsDataComponent()
        return TrackSelectionListFeatureBuilder.build(
            params = params,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            trackInteractor = appGraph.buildTrackDataComponent().trackInteractor,
            progressesInteractor = appGraph.buildProgressesDataComponent().progressesInteractor,
            trackSelectionDetailsRepository = trackSelectionDetailsDataComponent.trackSelectionDetailsRepository,
            currentStudyPlanStateRepository = appGraph.stateRepositoriesComponent.currentStudyPlanStateRepository,
            trackListViewStateMapper = TrackSelectionListViewStateMapper(
                numbersFormatter = appGraph.commonComponent.numbersFormatter,
                dateFormatter = appGraph.commonComponent.dateFormatter
            ),
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
    }
}