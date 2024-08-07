package org.hyperskill.app.step.presentation

import org.hyperskill.app.core.presentation.CompletableCoroutineActionDispatcher
import org.hyperskill.app.core.presentation.CompletableCoroutineActionDispatcherConfig
import org.hyperskill.app.step.domain.interactor.StepInteractor

internal class ViewStepActionDispatcher(
    config: CompletableCoroutineActionDispatcherConfig,
    private val stepInteractor: StepInteractor
) : CompletableCoroutineActionDispatcher<StepFeature.Action, StepFeature.Message>(config.createScope()) {

    override suspend fun handleNonCancellableAction(action: StepFeature.Action) {
        if (action is StepFeature.InternalAction.ViewStep) {
            stepInteractor.viewStep(action.stepId, action.stepContext)
        }
    }
}