package org.hyperskill.app.track_selection.details.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.providers.domain.repository.ProvidersRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.track_selection.details.domain.repository.TrackSelectionDetailsRepository
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Action
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.InternalAction
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class TrackSelectionDetailsActionDispatcher(
    config: ActionDispatcherOptions,
    private val trackSelectionDetailsRepository: TrackSelectionDetailsRepository,
    private val providersRepository: ProvidersRepository,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val sentryInteractor: SentryInteractor,
    private val profileRepository: ProfileRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchAdditionalInfo ->
                handleFetchAdditionalInfoAction(action, ::onNewMessage)
            is InternalAction.SelectTrack ->
                handleSelectTrackAction(action, ::onNewMessage)
            else -> {
                // no-op
            }
        }
    }

    private suspend fun handleSelectTrackAction(
        action: InternalAction.SelectTrack,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildTrackSelectionDetailsScreenSelectTrack(),
            onError = { TrackSelectionDetailsFeature.TrackSelectionResult.Error }
        ) {
            val currentProfile = currentProfileStateRepository
                .getState()
                .getOrThrow()

            val newProfile = profileRepository
                .selectTrack(profileId = currentProfile.id, trackId = action.trackId)
                .getOrThrow()
            currentProfileStateRepository.updateState(newProfile)

            trackSelectionDetailsRepository.incrementTrackSelectionCount(trackId = action.trackId)
            currentProfile.trackId?.let {
                if (!trackSelectionDetailsRepository.getTracksSelectionCountMap().containsKey(it)) {
                    trackSelectionDetailsRepository.incrementTrackSelectionCount(trackId = it)
                }
            }

            TrackSelectionDetailsFeature.TrackSelectionResult.Success
        }.let(onNewMessage)
    }

    private suspend fun handleFetchAdditionalInfoAction(
        action: InternalAction.FetchAdditionalInfo,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildTrackSelectionDetailsScreenRemoteDataLoading(),
            onError = { TrackSelectionDetailsFeature.FetchAdditionalInfoResult.Error }
        ) {
            coroutineScope {
                val providersDeferred = async {
                    providersRepository.getProviders(action.providerIds, action.forceLoadFromNetwork)
                }
                val subscriptionDeferred = async {
                    currentSubscriptionStateRepository.getState(action.forceLoadFromNetwork)
                }
                val profileDeferred = async {
                    currentProfileStateRepository.getState(forceUpdate = false)
                }
                TrackSelectionDetailsFeature.FetchAdditionalInfoResult.Success(
                    subscriptionType = subscriptionDeferred.await().getOrThrow().type,
                    profile = profileDeferred.await().getOrThrow(),
                    providers = providersDeferred.await().getOrThrow()
                )
            }
        }.let(onNewMessage)
    }
}