package org.hyperskill.app.track_selection.details.injection

import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Action
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Message
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface TrackSelectionDetailsComponent {
    fun trackSelectionDetailsFeature(
        trackSelectionDetailsParams: TrackSelectionDetailsParams
    ): Feature<ViewState, Message, Action>
}