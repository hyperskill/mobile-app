package org.hyperskill.app.study_plan.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature
import org.hyperskill.app.study_plan.view.StudyPlanViewState
import ru.nobird.app.presentation.redux.feature.Feature

class StudyPlanComponentImpl(private val appGraph: AppGraph) : StudyPlanComponent {
    override val studyPlanFeature: Feature<StudyPlanViewState, StudyPlanFeature.Message, StudyPlanFeature.Action>
        get() = StudyPlanFeatureBuilder.build(
            studyPlanInteractor = appGraph.buildStudyPlanDataComponent().studyPlanInteractor
        )
}