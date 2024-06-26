package org.hyperskill.app.step_toolbar.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_toolbar.presentation.MainStepToolbarActionDispatcher
import org.hyperskill.app.step_toolbar.presentation.StepToolbarActionDispatcher
import org.hyperskill.app.step_toolbar.presentation.StepToolbarReducer

internal class StepToolbarComponentImpl(
    private val appGraph: AppGraph,
    private val stepRoute: StepRoute
) : StepToolbarComponent {
    override val stepToolbarReducer: StepToolbarReducer
        get() = StepToolbarReducer(stepRoute)

    override val stepToolbarActionDispatcher: StepToolbarActionDispatcher
        get() = StepToolbarActionDispatcher(
            MainStepToolbarActionDispatcher(
                config = ActionDispatcherOptions(),
                topicCompletedFlow = appGraph.stepCompletionFlowDataComponent.topicCompletedFlow,
                progressesInteractor = appGraph.buildProgressesDataComponent().progressesInteractor
            ),
            appGraph.analyticComponent.analyticInteractor
        )
}