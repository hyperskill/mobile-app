package org.hyperskill.app.welcome_onboarding.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingActionDispatcher
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Message
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.State
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object WelcomeOnboardingFeatureBuilder {
    private const val LOG_TAG = "WelcomeOnboardingFeature"

    fun build(
        analyticInteractor: AnalyticInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<State, Message, Action> {
        val welcomeOnboardingReducer =
            WelcomeOnboardingReducer()
                .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val welcomeOnboardingActionDispatcher = WelcomeOnboardingActionDispatcher(
            ActionDispatcherOptions(),
            analyticInteractor
        )

        return ReduxFeature(
            initialState = WelcomeOnboardingFeature.initialState(),
            reducer = welcomeOnboardingReducer
        ).wrapWithActionDispatcher(welcomeOnboardingActionDispatcher)
    }
}