package org.hyperskill.app.submissions.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.submissions.cache.SubmissionsCacheDataSourceImpl
import org.hyperskill.app.submissions.data.repository.SubmissionsRepositoryImpl
import org.hyperskill.app.submissions.domain.repository.SubmissionsRepository
import org.hyperskill.app.submissions.remote.SubmissionsRemoteDataSourceImpl

internal class SubmissionsDataComponentImpl(
    appGraph: AppGraph
) : SubmissionsDataComponent {
    override val submissionsRepository: SubmissionsRepository =
        SubmissionsRepositoryImpl(
            SubmissionsRemoteDataSourceImpl(
                appGraph.networkComponent.authorizedHttpClient
            ),
            SubmissionsCacheDataSourceImpl(
                appGraph.commonComponent.settings
            )
        )
}