package org.hyperskill.app.placeholder_new_user.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserActionDispatcher
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.State
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.Message
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.Action
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserReducer
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object PlaceholderNewUserFeatureBuilder {
    fun build(
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        trackInteractor: TrackInteractor,
        progressesInteractor: ProgressesInteractor,
        profileInteractor: ProfileInteractor
    ): Feature<State, Message, Action> {
        val placeholderNewUserReducer = PlaceholderNewUserReducer()
        val placeholderNewUserActionDispatcher = PlaceholderNewUserActionDispatcher(
            ActionDispatcherOptions(),
            analyticInteractor,
            sentryInteractor,
            trackInteractor,
            progressesInteractor,
            profileInteractor
        )

        return ReduxFeature(State.Idle, placeholderNewUserReducer)
            .wrapWithActionDispatcher(placeholderNewUserActionDispatcher)
    }
}