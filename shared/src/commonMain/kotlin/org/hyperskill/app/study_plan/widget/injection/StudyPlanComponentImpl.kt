package org.hyperskill.app.study_plan.widget.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewState
import ru.nobird.app.presentation.redux.feature.Feature

class StudyPlanComponentImpl(private val appGraph: AppGraph) : StudyPlanWidgetComponent {
    override val studyPlanWidgetFeature: Feature<StudyPlanWidgetViewState, StudyPlanWidgetFeature.Message, StudyPlanWidgetFeature.Action>
        get() = StudyPlanWidgetFeatureBuilder.build(
            studyPlanInteractor = appGraph.buildStudyPlanDataComponent().studyPlanInteractor
        )
}