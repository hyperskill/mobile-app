package org.hyperskill.app.study_plan.screen.injection

import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface StudyPlanScreenComponent {
    val studyPlanScreenFeature: Feature<
        StudyPlanScreenFeature.ViewState, StudyPlanScreenFeature.Message, StudyPlanScreenFeature.Action>
}