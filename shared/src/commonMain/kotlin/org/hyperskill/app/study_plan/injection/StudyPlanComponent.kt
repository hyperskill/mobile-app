package org.hyperskill.app.study_plan.injection

import org.hyperskill.app.study_plan.presentation.StudyPlanFeature
import org.hyperskill.app.study_plan.view.StudyPlanViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface StudyPlanComponent {
    val studyPlanFeature: Feature<StudyPlanViewState, StudyPlanFeature.Message, StudyPlanFeature.Action>
}