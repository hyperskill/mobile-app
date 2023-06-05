package org.hyperskill.app.track_selection.details.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.providers.domain.repository.ProvidersRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Action
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.FetchAdditionalInfoResult
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class TrackSelectionDetailsActionDispatcher(
    config: ActionDispatcherOptions,
    private val providersRepository: ProvidersRepository,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val sentryInteractor: SentryInteractor,
    private val profileInteractor: ProfileInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is TrackSelectionDetailsFeature.InternalAction.FetchAdditionalInfo -> {
                val transaction =
                    HyperskillSentryTransactionBuilder.buildTrackSelectionDetailsScreenRemoteDataLoading()
                sentryInteractor.startTransaction(transaction)
                val message = fetchProvidersAndSubscription(action)
                    .getOrElse {
                        sentryInteractor.finishTransaction(transaction, it)
                        onNewMessage(TrackSelectionDetailsFeature.TrackSelectionResult.Error)
                        return
                    }
                sentryInteractor.finishTransaction(transaction)
                onNewMessage(message)
            }
            is TrackSelectionDetailsFeature.InternalAction.SelectTrack -> {
                val currentProfile = profileInteractor
                    .getCurrentProfile()
                    .getOrElse {
                        onNewMessage(TrackSelectionDetailsFeature.TrackSelectionResult.Error)
                        return
                    }

                profileInteractor.selectTrack(currentProfile.id, action.trackId)
                    .getOrElse {
                        return onNewMessage(TrackSelectionDetailsFeature.TrackSelectionResult.Error)
                    }

                onNewMessage(TrackSelectionDetailsFeature.TrackSelectionResult.Success)
            }
            is TrackSelectionDetailsFeature.InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.event)
            }
            else -> {
                // no-op
            }
        }
    }

    private suspend fun fetchProvidersAndSubscription(
        action: TrackSelectionDetailsFeature.InternalAction.FetchAdditionalInfo
    ): Result<FetchAdditionalInfoResult.Success> =
        coroutineScope {
            runCatching {
                val providersDeferred = async {
                    providersRepository
                        .getProviders(action.providerIds, action.forceLoadFromNetwork)
                        .getOrThrow()
                }
                val subscriptionDeferred = async {
                    currentSubscriptionStateRepository
                        .getState(action.forceLoadFromNetwork)
                        .getOrThrow()
                }
                val profileDeferred = async {
                    profileInteractor
                        .getCurrentProfile(forceLoadFromNetwork = false)
                        .getOrThrow()
                }
                FetchAdditionalInfoResult.Success(
                    subscriptionType = subscriptionDeferred.await().type,
                    profile = profileDeferred.await(),
                    providers = providersDeferred.await()
                )
            }
        }
}