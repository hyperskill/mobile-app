package org.hyperskill.app.study_plan.screen.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.screen.view.StudyPlanScreenViewState
import org.hyperskill.app.study_plan.widget.injection.StudyPlanWidgetComponent
import ru.nobird.app.presentation.redux.feature.Feature

class StudyPlanScreenComponentImpl(private val appGraph: AppGraph) : StudyPlanScreenComponent {

    private val toolbarComponent: GamificationToolbarComponent =
        appGraph.buildGamificationToolbarComponent(GamificationToolbarScreen.STUDY_PLAN)

    private val studyPlanWidgetComponent: StudyPlanWidgetComponent =
        appGraph.buildStudyPlanWidgetComponent()

    override val studyPlanScreenFeature: Feature<
        StudyPlanScreenViewState, StudyPlanScreenFeature.Message, StudyPlanScreenFeature.Action>
        get() = StudyPlanScreenFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            toolbarReducer = toolbarComponent.gamificationToolbarReducer,
            toolbarActionDispatcher = toolbarComponent.gamificationToolbarActionDispatcher,
            studyPlanWidgetReducer = studyPlanWidgetComponent.studyPlanWidgetReducer,
            studyPlanWidgetDispatcher = studyPlanWidgetComponent.studyPlanWidgetDispatcher,
            studyPlanWidgetViewStateMapper = studyPlanWidgetComponent.studyPlanWidgetViewStateMapper,
            resourceProvider = appGraph.commonComponent.resourceProvider
        )
}