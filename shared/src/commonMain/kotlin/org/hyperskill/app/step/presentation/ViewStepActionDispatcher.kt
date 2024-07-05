package org.hyperskill.app.step.presentation

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.hyperskill.app.step.domain.interactor.StepInteractor
import ru.nobird.app.presentation.redux.dispatcher.ActionDispatcher

internal class ViewStepActionDispatcher(
    private val stepInteractor: StepInteractor
) : ActionDispatcher<StepFeature.Action, StepFeature.Message> {

    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var isCancelled: Boolean = false

    override fun handleAction(action: StepFeature.Action) {
        if (!isCancelled && action is StepFeature.InternalAction.ViewStep) {
            coroutineScope.launch {
                stepInteractor.viewStep(action.stepId, action.stepContext)
            }
        }
    }

    override fun setListener(listener: (message: StepFeature.Message) -> Unit) {
        // no op
    }

    override fun cancel() {
        isCancelled = true
        (coroutineScope.coroutineContext[Job] as? CompletableJob)?.complete()
    }
}