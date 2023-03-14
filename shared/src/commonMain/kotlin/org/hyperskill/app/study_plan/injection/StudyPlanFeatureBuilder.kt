package org.hyperskill.app.study_plan.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.study_plan.presentation.StudyPlanActionDispatcher
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature
import org.hyperskill.app.study_plan.presentation.StudyPlanReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object StudyPlanFeatureBuilder {
    fun build(): Feature<StudyPlanFeature.State, StudyPlanFeature.Message, StudyPlanFeature.Action> {
        val studyPlanReducer = StudyPlanReducer()
        val studyPlanDispatcher = StudyPlanActionDispatcher(ActionDispatcherOptions())
        return ReduxFeature(StudyPlanFeature.State(), studyPlanReducer)
            .wrapWithActionDispatcher(studyPlanDispatcher)
    }
}