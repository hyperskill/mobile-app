package org.hyperskill.app.track_selection.details.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsActionDispatcher
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Action
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Message
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.ViewState
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsReducer
import org.hyperskill.app.track_selection.details.view.TrackSelectionDetailsViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object TrackSelectionDetailsFeatureBuilder {
    fun build(): Feature<ViewState, Message, Action> {
        val reducer = TrackSelectionDetailsReducer()
        val actionDispatcher = TrackSelectionDetailsActionDispatcher(ActionDispatcherOptions())
        val viewStateMapper = TrackSelectionDetailsViewStateMapper()
        return ReduxFeature(TrackSelectionDetailsFeature.State.Idle, reducer)
            .wrapWithActionDispatcher(actionDispatcher)
            .transformState(viewStateMapper::map)
    }
}