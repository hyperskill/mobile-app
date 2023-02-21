package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.data.repository.AttemptRepositoryImpl
import org.hyperskill.app.step_quiz.data.source.AttemptRemoteDataSource
import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.domain.repository.AttemptRepository
import org.hyperskill.app.step_quiz.domain.validation.StepQuizReplyValidator
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.remote.AttemptRemoteDataSourceImpl
import org.hyperskill.app.step_quiz.view.mapper.StepQuizStatsTextMapper
import org.hyperskill.app.step_quiz.view.mapper.StepQuizTitleMapper
import org.hyperskill.app.step_quiz.view.mapper.StepQuizUserPermissionRequestTextMapper
import ru.nobird.app.presentation.redux.feature.Feature

class StepQuizComponentImpl(
    private val appGraph: AppGraph,
    private val stepRoute: StepRoute
) : StepQuizComponent {
    private val stepQuizReplyValidator: StepQuizReplyValidator =
        StepQuizReplyValidator(appGraph.commonComponent.resourceProvider)

    override val stepQuizStatsTextMapper: StepQuizStatsTextMapper
        get() = StepQuizStatsTextMapper(appGraph.commonComponent.resourceProvider)

    override val stepQuizTitleMapper: StepQuizTitleMapper
        get() = StepQuizTitleMapper(appGraph.commonComponent.resourceProvider)

    override val stepQuizUserPermissionRequestTextMapper: StepQuizUserPermissionRequestTextMapper
        get() = StepQuizUserPermissionRequestTextMapper(appGraph.commonComponent.resourceProvider)

    private val attemptRemoteDataSource: AttemptRemoteDataSource = AttemptRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val attemptRepository: AttemptRepository =
        AttemptRepositoryImpl(attemptRemoteDataSource)

    override val stepQuizInteractor: StepQuizInteractor =
        StepQuizInteractor(
            attemptRepository,
            appGraph.submissionDataComponent.submissionRepository
        )

    override val stepQuizFeature: Feature<StepQuizFeature.State, StepQuizFeature.Message, StepQuizFeature.Action>
        get() = StepQuizFeatureBuilder.build(
            stepRoute,
            stepQuizInteractor,
            stepQuizReplyValidator,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.buildNotificationComponent().notificationInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.commonComponent.resourceProvider
        )
}