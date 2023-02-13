package org.hyperskill.app.step.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step.view.mapper.CommentThreadTitleMapper
import org.hyperskill.app.step_completion.injection.StepCompletionComponent
import ru.nobird.app.presentation.redux.feature.Feature

class StepComponentImpl(private val appGraph: AppGraph, private val stepRoute: StepRoute) : StepComponent {
    override val commentThreadTitleMapper: CommentThreadTitleMapper
        get() = CommentThreadTitleMapper(appGraph.commonComponent.resourceProvider)

    private val stepCompletionComponent: StepCompletionComponent =
        appGraph.buildStepCompletionComponent(stepRoute)

    override val stepFeature: Feature<StepFeature.State, StepFeature.Message, StepFeature.Action>
        get() = StepFeatureBuilder.build(
            stepRoute,
            appGraph.buildStepDataComponent().stepInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            stepCompletionComponent.stepCompletionReducer,
            stepCompletionComponent.stepCompletionActionDispatcher
        )
}