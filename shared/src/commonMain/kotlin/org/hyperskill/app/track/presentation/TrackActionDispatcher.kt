package org.hyperskill.app.track.presentation

import kotlinx.coroutines.async
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.presentation.TrackFeature.Action
import org.hyperskill.app.track.presentation.TrackFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class TrackActionDispatcher(
    config: ActionDispatcherOptions,
    private val trackInteractor: TrackInteractor,
    private val profileInteractor: ProfileInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val urlPathProcessor: UrlPathProcessor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchTrack -> {
                val trackId = profileInteractor
                    .getCurrentProfile(sourceType = DataSourceType.CACHE)
                    .map { profile -> profile.trackId }
                    .getOrElse {
                        Message.TrackFailure
                        return
                    }

                if (trackId == null) {
                    Message.TrackFailure
                    return
                }

                val trackResult = actionScope.async {
                    trackInteractor.getTrack(trackId)
                }
                val trackProgressResult = actionScope.async {
                    trackInteractor.getTrackProgress(trackId)
                }
                val studyPlanResult = actionScope.async {
                    trackInteractor.getStudyPlanByTrackId(trackId)
                }

                val track = trackResult.await().getOrElse {
                    onNewMessage(Message.TrackFailure)
                    return
                }
                val trackProgress = trackProgressResult.await().getOrElse {
                    onNewMessage(Message.TrackFailure)
                    return
                }
                val studyPlan = studyPlanResult.await().getOrNull()

                onNewMessage(Message.TrackSuccess(track, trackProgress, studyPlan))
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            is Action.GetLink ->
                getLink(action.path, ::onNewMessage)

        }
    }

    private suspend fun getLink(path: HyperskillUrlPath, onNewMessage: (Message) -> Unit): Unit =
        urlPathProcessor.processUrlPath(path)
            .fold(
                onSuccess = { url ->
                    onNewMessage(Message.LinkReceived(url))
                },
                onFailure = {
                    onNewMessage(Message.LinkReceiveFailed)
                }
            )
}