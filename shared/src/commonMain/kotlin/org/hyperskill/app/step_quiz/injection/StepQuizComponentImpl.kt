package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.notification.domain.NotificationInteractor
import org.hyperskill.app.step_quiz.data.repository.AttemptRepositoryImpl
import org.hyperskill.app.step_quiz.data.source.AttemptRemoteDataSource
import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.domain.repository.AttemptRepository
import org.hyperskill.app.step_quiz.presentation.StepQuizActionDispatcher
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizReducer
import org.hyperskill.app.step_quiz.remote.AttemptRemoteDataSourceImpl
import org.hyperskill.app.step_quiz.view.mapper.StepQuizStatsTextMapper
import org.hyperskill.app.step_quiz.view.mapper.StepQuizTitleMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

class StepQuizComponentImpl(private val appGraph: AppGraph) : StepQuizComponent {
    private val attemptRemoteDataSource: AttemptRemoteDataSource = AttemptRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val attemptRepository: AttemptRepository =
        AttemptRepositoryImpl(attemptRemoteDataSource)

    override val stepQuizStatsTextMapper: StepQuizStatsTextMapper
        get() = StepQuizStatsTextMapper(appGraph.commonComponent.resourceProvider)

    override val stepQuizTitleMapper: StepQuizTitleMapper
        get() = StepQuizTitleMapper(appGraph.commonComponent.resourceProvider)

    override val stepQuizInteractor: StepQuizInteractor
        get() = StepQuizInteractor(
            attemptRepository,
            appGraph.submissionDataComponent.submissionRepository
        )

    private val notificationInteractor: NotificationInteractor = appGraph.buildNotificationComponent().notificationInteractor

    override val stepQuizFeature: Feature<StepQuizFeature.State, StepQuizFeature.Message, StepQuizFeature.Action>
        get() {
            val stepQuizReducer = StepQuizReducer()
            val stepQuizActionDispatcher = StepQuizActionDispatcher(
                ActionDispatcherOptions(),
                appGraph.analyticComponent.analyticInteractor,
                stepQuizInteractor,
                appGraph.buildProfileDataComponent().profileInteractor,
                notificationInteractor
            )

            return ReduxFeature(StepQuizFeature.State.Idle, stepQuizReducer)
                .wrapWithActionDispatcher(stepQuizActionDispatcher)
        }
}