package org.hyperskill.app.study_plan.widget.injection

import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface StudyPlanWidgetComponent {
    val studyPlanWidgetFeature: Feature<StudyPlanWidgetViewState, StudyPlanWidgetFeature.Message, StudyPlanWidgetFeature.Action>
}