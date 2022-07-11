package org.hyperskill.app.track.presentation

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
                val trackResult = trackInteractor.getTrack(action.trackId)
                val trackProgressResult = trackInteractor.getTrackProgress(action.trackId)
                val studyPlanResult = trackInteractor.getStudyPlanByTrackId(action.trackId)

                val track = trackResult.getOrElse {
                    onNewMessage(
                        Message.TrackError(message = it.message ?: "")
                    )
                    return
                }
                val trackProgress = trackProgressResult.getOrElse {
                    onNewMessage(
                        Message.TrackError(message = it.message ?: "")
                    )
                    return
                }
                val studyPlan = studyPlanResult.getOrNull()

                onNewMessage(
                    Message.TrackSuccess(track, trackProgress, studyPlan)
                )
            }
        }
    }
}