package org.hyperskill.app.track_selection.details.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.wrapWithAnalyticLogger
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.providers.domain.repository.ProvidersRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.track_selection.details.domain.repository.TrackSelectionDetailsRepository
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsActionDispatcher
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Action
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.InternalAction
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Message
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.ViewState
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsReducer
import org.hyperskill.app.track_selection.details.view.TrackSelectionDetailsViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object TrackSelectionDetailsFeatureBuilder {
    private const val LOG_TAG = "TrackSelectionDetailsFeature"

    fun build(
        params: TrackSelectionDetailsParams,
        resourceProvider: ResourceProvider,
        dateFormatter: SharedDateFormatter,
        currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
        trackSelectionDetailsRepository: TrackSelectionDetailsRepository,
        providersRepository: ProvidersRepository,
        sentryInteractor: SentryInteractor,
        profileRepository: ProfileRepository,
        currentProfileStateRepository: CurrentProfileStateRepository,
        analyticInteractor: AnalyticInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<ViewState, Message, Action> {
        val reducer = TrackSelectionDetailsReducer().wrapWithLogger(buildVariant, logger, LOG_TAG)
        val actionDispatcher = TrackSelectionDetailsActionDispatcher(
            config = ActionDispatcherOptions(),
            trackSelectionDetailsRepository = trackSelectionDetailsRepository,
            providersRepository = providersRepository,
            currentSubscriptionStateRepository = currentSubscriptionStateRepository,
            sentryInteractor = sentryInteractor,
            profileRepository = profileRepository,
            currentProfileStateRepository = currentProfileStateRepository
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
            .wrapWithAnalyticLogger(analyticInteractor) {
                (it as? InternalAction.LogAnalyticEvent)?.analyticEvent
            }
    }
}