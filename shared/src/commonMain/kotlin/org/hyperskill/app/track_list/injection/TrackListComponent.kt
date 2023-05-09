package org.hyperskill.app.track_list.injection

import org.hyperskill.app.track_list.presentation.TrackListFeature
import org.hyperskill.app.track_list.view.TrackListViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface TrackListComponent {
    val trackListFeature: Feature<TrackListViewState, TrackListFeature.Message, TrackListFeature.Action>
}