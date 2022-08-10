package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.step_quiz.data.repository.SubmissionRepositoryImpl
import org.hyperskill.app.step_quiz.data.source.SubmissionRemoteDataSource
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository

class SubmissionDataComponentImpl(
    private val submissionRemoteDataSource: SubmissionRemoteDataSource
) : SubmissionDataComponent {
    override val submissionRepository: SubmissionRepository =
        SubmissionRepositoryImpl(submissionRemoteDataSource)
}