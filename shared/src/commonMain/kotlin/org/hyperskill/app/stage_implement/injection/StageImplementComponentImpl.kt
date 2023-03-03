package org.hyperskill.app.stage_implement.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.presentation.redux.feature.Feature

class StageImplementComponentImpl(
    private val appGraph: AppGraph,
    private val stepRoute: StepRoute
) : StageImplementComponent {
    override val stageImplementFeature: Feature<StageImplementFeature.ViewState, StageImplementFeature.Message, StageImplementFeature.Action>
        get() = StageImplementFeatureBuilder.build(
            stepRoute,
            appGraph.buildProjectsDataComponent().projectsInteractor,
            appGraph.buildStagesDataComponent().stagesInteractor,
            appGraph.buildStepDataComponent().stepInteractor
        )
}