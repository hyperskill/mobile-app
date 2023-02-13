package org.hyperskill.app.step_completion.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.presentation.StepCompletionActionDispatcher
import org.hyperskill.app.step_completion.presentation.StepCompletionReducer

class StepCompletionComponentImpl(
    private val appGraph: AppGraph,
    private val stepRoute: StepRoute
) : StepCompletionComponent {
    override val stepCompletionReducer: StepCompletionReducer
        get() = StepCompletionReducer(stepRoute)

    override val stepCompletionActionDispatcher: StepCompletionActionDispatcher
        get() = StepCompletionActionDispatcher(
            ActionDispatcherOptions(),
            appGraph.buildStepDataComponent().stepInteractor,
            appGraph.buildProgressesDataComponent().progressesInteractor,
            appGraph.buildTopicsDataComponent().topicsInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.commonComponent.resourceProvider,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.buildNotificationComponent().notificationInteractor,
            appGraph.stepCompletionFlowDataComponent.topicCompletedFlow
        )
}