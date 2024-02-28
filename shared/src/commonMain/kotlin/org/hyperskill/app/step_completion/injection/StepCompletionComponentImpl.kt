package org.hyperskill.app.step_completion.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.presentation.StepCompletionActionDispatcher
import org.hyperskill.app.step_completion.presentation.StepCompletionReducer

internal class StepCompletionComponentImpl(
    private val appGraph: AppGraph,
    private val stepRoute: StepRoute
) : StepCompletionComponent {
    override val stepCompletionReducer: StepCompletionReducer
        get() = StepCompletionReducer(stepRoute)

    override val stepCompletionActionDispatcher: StepCompletionActionDispatcher
        get() = StepCompletionActionDispatcher(
            ActionDispatcherOptions(),
            submissionRepository = appGraph.submissionDataComponent.submissionRepository,
            stepInteractor = appGraph.buildStepDataComponent().stepInteractor,
            progressesInteractor = appGraph.buildProgressesDataComponent().progressesInteractor,
            topicsRepository = appGraph.buildTopicsDataComponent().topicsRepository,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            resourceProvider = appGraph.commonComponent.resourceProvider,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            subscriptionsInteractor = appGraph.subscriptionDataComponent.subscriptionsInteractor,
            shareStreakInteractor = appGraph.buildShareStreakDataComponent().shareStreakInteractor,
            requestReviewInteractor = appGraph.buildRequestReviewDataComponent().requestReviewInteractor,
            nextLearningActivityStateRepository = appGraph.stateRepositoriesComponent
                .nextLearningActivityStateRepository,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            currentGamificationToolbarDataStateRepository = appGraph.stateRepositoriesComponent
                .currentGamificationToolbarDataStateRepository,
            dailyStepCompletedFlow = appGraph.stepCompletionFlowDataComponent.dailyStepCompletedFlow,
            topicCompletedFlow = appGraph.stepCompletionFlowDataComponent.topicCompletedFlow,
            topicProgressFlow = appGraph.progressesFlowDataComponent.topicProgressFlow,
            interviewStepsStateRepository = appGraph.stateRepositoriesComponent.interviewStepsStateRepository
        )
}