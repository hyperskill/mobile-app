package org.hyperskill.app.study_plan.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor
import org.hyperskill.app.study_plan.presentation.StudyPlanActionDispatcher
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature
import org.hyperskill.app.study_plan.presentation.StudyPlanReducer
import org.hyperskill.app.study_plan.view.StudyPlanViewState
import org.hyperskill.app.study_plan.view.StudyPlanViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object StudyPlanFeatureBuilder {
    fun build(
        studyPlanInteractor: StudyPlanInteractor
    ): Feature<StudyPlanViewState, StudyPlanFeature.Message, StudyPlanFeature.Action> {
        val studyPlanReducer = StudyPlanReducer()
        val studyPlanDispatcher = StudyPlanActionDispatcher(ActionDispatcherOptions(), studyPlanInteractor)
        return ReduxFeature(StudyPlanFeature.State(), studyPlanReducer)
            .wrapWithActionDispatcher(studyPlanDispatcher)
            .transformState(StudyPlanViewStateMapper::map)
    }
}