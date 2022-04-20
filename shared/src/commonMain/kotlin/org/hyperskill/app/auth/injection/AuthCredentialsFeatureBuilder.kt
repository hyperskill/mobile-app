package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.presentation.AuthCredentialsActionDispatcher
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.Action
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.Message
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.State
import org.hyperskill.app.auth.presentation.AuthCredentialsReducer
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object AuthCredentialsFeatureBuilder {
    fun build(authInteractor: AuthInteractor): Feature<State, Message, Action> {
        val authReducer = AuthCredentialsReducer()
        val authActionDispatcher = AuthCredentialsActionDispatcher(ActionDispatcherOptions(), authInteractor)

        return ReduxFeature(State.Idle, authReducer)
            .wrapWithActionDispatcher(authActionDispatcher)
    }
}