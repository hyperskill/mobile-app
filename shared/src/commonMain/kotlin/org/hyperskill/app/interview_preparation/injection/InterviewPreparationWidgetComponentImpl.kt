package org.hyperskill.app.interview_preparation.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetActionDispatcher
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetReducer
import org.hyperskill.app.interview_preparation.view.mapper.InterviewPreparationWidgetViewStateMapper

class InterviewPreparationWidgetComponentImpl(
    private val appGraph: AppGraph
) : InterviewPreparationWidgetComponent {
    override val interviewPreparationWidgetReducer: InterviewPreparationWidgetReducer
        get() = InterviewPreparationWidgetReducer()

    override val interviewPreparationWidgetActionDispatcher: InterviewPreparationWidgetActionDispatcher
        get() = InterviewPreparationWidgetActionDispatcher(
            config = ActionDispatcherOptions(),
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            interviewStepsStateRepository = appGraph.stateRepositoriesComponent.interviewStepsStateRepository,
            submissionRepository = appGraph.submissionDataComponent.submissionRepository,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            onboardingInteractor = appGraph.buildOnboardingDataComponent().onboardingInteractor
        )

    override val interviewPreparationWidgetViewStateMapper: InterviewPreparationWidgetViewStateMapper
        get() = InterviewPreparationWidgetViewStateMapper(
            appGraph.commonComponent.resourceProvider
        )
}