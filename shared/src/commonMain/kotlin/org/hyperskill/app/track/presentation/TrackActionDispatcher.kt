package org.hyperskill.app.track.presentation

import kotlinx.coroutines.async
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.presentation.TrackFeature.Action
import org.hyperskill.app.track.presentation.TrackFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class TrackActionDispatcher(
    config: ActionDispatcherOptions,
    private val trackInteractor: TrackInteractor,
    private val profileInteractor: ProfileInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchTrack -> {
                val trackId = profileInteractor
                    .getCurrentProfile(sourceType = DataSourceType.CACHE)
                    .map { it.trackId }
                    .getOrNull()
                    ?: return onNewMessage(Message.TrackFailure)

                val trackResult = actionScope.async {
                    trackInteractor.getTrack(trackId)
                }
                val trackProgressResult = actionScope.async {
                    progressesInteractor.getTrackProgress(trackId)
                }
                val studyPlanResult = actionScope.async {
                    trackInteractor.getStudyPlanByTrackId(trackId)
                }

                val track = trackResult.await().getOrElse {
                    return onNewMessage(Message.TrackFailure)
                }
                val trackProgress = trackProgressResult.await().getOrNull()
                    ?: return onNewMessage(Message.TrackFailure)

                val studyPlan = studyPlanResult.await().getOrNull()

                onNewMessage(Message.TrackSuccess(track, trackProgress, studyPlan))
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
        }
    }
}