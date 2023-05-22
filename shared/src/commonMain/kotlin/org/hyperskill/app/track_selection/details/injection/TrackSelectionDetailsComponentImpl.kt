package org.hyperskill.app.track_selection.details.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Action
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Message
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

class TrackSelectionDetailsComponentImpl(
    private val appGraph: AppGraph
) : TrackSelectionDetailsComponent {
    override val trackSelectionDetailsFeature: Feature<ViewState, Message, Action>
        get() = TrackSelectionDetailsFeatureBuilder.build()
}