package org.hyperskill.app.discussions.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.discussions.data.repository.DiscussionsRepositoryImpl
import org.hyperskill.app.discussions.data.source.DiscussionsRemoteDataSource
import org.hyperskill.app.discussions.domain.repository.DiscussionsRepository
import org.hyperskill.app.discussions.remote.DiscussionsRemoteDataSourceImpl

internal class DiscussionsDataComponentImpl(appGraph: AppGraph) : DiscussionsDataComponent {
    private val discussionsRemoteDataSource: DiscussionsRemoteDataSource =
        DiscussionsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val discussionsRepository: DiscussionsRepository =
        DiscussionsRepositoryImpl(discussionsRemoteDataSource)
}