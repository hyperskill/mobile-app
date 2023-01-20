package org.hyperskill.app.step.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step.view.mapper.CommentThreadTitleMapper
import ru.nobird.app.presentation.redux.feature.Feature

class StepComponentImpl(private val appGraph: AppGraph) : StepComponent {
    override val commentThreadTitleMapper: CommentThreadTitleMapper
        get() = CommentThreadTitleMapper(appGraph.commonComponent.resourceProvider)

    override val stepFeature: Feature<StepFeature.State, StepFeature.Message, StepFeature.Action>
        get() = StepFeatureBuilder.build(
            appGraph.stepDataComponent.stepInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.stepQuizDataComponent.stepQuizInteractor.failedToLoadNextStepQuizMutableSharedFlow
        )
}