package org.hyperskill.app.step.injection

import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.presentation.StepActionDispatcher
import org.hyperskill.app.step.presentation.StepFeature.Action
import org.hyperskill.app.step.presentation.StepFeature.Message
import org.hyperskill.app.step.presentation.StepFeature.State
import org.hyperskill.app.step.presentation.StepReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object StepFeatureBuilder {
    fun build(
        stepInteractor: StepInteractor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        failedToLoadNextStepQuizMutableSharedFlow: MutableSharedFlow<Unit>
    ): Feature<State, Message, Action> {
        val stepReducer = StepReducer()
        val stepActionDispatcher = StepActionDispatcher(
            ActionDispatcherOptions(),
            stepInteractor,
            analyticInteractor,
            sentryInteractor,
            failedToLoadNextStepQuizMutableSharedFlow
        )

        return ReduxFeature(State.Idle, stepReducer)
            .wrapWithActionDispatcher(stepActionDispatcher)
    }
}