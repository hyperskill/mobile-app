package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.step_quiz.data.repository.AttemptRepositoryImpl
import org.hyperskill.app.step_quiz.data.source.AttemptRemoteDataSource
import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.domain.repository.AttemptRepository
import org.hyperskill.app.step_quiz.remote.AttemptRemoteDataSourceImpl

class StepQuizDataComponentImpl(
    appGraph: AppGraph
) : StepQuizDataComponent {
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
}