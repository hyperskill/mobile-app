package org.hyperskill.app.step.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
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
        profileInteractor: ProfileInteractor,
        analyticInteractor: AnalyticInteractor
    ): Feature<State, Message, Action> {
        val stepReducer = StepReducer()
        val stepActionDispatcher = StepActionDispatcher(
            ActionDispatcherOptions(),
            stepInteractor,
            profileInteractor,
            analyticInteractor
        )

        return ReduxFeature(State.Idle, stepReducer)
            .wrapWithActionDispatcher(stepActionDispatcher)
    }
}