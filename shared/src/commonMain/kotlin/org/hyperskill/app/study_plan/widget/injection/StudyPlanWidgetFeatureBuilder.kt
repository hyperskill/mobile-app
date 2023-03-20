package org.hyperskill.app.study_plan.widget.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetActionDispatcher
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewState
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object StudyPlanWidgetFeatureBuilder {
    fun build(
        studyPlanInteractor: StudyPlanInteractor
    ): Feature<StudyPlanWidgetViewState, StudyPlanWidgetFeature.Message, StudyPlanWidgetFeature.Action> {
        val studyPlanReducer = StudyPlanWidgetReducer()
        val studyPlanDispatcher = StudyPlanWidgetActionDispatcher(ActionDispatcherOptions(), studyPlanInteractor)
        return ReduxFeature(StudyPlanWidgetFeature.State(), studyPlanReducer)
            .wrapWithActionDispatcher(studyPlanDispatcher)
            .transformState(StudyPlanWidgetViewStateMapper::map)
    }
}