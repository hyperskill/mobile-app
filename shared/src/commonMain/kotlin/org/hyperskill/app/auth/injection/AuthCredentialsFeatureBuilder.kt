package org.hyperskill.app.auth.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.presentation.AuthCredentialsActionDispatcher
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.Action
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.Message
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.State
import org.hyperskill.app.auth.presentation.AuthCredentialsReducer
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object AuthCredentialsFeatureBuilder {
    private const val LOG_TAG = "AuthCredentialsFeature"

    fun build(
        authInteractor: AuthInteractor,
        currentProfileStateRepository: CurrentProfileStateRepository,
        urlPathProcessor: UrlPathProcessor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<State, Message, Action> {
        val authReducer = AuthCredentialsReducer()
        val authActionDispatcher = AuthCredentialsActionDispatcher(
            ActionDispatcherOptions(),
            authInteractor,
            currentProfileStateRepository,
            urlPathProcessor,
            analyticInteractor,
            sentryInteractor
        )

        return ReduxFeature(
            State("", "", AuthCredentialsFeature.FormState.Editing),
            authReducer.wrapWithLogger(buildVariant, logger, LOG_TAG)
        ).wrapWithActionDispatcher(authActionDispatcher)
    }
}