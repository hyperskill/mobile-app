package org.hyperskill.app.track.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher
import org.hyperskill.app.track.presentation.TrackFeature.Action
import org.hyperskill.app.track.presentation.TrackFeature.Message

class TrackActionDispatcher(
    config: ActionDispatcherOptions,
    private val trackInteractor: TrackInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchTrack -> {
                val trackResult = actionScope.async {
                    trackInteractor.getTrack(action.trackId)
                }
                val trackProgressResult = actionScope.async {
                    trackInteractor.getTrackProgress(action.trackId)
                }
                val studyPlanResult = actionScope.async {
                    trackInteractor.getStudyPlanByTrackId(action.trackId)
                }

                val track = trackResult.await().getOrElse {
                    onNewMessage(
                        Message.TrackError(message = it.message ?: "")
                    )
                    return
                }
                val trackProgress = trackProgressResult.await().getOrElse {
                    onNewMessage(
                        Message.TrackError(message = it.message ?: "")
                    )
                    return
                }
                val studyPlan = studyPlanResult.await().getOrNull()

                onNewMessage(
                    Message.TrackSuccess(track, trackProgress, studyPlan)
                )
            }
        }
    }
}