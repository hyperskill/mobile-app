package org.hyperskill.app.track_selection.injection

import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.Action
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.Message
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface TrackSelectionListComponent {
    val trackSelectionListFeature: Feature<ViewState, Message, Action>
}