package org.hyperskill.app.interview_preparation.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetActionDispatcher
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetReducer
import org.hyperskill.app.interview_preparation.view.mapper.InterviewPreparationWidgetViewStateMapper

internal class InterviewPreparationWidgetComponentImpl(
    private val appGraph: AppGraph
) : InterviewPreparationWidgetComponent {
    override val interviewPreparationWidgetReducer: InterviewPreparationWidgetReducer
        get() = InterviewPreparationWidgetReducer()

    override val interviewPreparationWidgetActionDispatcher: InterviewPreparationWidgetActionDispatcher
        get() = InterviewPreparationWidgetActionDispatcher(
            config = ActionDispatcherOptions(),
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            interviewStepsStateRepository = appGraph.stateRepositoriesComponent.interviewStepsStateRepository,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            resourceProvider = appGraph.commonComponent.resourceProvider
        )

    override val interviewPreparationWidgetViewStateMapper: InterviewPreparationWidgetViewStateMapper
        get() = InterviewPreparationWidgetViewStateMapper(appGraph.commonComponent.resourceProvider)
}