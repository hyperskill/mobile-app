package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.step_quiz.data.repository.AttemptRepositoryImpl
import org.hyperskill.app.step_quiz.data.repository.SubmissionRepositoryImpl
import org.hyperskill.app.step_quiz.data.source.AttemptRemoteDataSource
import org.hyperskill.app.step_quiz.data.source.SubmissionRemoteDataSource
import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.domain.repository.AttemptRepository
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository
import org.hyperskill.app.step_quiz.remote.AttemptRemoteDataSourceImpl
import org.hyperskill.app.step_quiz.remote.SubmissionRemoteDataSourceImpl

class StepQuizComponentImpl(private val appGraph: AppGraph) : StepQuizComponent {
    private val attemptRemoteDataSource: AttemptRemoteDataSource = AttemptRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val attemptRepository: AttemptRepository = AttemptRepositoryImpl(attemptRemoteDataSource)
    private val submissionRemoteDataSource: SubmissionRemoteDataSource = SubmissionRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val submissionRepository: SubmissionRepository = SubmissionRepositoryImpl(
        submissionRemoteDataSource
    )

    override val stepQuizInteractor: StepQuizInteractor
        get() = StepQuizInteractor(attemptRepository, submissionRepository)
}