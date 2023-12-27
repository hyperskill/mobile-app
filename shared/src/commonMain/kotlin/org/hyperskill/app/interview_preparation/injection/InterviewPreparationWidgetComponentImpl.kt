package org.hyperskill.app.interview_preparation.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetActionDispatcher
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetReducer

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
            sentryInteractor = appGraph.sentryComponent.sentryInteractor
        )
}