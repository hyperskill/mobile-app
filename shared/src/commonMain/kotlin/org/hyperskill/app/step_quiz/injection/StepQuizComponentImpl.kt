package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.data.repository.AttemptRepositoryImpl
import org.hyperskill.app.step_quiz.data.source.AttemptRemoteDataSource
import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.domain.repository.AttemptRepository
import org.hyperskill.app.step_quiz.domain.validation.StepQuizReplyValidator
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.remote.AttemptRemoteDataSourceImpl
import org.hyperskill.app.step_quiz.view.mapper.StepQuizFeedbackMapper
import org.hyperskill.app.step_quiz.view.mapper.StepQuizStatsTextMapper
import org.hyperskill.app.step_quiz.view.mapper.StepQuizTitleMapper
import org.hyperskill.app.step_quiz_code_blanks.injection.StepQuizCodeBlanksComponent
import org.hyperskill.app.step_quiz_hints.injection.StepQuizHintsComponent
import org.hyperskill.app.step_quiz_toolbar.injection.StepQuizToolbarComponent
import ru.nobird.app.presentation.redux.feature.Feature

internal class StepQuizComponentImpl(
    private val appGraph: AppGraph,
    private val stepRoute: StepRoute
) : StepQuizComponent {

    private val resourceProvider: ResourceProvider = appGraph.commonComponent.resourceProvider

    private val stepQuizReplyValidator: StepQuizReplyValidator =
        StepQuizReplyValidator(resourceProvider)

    override val stepQuizStatsTextMapper: StepQuizStatsTextMapper
        get() = StepQuizStatsTextMapper(
            resourceProvider,
            appGraph.commonComponent.dateFormatter
        )

    override val stepQuizTitleMapper: StepQuizTitleMapper
        get() = StepQuizTitleMapper(resourceProvider)

    override val stepQuizFeedbackMapper: StepQuizFeedbackMapper
        get() = StepQuizFeedbackMapper(resourceProvider)

    private val attemptRemoteDataSource: AttemptRemoteDataSource =
        AttemptRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)
    private val attemptRepository: AttemptRepository =
        AttemptRepositoryImpl(attemptRemoteDataSource)

    override val stepQuizInteractor: StepQuizInteractor =
        StepQuizInteractor(
            attemptRepository = attemptRepository,
            submissionsRepository = appGraph.buildSubmissionsDataComponent().submissionsRepository,
            stepCompletedFlow = appGraph.stepCompletionFlowDataComponent.stepCompletedFlow
        )

    private val stepQuizHintsComponent: StepQuizHintsComponent =
        appGraph.buildStepQuizHintsComponent(stepRoute)

    private val stepQuizToolbarComponent: StepQuizToolbarComponent =
        appGraph.buildStepQuizToolbarComponent(stepRoute)

    private val stepQuizCodeBlanksComponent: StepQuizCodeBlanksComponent =
        appGraph.buildStepQuizCodeBlanksComponent(stepRoute)

    override val stepQuizFeature: Feature<StepQuizFeature.State, StepQuizFeature.Message, StepQuizFeature.Action>
        get() = StepQuizFeatureBuilder.build(
            stepRoute = stepRoute,
            stepQuizInteractor = stepQuizInteractor,
            stepQuizReplyValidator = stepQuizReplyValidator,
            subscriptionsInteractor = appGraph.subscriptionDataComponent.subscriptionsInteractor,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            urlPathProcessor = appGraph.buildMagicLinksDataComponent().urlPathProcessor,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            onboardingInteractor = appGraph.buildOnboardingDataComponent().onboardingInteractor,
            featuresDataSource = appGraph.profileDataComponent.featuresDataSource,
            runCodeRepository = appGraph.buildRunCodeDataComponent().runCodeRepository,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant,
            stepQuizHintsActionDispatcher = stepQuizHintsComponent.stepQuizHintsActionDispatcher,
            stepQuizHintsReducer = stepQuizHintsComponent.stepQuizHintsReducer,
            stepQuizToolbarReducer = stepQuizToolbarComponent.stepQuizToolbarReducer,
            stepQuizToolbarActionDispatcher = stepQuizToolbarComponent.stepQuizToolbarActionDispatcher,
            stepQuizCodeBlanksReducer = stepQuizCodeBlanksComponent.stepQuizCodeBlanksReducer,
            stepQuizCodeBlanksActionDispatcher = stepQuizCodeBlanksComponent.stepQuizCodeBlanksActionDispatcher
        )
}