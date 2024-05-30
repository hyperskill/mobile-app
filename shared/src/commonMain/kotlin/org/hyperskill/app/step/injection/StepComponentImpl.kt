package org.hyperskill.app.step.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step.view.mapper.CommentThreadTitleMapper
import org.hyperskill.app.step_completion.injection.StepCompletionComponent
import org.hyperskill.app.step_toolbar.injection.StepToolbarComponent
import ru.nobird.app.presentation.redux.feature.Feature

internal class StepComponentImpl(
    private val appGraph: AppGraph,
    private val stepRoute: StepRoute
) : StepComponent {
    override val commentThreadTitleMapper: CommentThreadTitleMapper
        get() = CommentThreadTitleMapper(appGraph.commonComponent.resourceProvider)

    private val stepCompletionComponent: StepCompletionComponent =
        appGraph.buildStepCompletionComponent(stepRoute)

    private val stepToolbarComponent: StepToolbarComponent =
        appGraph.buildStepToolbarComponent(stepRoute)

    /*ktlint-disable*/
    override val stepFeature: Feature<StepFeature.ViewState, StepFeature.Message, StepFeature.Action>
        get() = StepFeatureBuilder.build(
            stepRoute = stepRoute,
            stepInteractor = appGraph.buildStepDataComponent().stepInteractor,
            stepCompletedFlow = appGraph.stepCompletionFlowDataComponent.stepCompletedFlow,
            nextLearningActivityStateRepository = appGraph.stateRepositoriesComponent.nextLearningActivityStateRepository,
            learningActivitiesRepository = appGraph.buildLearningActivitiesDataComponent().learningActivitiesRepository,
            urlBuilder = appGraph.networkComponent.urlBuilder,
            magicLinksInteractor = appGraph.buildMagicLinksDataComponent().magicLinksInteractor,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            stepCompletionReducer = stepCompletionComponent.stepCompletionReducer,
            stepCompletionActionDispatcher = stepCompletionComponent.stepCompletionActionDispatcher,
            stepToolbarReducer = stepToolbarComponent.stepToolbarReducer,
            stepToolbarActionDispatcher = stepToolbarComponent.stepToolbarActionDispatcher,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}