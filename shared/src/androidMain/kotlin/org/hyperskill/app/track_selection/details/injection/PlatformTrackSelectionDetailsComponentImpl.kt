package org.hyperskill.app.track_selection.details.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformTrackSelectionDetailsComponentImpl(
    private val trackSelectionDetailsComponent: TrackSelectionDetailsComponent,
    private val params: TrackSelectionDetailsParams
) : PlatformTrackSelectionDetailsComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                TrackSelectionDetailsViewModel::class.java to {
                    TrackSelectionDetailsViewModel(
                        trackSelectionDetailsComponent
                            .trackSelectionDetailsFeature(params)
                            .wrapWithViewContainer()
                    )
                }
            )
        )
}