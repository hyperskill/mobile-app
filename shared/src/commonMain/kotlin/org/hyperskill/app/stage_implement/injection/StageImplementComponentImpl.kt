package org.hyperskill.app.stage_implement.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature
import ru.nobird.app.presentation.redux.feature.Feature

class StageImplementComponentImpl(
    private val appGraph: AppGraph,
    private val projectId: Long,
    private val stageId: Long
) : StageImplementComponent {
    override val stageImplementFeature: Feature<StageImplementFeature.ViewState, StageImplementFeature.Message, StageImplementFeature.Action>
        get() = StageImplementFeatureBuilder.build(
            projectId,
            stageId,
            appGraph.buildProjectsDataComponent().projectsInteractor,
            appGraph.buildStagesDataComponent().stagesInteractor,
            appGraph.buildStepDataComponent().stepInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.commonComponent.resourceProvider
        )
}