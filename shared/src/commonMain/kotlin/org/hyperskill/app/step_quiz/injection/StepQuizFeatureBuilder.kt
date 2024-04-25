package org.hyperskill.app.step_quiz.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.domain.validation.StepQuizReplyValidator
import org.hyperskill.app.step_quiz.presentation.StepQuizActionDispatcher
import org.hyperskill.app.step_quiz.presentation.StepQuizChildFeatureReducer
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizReducer
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsActionDispatcher
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsReducer
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarActionDispatcher
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarReducer
import org.hyperskill.app.subscriptions.domain.interactor.SubscriptionsInteractor
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object StepQuizFeatureBuilder {
    private const val LOG_TAG = "StepQuizFeature"

    fun build(
        stepRoute: StepRoute,
        stepQuizInteractor: StepQuizInteractor,
        stepQuizReplyValidator: StepQuizReplyValidator,
        subscriptionsInteractor: SubscriptionsInteractor,
        currentProfileStateRepository: CurrentProfileStateRepository,
        urlPathProcessor: UrlPathProcessor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        onboardingInteractor: OnboardingInteractor,
        currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
        stepQuizHintsReducer: StepQuizHintsReducer,
        stepQuizHintsActionDispatcher: StepQuizHintsActionDispatcher,
        stepQuizToolbarReducer: StepQuizToolbarReducer,
        stepQuizToolbarActionDispatcher: StepQuizToolbarActionDispatcher,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<StepQuizFeature.State, StepQuizFeature.Message, StepQuizFeature.Action> {
        val stepQuizReducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer(
                stepQuizHintsReducer = stepQuizHintsReducer,
                stepQuizToolbarReducer = stepQuizToolbarReducer
            ),
        ).wrapWithLogger(buildVariant, logger, LOG_TAG)

        val stepQuizActionDispatcher = StepQuizActionDispatcher(
            config = ActionDispatcherOptions(),
            stepQuizInteractor = stepQuizInteractor,
            stepQuizReplyValidator = stepQuizReplyValidator,
            subscriptionsInteractor = subscriptionsInteractor,
            currentProfileStateRepository = currentProfileStateRepository,
            currentSubscriptionStateRepository = currentSubscriptionStateRepository,
            urlPathProcessor = urlPathProcessor,
            analyticInteractor = analyticInteractor,
            sentryInteractor = sentryInteractor,
            onboardingInteractor = onboardingInteractor,
            logger = logger.withTag(LOG_TAG)
        )

        return ReduxFeature(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Idle,
                stepQuizHintsState = StepQuizHintsFeature.State.Idle,
                stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute)
            ),
            stepQuizReducer
        )
            .wrapWithActionDispatcher(stepQuizActionDispatcher)
            .wrapWithActionDispatcher(
                stepQuizHintsActionDispatcher.transform(
                    transformAction = { it.safeCast<StepQuizFeature.Action.StepQuizHintsAction>()?.action },
                    transformMessage = StepQuizFeature.Message::StepQuizHintsMessage
                )
            )
            .wrapWithActionDispatcher(
                stepQuizToolbarActionDispatcher.transform(
                    transformAction = { it.safeCast<StepQuizFeature.Action.StepQuizToolbarAction>()?.action },
                    transformMessage = StepQuizFeature.Message::StepQuizToolbarMessage
                )
            )
    }
}