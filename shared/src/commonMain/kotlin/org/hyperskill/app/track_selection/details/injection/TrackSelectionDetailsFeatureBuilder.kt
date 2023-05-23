package org.hyperskill.app.track_selection.details.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.providers.domain.repository.ProvidersRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsActionDispatcher
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Action
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Message
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.ViewState
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsReducer
import org.hyperskill.app.track_selection.details.view.TrackSelectionDetailsViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object TrackSelectionDetailsFeatureBuilder {
    fun build(
        params: TrackSelectionDetailsParams,
        resourceProvider: ResourceProvider,
        freemiumInteractor: FreemiumInteractor,
        providersRepository: ProvidersRepository,
        sentryInteractor: SentryInteractor,
        profileInteractor: ProfileInteractor
    ): Feature<ViewState, Message, Action> {
        val reducer = TrackSelectionDetailsReducer()
        val actionDispatcher = TrackSelectionDetailsActionDispatcher(
            config = ActionDispatcherOptions(),
            providersRepository = providersRepository,
            freemiumInteractor = freemiumInteractor,
            sentryInteractor = sentryInteractor,
            profileInteractor = profileInteractor
        )
        val viewStateMapper = TrackSelectionDetailsViewStateMapper(
            resourceProvider = resourceProvider
        )
        val initialState = TrackSelectionDetailsFeature.initialState(
            trackWithProgress = params.trackWithProgress,
            isTrackSelected = params.isTrackSelected
        )
        return ReduxFeature(initialState, reducer)
            .wrapWithActionDispatcher(actionDispatcher)
            .transformState(viewStateMapper::map)
    }
}