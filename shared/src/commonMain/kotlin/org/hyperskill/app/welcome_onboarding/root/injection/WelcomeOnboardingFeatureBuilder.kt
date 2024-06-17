package org.hyperskill.app.welcome_onboarding.root.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.learning_activities.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingFeatureParams
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingActionDispatcher
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.Message
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.State
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object WelcomeOnboardingFeatureBuilder {
    private const val LOG_TAG = "WelcomeOnboardingFeature"

    fun build(
        analyticInteractor: AnalyticInteractor,
        logger: Logger,
        buildVariant: BuildVariant,
        sentryInteractor: SentryInteractor,
        learningActivityRepository: LearningActivitiesRepository,
        params: WelcomeOnboardingFeatureParams
    ): Feature<State, Message, Action> {
        val welcomeOnboardingReducer =
            WelcomeOnboardingReducer()
                .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val welcomeOnboardingActionDispatcher = WelcomeOnboardingActionDispatcher(
            ActionDispatcherOptions(),
            analyticInteractor,
            sentryInteractor = sentryInteractor,
            learningActivityRepository = learningActivityRepository,
            logger = logger.withTag(LOG_TAG)
        )

        return ReduxFeature(
            initialState = WelcomeOnboardingFeature.initialState(params),
            reducer = welcomeOnboardingReducer
        ).wrapWithActionDispatcher(welcomeOnboardingActionDispatcher)
    }
}