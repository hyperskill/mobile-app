package org.hyperskill.app.track_selection.details.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Action
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Message
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

class TrackSelectionDetailsComponentImpl(
    private val appGraph: AppGraph
) : TrackSelectionDetailsComponent {
    override fun trackSelectionDetailsFeature(
        trackSelectionDetailsParams: TrackSelectionDetailsParams
    ): Feature<ViewState, Message, Action> {
        val profileComponent = appGraph.buildProfileDataComponent()
        return TrackSelectionDetailsFeatureBuilder.build(
            trackSelectionDetailsParams,
            resourceProvider = appGraph.commonComponent.resourceProvider,
            dateFormatter = appGraph.commonComponent.dateFormatter,
            currentSubscriptionStateRepository = appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository,
            providersRepository = appGraph.buildProvidersDataComponent().providersRepository,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            profileInteractor = profileComponent.profileInteractor,
            currentProfileStateRepository = profileComponent.currentProfileStateRepository,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor
        )
    }
}