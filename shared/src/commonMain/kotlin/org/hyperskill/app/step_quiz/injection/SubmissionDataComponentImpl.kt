package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.step_quiz.cache.SubmissionCacheDataSourceImpl
import org.hyperskill.app.step_quiz.data.repository.SubmissionRepositoryImpl
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository
import org.hyperskill.app.step_quiz.remote.SubmissionRemoteDataSourceImpl

class SubmissionDataComponentImpl(
    appGraph: AppGraph
) : SubmissionDataComponent {
    override val submissionRepository: SubmissionRepository =
        SubmissionRepositoryImpl(
            SubmissionRemoteDataSourceImpl(
                appGraph.networkComponent.authorizedHttpClient
            ),
            SubmissionCacheDataSourceImpl(
                appGraph.commonComponent.settings
            )
        )
}