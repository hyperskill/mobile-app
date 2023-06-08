package org.hyperskill.app.track_selection.list.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformTrackSelectionListComponentImpl(
    private val trackSelectionListComponent: TrackSelectionListComponent,
    private val params: TrackSelectionListParams
) : PlatformTrackSelectionListComponent {

    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            viewModelMap = mapOf(
                TrackSelectionListViewModel::class.java to {
                    TrackSelectionListViewModel(
                        trackSelectionListComponent
                            .trackSelectionListFeature(params)
                            .wrapWithViewContainer()
                    )
                }
            )
        )
}