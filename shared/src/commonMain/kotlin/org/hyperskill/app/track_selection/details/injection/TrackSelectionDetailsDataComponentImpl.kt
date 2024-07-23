package org.hyperskill.app.track_selection.details.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.track_selection.details.cache.TrackSelectionDetailsCacheDataSourceImpl
import org.hyperskill.app.track_selection.details.data.repository.TrackSelectionDetailsRepositoryImpl
import org.hyperskill.app.track_selection.details.data.source.TrackSelectionDetailsCacheDataSource
import org.hyperskill.app.track_selection.details.domain.repository.TrackSelectionDetailsRepository

internal class TrackSelectionDetailsDataComponentImpl(
    appGraph: AppGraph
) : TrackSelectionDetailsDataComponent {
    private val trackSelectionDetailsCacheDataSource: TrackSelectionDetailsCacheDataSource =
        TrackSelectionDetailsCacheDataSourceImpl(
            json = appGraph.commonComponent.json,
            settings = appGraph.commonComponent.settings
        )

    override val trackSelectionDetailsRepository: TrackSelectionDetailsRepository
        get() = TrackSelectionDetailsRepositoryImpl(trackSelectionDetailsCacheDataSource)
}