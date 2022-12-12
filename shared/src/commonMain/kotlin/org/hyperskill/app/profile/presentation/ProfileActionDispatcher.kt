package org.hyperskill.app.profile.presentation

import kotlinx.coroutines.launch
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.presentation.ProfileFeature.Action
import org.hyperskill.app.profile.presentation.ProfileFeature.Message
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.streak.domain.interactor.StreakInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProfileActionDispatcher(
    config: ActionDispatcherOptions,
    private val profileInteractor: ProfileInteractor,
    private val streakInteractor: StreakInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val urlPathProcessor: UrlPathProcessor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        actionScope.launch {
            profileInteractor.solvedStepsSharedFlow.collect {
                onNewMessage(Message.StepQuizSolved)
            }
        }

        actionScope.launch {
            profileInteractor.observeHypercoinsBalance().collect {
                onNewMessage(Message.HypercoinsBalanceChanged(it))
            }
        }
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchCurrentProfile -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildProfileScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                val currentProfile = profileInteractor
                    .getCurrentProfile(sourceType = DataSourceType.REMOTE)
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        onNewMessage(Message.ProfileLoaded.Error)
                        return
                    }

                streakInteractor
                    .getStreaks(currentProfile.id)
                    .fold(
                        onSuccess = {
                            sentryInteractor.finishTransaction(sentryTransaction)
                            onNewMessage(
                                Message.ProfileLoaded.Success(
                                    profile = currentProfile,
                                    streak = it.firstOrNull()
                                )
                            )
                        },
                        onFailure = {
                            sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                            onNewMessage(Message.ProfileLoaded.Error)
                        }
                    )
            }
            is Action.FetchProfile -> {
                // TODO add code when GET on any profile is implemented
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            is Action.GetMagicLink ->
                getLink(action.path, ::onNewMessage)
            else -> {}
        }
    }

    private suspend fun getLink(path: HyperskillUrlPath, onNewMessage: (Message) -> Unit): Unit =
        urlPathProcessor.processUrlPath(path)
            .fold(
                onSuccess = { url ->
                    onNewMessage(Message.GetMagicLinkReceiveSuccess(url))
                },
                onFailure = {
                    onNewMessage(Message.GetMagicLinkReceiveFailure)
                }
            )
}