package org.hyperskill.app.main.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.main.presentation.AppActionDispatcher
import org.hyperskill.app.main.presentation.AppFeature.Action
import org.hyperskill.app.main.presentation.AppFeature.Message
import org.hyperskill.app.main.presentation.AppFeature.State
import org.hyperskill.app.main.presentation.AppReducer
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object AppFeatureBuilder {
    fun build(
        authInteractor: AuthInteractor,
        profileInteractor: ProfileInteractor,
        analyticInteractor: AnalyticInteractor
    ): Feature<State, Message, Action> {
        val appReducer = AppReducer()
        val appActionDispatcher = AppActionDispatcher(
            ActionDispatcherOptions(),
            authInteractor,
            profileInteractor,
            analyticInteractor
        )

        return ReduxFeature(State.Idle, appReducer)
            .wrapWithActionDispatcher(appActionDispatcher)
    }
}