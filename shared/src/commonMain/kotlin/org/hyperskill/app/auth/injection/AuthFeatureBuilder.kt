package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.presentation.AuthActionDispatcher
import org.hyperskill.app.auth.presentation.AuthFeature.Action
import org.hyperskill.app.auth.presentation.AuthFeature.Message
import org.hyperskill.app.auth.presentation.AuthFeature.State
import org.hyperskill.app.auth.presentation.AuthReducer
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object AuthFeatureBuilder {
    fun build(authInteractor: AuthInteractor): Feature<State, Message, Action> {
        val authReducer = AuthReducer()
        val authActionDispatcher = AuthActionDispatcher(ActionDispatcherOptions(), authInteractor)

        return ReduxFeature(State.Idle, authReducer)
            .wrapWithActionDispatcher(authActionDispatcher)
    }
}