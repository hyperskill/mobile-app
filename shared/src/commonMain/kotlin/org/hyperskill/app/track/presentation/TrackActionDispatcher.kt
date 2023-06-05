package org.hyperskill.app.track.presentation

import kotlinx.coroutines.async
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.presentation.TrackFeature.Action
import org.hyperskill.app.track.presentation.TrackFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class TrackActionDispatcher(
    config: ActionDispatcherOptions,
    private val trackInteractor: TrackInteractor,
    private val studyPlanInteractor: StudyPlanInteractor,
    private val profileInteractor: ProfileInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val urlPathProcessor: UrlPathProcessor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchTrack -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildTrackScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                val trackId = profileInteractor
                    .getCurrentProfile(forceLoadFromNetwork = false)
                    .map { it.trackId }
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        onNewMessage(Message.TrackFailure)
                        return
                    } ?: return

                val trackResult = actionScope.async {
                    trackInteractor.getTrack(trackId, forceLoadFromRemote = action.forceUpdate)
                }
                val trackProgressResult = actionScope.async {
                    progressesInteractor.getTrackProgress(trackId, forceLoadFromRemote = action.forceUpdate)
                }
                val studyPlanResult = actionScope.async {
                    studyPlanInteractor.getCurrentStudyPlan(forceLoadFromRemote = action.forceUpdate)
                }

                val track = trackResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    onNewMessage(Message.TrackFailure)
                    return
                }
                val trackProgress = trackProgressResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    onNewMessage(Message.TrackFailure)
                    return
                } ?: return
                val studyPlan = studyPlanResult.await().getOrNull()

                sentryInteractor.finishTransaction(sentryTransaction)

                onNewMessage(Message.TrackSuccess(track, trackProgress, studyPlan))
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