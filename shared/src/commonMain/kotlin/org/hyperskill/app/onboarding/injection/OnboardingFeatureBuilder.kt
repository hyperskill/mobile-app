package org.hyperskill.app.onboarding.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.onboarding.presentation.OnboardingActionDispatcher
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.Action
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.Message
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.State
import org.hyperskill.app.onboarding.presentation.OnboardingReducer
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object OnboardingFeatureBuilder {
    private const val LOG_TAG = "OnboardingFeature"
    fun build(
        onboardingInteractor: OnboardingInteractor,
        currentProfileStateRepository: CurrentProfileStateRepository,
        analyticInteractor: AnalyticInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<State, Message, Action> {
        val onboardingReducer = OnboardingReducer().wrapWithLogger(buildVariant, logger, LOG_TAG)

        val onboardingActionDispatcher = OnboardingActionDispatcher(
            ActionDispatcherOptions(),
            onboardingInteractor,
            currentProfileStateRepository,
            analyticInteractor
        )

        return ReduxFeature(State.Idle, onboardingReducer)
            .wrapWithActionDispatcher(onboardingActionDispatcher)
    }
}