package org.hyperskill.app.study_plan.widget.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetActionDispatcher
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import org.hyperskill.app.study_plan.widget.view.mapper.StudyPlanWidgetViewStateMapper

class StudyPlanWidgetComponentImpl(private val appGraph: AppGraph) : StudyPlanWidgetComponent {
    override val studyPlanWidgetDispatcher: StudyPlanWidgetActionDispatcher
        get() = StudyPlanWidgetActionDispatcher(
            config = ActionDispatcherOptions(),
            studyPlanInteractor = appGraph.buildStudyPlanDataComponent().studyPlanInteractor,
            trackInteractor = appGraph.buildTrackDataComponent().trackInteractor,
            nextLearningActivityStateRepository = appGraph
                .stateRepositoriesComponent.nextLearningActivityStateRepository,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor
        )

    override val studyPlanWidgetReducer: StudyPlanWidgetReducer
        get() = StudyPlanWidgetReducer()

    override val studyPlanWidgetViewStateMapper: StudyPlanWidgetViewStateMapper
        get() = StudyPlanWidgetViewStateMapper(appGraph.commonComponent.dateFormatter)
}