package org.hyperskill.app.study_plan.screen.view

import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewStateMapper

internal object StudyPlanScreenViewStateMapper {
    fun map(state: StudyPlanScreenFeature.State): StudyPlanScreenViewState =
        StudyPlanScreenViewState(
            toolbarState = state.toolbarState,
            studyPlanWidgetViewState = StudyPlanWidgetViewStateMapper.map(state.studyPlanWidgetState)
        )
}