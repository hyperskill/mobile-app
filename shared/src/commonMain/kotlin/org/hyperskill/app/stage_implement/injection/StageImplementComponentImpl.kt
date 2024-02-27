package org.hyperskill.app.stage_implement.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Action
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Message
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class StageImplementComponentImpl(
    private val appGraph: AppGraph,
    private val projectId: Long,
    private val stageId: Long
) : StageImplementComponent {
    override val stageImplementFeature: Feature<ViewState, Message, Action>
        get() = StageImplementFeatureBuilder.build(
            projectId,
            stageId,
            appGraph.buildStagesDataComponent().stagesInteractor,
            appGraph.buildProgressesDataComponent().progressesInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.commonComponent.resourceProvider,
            appGraph.profileDataComponent.currentProfileStateRepository,
            appGraph.stepCompletionFlowDataComponent.stepCompletedFlow,
            appGraph.loggerComponent.logger,
            appGraph.commonComponent.buildKonfig.buildVariant
        )
}