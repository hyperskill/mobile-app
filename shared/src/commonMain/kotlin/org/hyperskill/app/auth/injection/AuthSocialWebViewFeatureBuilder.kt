package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.presentation.AuthSocialWebViewActionDispatcher
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.Action
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.Message
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.State
import org.hyperskill.app.auth.presentation.AuthSocialWebViewReducer
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object AuthSocialWebViewFeatureBuilder {
    fun build(): Feature<State, Message, Action> {
        val authReducer = AuthSocialWebViewReducer()
        val authActionDispatcher = AuthSocialWebViewActionDispatcher(ActionDispatcherOptions())

        return ReduxFeature(State.Idle, authReducer)
            .wrapWithActionDispatcher(authActionDispatcher)
    }
}