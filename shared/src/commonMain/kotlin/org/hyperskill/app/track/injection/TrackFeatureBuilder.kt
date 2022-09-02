package org.hyperskill.app.track.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.presentation.TrackActionDispatcher
import org.hyperskill.app.track.presentation.TrackFeature.Action
import org.hyperskill.app.track.presentation.TrackFeature.Message
import org.hyperskill.app.track.presentation.TrackFeature.State
import org.hyperskill.app.track.presentation.TrackReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object TrackFeatureBuilder {
    fun build(
        trackInteractor: TrackInteractor,
        profileInteractor: ProfileInteractor,
        analyticInteractor: AnalyticInteractor
    ): Feature<State, Message, Action> {
        val trackReducer = TrackReducer()
        val trackActionDispatcher = TrackActionDispatcher(
            ActionDispatcherOptions(),
            trackInteractor,
            profileInteractor,
            analyticInteractor
        )

        return ReduxFeature(State.Idle, trackReducer)
            .wrapWithActionDispatcher(trackActionDispatcher)
    }
}