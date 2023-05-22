package org.hyperskill.app.track_selection.presentation

import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Action.ViewAction
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Message
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.ViewState
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class TrackSelectionListViewModel(
    reduxViewContainer: ReduxViewContainer<ViewState, Message, ViewAction>
) : ReduxViewModel<ViewState, Message, ViewAction>(reduxViewContainer) {
    init {
        onNewMessage(Message.Initialize)
    }
}