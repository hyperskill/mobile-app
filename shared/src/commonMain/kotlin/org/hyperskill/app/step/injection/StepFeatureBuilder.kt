package org.hyperskill.app.step.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.presentation.StepActionDispatcher
import org.hyperskill.app.step.presentation.StepFeature.Action
import org.hyperskill.app.step.presentation.StepFeature.Message
import org.hyperskill.app.step.presentation.StepFeature.State
import org.hyperskill.app.step.presentation.StepReducer
import org.hyperskill.app.step_completion.presentation.StepCompletionActionDispatcher
import org.hyperskill.app.step_completion.presentation.StepCompletionReducer
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object StepFeatureBuilder {
    fun build(
        stepRoute: StepRoute,
        stepInteractor: StepInteractor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        stepCompletionReducer: StepCompletionReducer,
        stepCompletionActionDispatcher: StepCompletionActionDispatcher
    ): Feature<State, Message, Action> {
        val stepReducer = StepReducer(stepRoute, stepCompletionReducer)
        val stepActionDispatcher = StepActionDispatcher(
            ActionDispatcherOptions(),
            stepInteractor,
            analyticInteractor,
            sentryInteractor
        )

        return ReduxFeature(State.Idle, stepReducer)
            .wrapWithActionDispatcher(stepActionDispatcher)
            .wrapWithActionDispatcher(
                stepCompletionActionDispatcher.transform(
                    transformAction = { it.safeCast<Action.StepCompletionAction>()?.action },
                    transformMessage = Message::StepCompletionMessage
                )
            )
    }
}