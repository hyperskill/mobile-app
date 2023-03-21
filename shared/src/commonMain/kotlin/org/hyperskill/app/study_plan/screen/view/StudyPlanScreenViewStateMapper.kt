package org.hyperskill.app.study_plan.screen.view

import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewStateMapper

internal class StudyPlanScreenViewStateMapper(
    private val studyPlanWidgetViewStateMapper: StudyPlanWidgetViewStateMapper
) {
    fun map(state: StudyPlanScreenFeature.State): StudyPlanScreenViewState =
        StudyPlanScreenViewState(
            toolbarState = state.toolbarState,
            studyPlanWidgetViewState = studyPlanWidgetViewStateMapper.map(state.studyPlanWidgetState)
        )
}