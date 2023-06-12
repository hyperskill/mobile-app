package org.hyperskill.app.auth.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.presentation.AuthSocialActionDispatcher
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Action
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Message
import org.hyperskill.app.auth.presentation.AuthSocialFeature.State
import org.hyperskill.app.auth.presentation.AuthSocialReducer
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object AuthSocialFeatureBuilder {
    fun build(
        authInteractor: AuthInteractor,
        currentProfileStateRepository: CurrentProfileStateRepository,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor
    ): Feature<State, Message, Action> {
        val authReducer = AuthSocialReducer()
        val authActionDispatcher = AuthSocialActionDispatcher(
            ActionDispatcherOptions(),
            authInteractor,
            currentProfileStateRepository,
            analyticInteractor,
            sentryInteractor
        )

        return ReduxFeature(State.Idle, authReducer)
            .wrapWithActionDispatcher(authActionDispatcher)
    }
}