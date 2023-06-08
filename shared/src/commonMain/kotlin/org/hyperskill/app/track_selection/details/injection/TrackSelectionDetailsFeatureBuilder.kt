package org.hyperskill.app.track_selection.details.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.providers.domain.repository.ProvidersRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
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
        dateFormatter: SharedDateFormatter,
        currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
        providersRepository: ProvidersRepository,
        sentryInteractor: SentryInteractor,
        profileInteractor: ProfileInteractor,
        analyticInteractor: AnalyticInteractor
    ): Feature<ViewState, Message, Action> {
        val reducer = TrackSelectionDetailsReducer()
        val actionDispatcher = TrackSelectionDetailsActionDispatcher(
            config = ActionDispatcherOptions(),
            providersRepository = providersRepository,
            currentSubscriptionStateRepository = currentSubscriptionStateRepository,
            sentryInteractor = sentryInteractor,
            profileInteractor = profileInteractor,
            analyticInteractor = analyticInteractor
        )
        val viewStateMapper = TrackSelectionDetailsViewStateMapper(
            resourceProvider = resourceProvider,
            dateFormatter = dateFormatter
        )
        val initialState = TrackSelectionDetailsFeature.initialState(
            trackWithProgress = params.trackWithProgress,
            isTrackSelected = params.isTrackSelected,
            isNewUserMode = params.isNewUserMode
        )
        return ReduxFeature(initialState, reducer)
            .wrapWithActionDispatcher(actionDispatcher)
            .transformState(viewStateMapper::map)
    }
}