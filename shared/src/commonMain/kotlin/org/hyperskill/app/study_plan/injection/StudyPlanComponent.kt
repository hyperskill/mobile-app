package org.hyperskill.app.study_plan.injection

import org.hyperskill.app.study_plan.presentation.StudyPlanFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface StudyPlanComponent {
    val studyPlanFeature: Feature<StudyPlanFeature.State, StudyPlanFeature.Message, StudyPlanFeature.Action>
}