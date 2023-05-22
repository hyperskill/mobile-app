package org.hyperskill.app.track_selection.details.presentation

object TrackSelectionDetailsFeature {
    internal sealed interface State {
        object Idle : State
    }

    sealed interface ViewState

    sealed interface Message

    sealed interface Action
}