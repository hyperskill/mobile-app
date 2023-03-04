package org.hyperskill.app.stage_implement.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature
import ru.nobird.app.presentation.redux.feature.Feature

class StageImplementComponentImpl(private val appGraph: AppGraph) : StageImplementComponent {
    override val stageImplementFeature: Feature<StageImplementFeature.ViewState, StageImplementFeature.Message, StageImplementFeature.Action>
        get() = StageImplementFeatureBuilder.build(
            appGraph.buildProjectsDataComponent().projectsInteractor,
            appGraph.buildStagesDataComponent().stagesInteractor,
            appGraph.buildStepDataComponent().stepInteractor,
            appGraph.commonComponent.resourceProvider
        )
}