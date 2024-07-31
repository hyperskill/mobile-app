package org.hyperskill.app.study_plan.widget.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.study_plan.widget.presentation.MainStudyPlanWidgetActionDispatcher
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetActionDispatcher
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import org.hyperskill.app.study_plan.widget.view.mapper.StudyPlanWidgetViewStateMapper

internal class StudyPlanWidgetComponentImpl(private val appGraph: AppGraph) : StudyPlanWidgetComponent {
    private val mainStudyPlanWidgetActionDispatcher: MainStudyPlanWidgetActionDispatcher
        get() = MainStudyPlanWidgetActionDispatcher(
            config = ActionDispatcherOptions(),
            learningActivitiesRepository = appGraph.buildLearningActivitiesDataComponent().learningActivitiesRepository,
            nextLearningActivityStateRepository = appGraph
                .stateRepositoriesComponent.nextLearningActivityStateRepository,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            currentStudyPlanStateRepository = appGraph.stateRepositoriesComponent.currentStudyPlanStateRepository,
            subscriptionInteractor = appGraph.subscriptionDataComponent.subscriptionsInteractor,
            progressesRepository = appGraph.buildProgressesDataComponent().progressesRepository,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor
        )

    override val studyPlanWidgetDispatcher: StudyPlanWidgetActionDispatcher
        get() = StudyPlanWidgetActionDispatcher(
            mainStudyPlanWidgetActionDispatcher,
            appGraph.analyticComponent.analyticInteractor
        )

    override val studyPlanWidgetReducer: StudyPlanWidgetReducer
        get() = StudyPlanWidgetReducer()

    override val studyPlanWidgetViewStateMapper: StudyPlanWidgetViewStateMapper
        get() = StudyPlanWidgetViewStateMapper(appGraph.commonComponent.dateFormatter)
}