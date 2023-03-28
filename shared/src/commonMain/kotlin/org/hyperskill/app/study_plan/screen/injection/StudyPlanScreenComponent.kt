package org.hyperskill.app.study_plan.screen.injection

import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.screen.view.StudyPlanScreenViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface StudyPlanScreenComponent {
    val studyPlanScreenFeature: Feature<StudyPlanScreenViewState, StudyPlanScreenFeature.Message, StudyPlanScreenFeature.Action>
}