package org.hyperskill.app.study_plan.widget.injection

import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetActionDispatcher
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer

interface StudyPlanWidgetComponent {
    val studyPlanWidgetReducer: StudyPlanWidgetReducer
    val studyPlanWidgetDispatcher: StudyPlanWidgetActionDispatcher
}