package org.hyperskill.app.auth.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.presentation.AuthSocialActionDispatcher
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Action
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Message
import org.hyperskill.app.auth.presentation.AuthSocialFeature.State
import org.hyperskill.app.auth.presentation.AuthSocialReducer
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object AuthSocialFeatureBuilder {
    private const val LOG_TAG = "AuthSocialFeature"

    fun build(
        authInteractor: AuthInteractor,
        currentProfileStateRepository: CurrentProfileStateRepository,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<State, Message, Action> {
        val authReducer = AuthSocialReducer()
        val authActionDispatcher = AuthSocialActionDispatcher(
            ActionDispatcherOptions(),
            authInteractor,
            currentProfileStateRepository,
            analyticInteractor,
            sentryInteractor
        )

        return ReduxFeature(
            State.Idle,
            authReducer.wrapWithLogger(buildVariant, logger, LOG_TAG)
        ).wrapWithActionDispatcher(authActionDispatcher)
    }
}