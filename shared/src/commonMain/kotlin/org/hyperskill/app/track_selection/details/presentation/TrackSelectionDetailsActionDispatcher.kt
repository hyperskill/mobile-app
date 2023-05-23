package org.hyperskill.app.track_selection.details.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.providers.domain.repository.ProvidersRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Action
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.FetchProvidersAndFreemiumStatusResult
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class TrackSelectionDetailsActionDispatcher(
    config: ActionDispatcherOptions,
    private val providersRepository: ProvidersRepository,
    private val freemiumInteractor: FreemiumInteractor,
    private val sentryInteractor: SentryInteractor,
    private val profileInteractor: ProfileInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is TrackSelectionDetailsFeature.InternalAction.FetchProvidersAndFreemiumStatus -> {
                val transaction =
                    HyperskillSentryTransactionBuilder.buildTrackSelectionDetailsScreenRemoteDataLoading()
                sentryInteractor.startTransaction(transaction)
                val providers =
                    providersRepository.getProviders(action.providerIds, action.forceLoadFromNetwork)
                        .getOrElse {
                            sentryInteractor.finishTransaction(transaction, throwable = it)
                            onNewMessage(FetchProvidersAndFreemiumStatusResult.Error)
                            return
                        }
                val isFreemiumEnabled = freemiumInteractor.isFreemiumEnabled()
                    .getOrElse {
                        sentryInteractor.finishTransaction(transaction, throwable = it)
                        onNewMessage(FetchProvidersAndFreemiumStatusResult.Error)
                        return
                    }

                sentryInteractor.finishTransaction(transaction)
                onNewMessage(
                    FetchProvidersAndFreemiumStatusResult.Success(
                        isFreemiumEnabled = isFreemiumEnabled,
                        providers = providers
                    )
                )
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
}