package org.hyperskill.app.track_selection.list.injection

import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Action
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Message
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface TrackSelectionListComponent {
    fun trackSelectionListFeature(params: TrackSelectionListParams): Feature<ViewState, Message, Action>
}