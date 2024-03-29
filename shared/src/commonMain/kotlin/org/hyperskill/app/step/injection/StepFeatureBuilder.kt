package org.hyperskill.app.step.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.presentation.StepActionDispatcher
import org.hyperskill.app.step.presentation.StepFeature.Action
import org.hyperskill.app.step.presentation.StepFeature.InternalAction
import org.hyperskill.app.step.presentation.StepFeature.Message
import org.hyperskill.app.step.presentation.StepFeature.State
import org.hyperskill.app.step.presentation.StepReducer
import org.hyperskill.app.step_completion.domain.flow.StepCompletedFlow
import org.hyperskill.app.step_completion.presentation.StepCompletionActionDispatcher
import org.hyperskill.app.step_completion.presentation.StepCompletionReducer
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object StepFeatureBuilder {
    private const val LOG_TAG = "StepFeature"

    fun build(
        stepRoute: StepRoute,
        stepInteractor: StepInteractor,
        nextLearningActivityStateRepository: NextLearningActivityStateRepository,
        currentProfileStateRepository: CurrentProfileStateRepository,
        analyticInteractor: AnalyticInteractor,
        stepCompletedFlow: StepCompletedFlow,
        sentryInteractor: SentryInteractor,
        stepCompletionReducer: StepCompletionReducer,
        stepCompletionActionDispatcher: StepCompletionActionDispatcher,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<State, Message, Action> {
        val stepReducer = StepReducer(stepRoute, stepCompletionReducer).wrapWithLogger(buildVariant, logger, LOG_TAG)
        val stepActionDispatcher = StepActionDispatcher(
            config = ActionDispatcherOptions(),
            stepCompletedFlow = stepCompletedFlow,
            stepInteractor = stepInteractor,
            nextLearningActivityStateRepository = nextLearningActivityStateRepository,
            currentProfileStateRepository = currentProfileStateRepository,
            analyticInteractor = analyticInteractor,
            sentryInteractor = sentryInteractor,
            logger.withTag(LOG_TAG)
        )

        return ReduxFeature(State.Idle, stepReducer)
            .wrapWithActionDispatcher(stepActionDispatcher)
            .wrapWithActionDispatcher(
                stepCompletionActionDispatcher.transform(
                    transformAction = { it.safeCast<InternalAction.StepCompletionAction>()?.action },
                    transformMessage = Message::StepCompletionMessage
                )
            )
    }
}