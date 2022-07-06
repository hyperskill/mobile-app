package org.hyperskill.app.track.presentation

import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class TrackViewModel(
    reduxViewContainer: ReduxViewContainer<TrackFeature.State, TrackFeature.Message, TrackFeature.Action.ViewAction>
) : ReduxViewModel<TrackFeature.State, TrackFeature.Message, TrackFeature.Action.ViewAction>(reduxViewContainer)