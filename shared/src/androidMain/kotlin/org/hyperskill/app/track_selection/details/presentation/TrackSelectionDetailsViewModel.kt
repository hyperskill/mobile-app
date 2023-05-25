package org.hyperskill.app.track_selection.details.presentation

import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Action.ViewAction
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Message
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.ViewState
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class TrackSelectionDetailsViewModel(
    reduxViewContainer: ReduxViewContainer<ViewState, Message, ViewAction>
) : ReduxViewModel<ViewState, Message, ViewAction>(reduxViewContainer) {
    init {
        onNewMessage(Message.Initialize)
    }
}