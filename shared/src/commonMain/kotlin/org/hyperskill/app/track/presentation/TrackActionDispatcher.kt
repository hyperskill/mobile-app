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
                val result = trackInteractor.getTrack(action.trackId)

                val message =
                    result
                        .map { Message.TrackSuccess(it) }
                        .getOrElse {
                            Message.TrackError(message = it.message ?: "")
                        }

                onNewMessage(message)
            }
        }
    }
}