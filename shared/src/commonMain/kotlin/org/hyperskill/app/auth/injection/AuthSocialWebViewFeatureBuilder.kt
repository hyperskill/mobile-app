package org.hyperskill.app.auth.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.auth.presentation.AuthSocialWebViewActionDispatcher
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.Action
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.Message
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.State
import org.hyperskill.app.auth.presentation.AuthSocialWebViewReducer
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.logging.presentation.wrapWithLogger
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object AuthSocialWebViewFeatureBuilder {
    private const val LOG_TAG = "AuthSocialWebViewFeature"

    fun build(
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<State, Message, Action> {
        val authReducer = AuthSocialWebViewReducer()
        val authActionDispatcher = AuthSocialWebViewActionDispatcher(ActionDispatcherOptions())

        return ReduxFeature(
            State.Idle,
            authReducer.wrapWithLogger(buildVariant, logger, LOG_TAG)
        ).wrapWithActionDispatcher(authActionDispatcher)
    }
}