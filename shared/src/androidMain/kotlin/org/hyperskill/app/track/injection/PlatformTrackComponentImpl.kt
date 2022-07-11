package org.hyperskill.app.track.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.track.presentation.TrackViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformTrackComponentImpl(private val trackComponent: TrackComponent) : PlatformTrackComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(mapOf(TrackViewModel::class.java to { TrackViewModel(trackComponent.trackFeature.wrapWithViewContainer()) }))
}