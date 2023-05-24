package org.hyperskill.app.track_selection.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListComponent
import org.hyperskill.app.track_selection.presentation.TrackSelectionListViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformTrackSelectionListComponentImpl(
    private val trackSelectionListComponent: TrackSelectionListComponent
) : PlatformTrackSelectionListComponent {

    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            viewModelMap = mapOf(
                TrackSelectionListViewModel::class.java to {
                    TrackSelectionListViewModel(
                        reduxViewContainer = trackSelectionListComponent
                            .trackSelectionListFeature.wrapWithViewContainer()
                    )
                }
            )
        )
}