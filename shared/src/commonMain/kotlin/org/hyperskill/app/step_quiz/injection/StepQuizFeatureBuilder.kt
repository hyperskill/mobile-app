package org.hyperskill.app.step_quiz.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitActionDispatcher
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.domain.validation.StepQuizReplyValidator
import org.hyperskill.app.step_quiz.presentation.StepQuizActionDispatcher
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizReducer
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object StepQuizFeatureBuilder {
    private const val LOG_TAG = "StepQuizFeature"

    fun build(
        stepRoute: StepRoute,
        problemsLimitReducer: ProblemsLimitReducer,
        problemsLimitActionDispatcher: ProblemsLimitActionDispatcher,
        stepQuizInteractor: StepQuizInteractor,
        stepQuizReplyValidator: StepQuizReplyValidator,
        currentProfileStateRepository: CurrentProfileStateRepository,
        freemiumInteractor: FreemiumInteractor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        onboardingInteractor: OnboardingInteractor,
        resourceProvider: ResourceProvider,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<StepQuizFeature.State, StepQuizFeature.Message, StepQuizFeature.Action> {
        val stepQuizReducer =
            StepQuizReducer(stepRoute, problemsLimitReducer)
                .wrapWithLogger(buildVariant, logger, LOG_TAG)
        val stepQuizActionDispatcher = StepQuizActionDispatcher(
            ActionDispatcherOptions(),
            stepQuizInteractor,
            stepQuizReplyValidator,
            currentProfileStateRepository,
            freemiumInteractor,
            analyticInteractor,
            sentryInteractor,
            onboardingInteractor,
            resourceProvider
        )

        return ReduxFeature(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Idle,
                problemsLimitState = ProblemsLimitFeature.State.Idle
            ),
            stepQuizReducer
        )
            .wrapWithActionDispatcher(stepQuizActionDispatcher)
            .wrapWithActionDispatcher(
                problemsLimitActionDispatcher.transform(
                    transformAction = { it.safeCast<StepQuizFeature.Action.ProblemsLimitAction>()?.action },
                    transformMessage = StepQuizFeature.Message::ProblemsLimitMessage
                )
            )
    }
}