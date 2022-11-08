package org.hyperskill.app.discussions.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.discussions.data.repository.DiscussionsRepositoryImpl
import org.hyperskill.app.discussions.data.source.DiscussionsRemoteDataSource
import org.hyperskill.app.discussions.domain.interactor.DiscussionsInteractor
import org.hyperskill.app.discussions.domain.repository.DiscussionsRepository
import org.hyperskill.app.discussions.remote.DiscussionsRemoteDataSourceImpl

class DiscussionsDataComponentImpl(private val appGraph: AppGraph) : DiscussionsDataComponent {
    private val discussionsRemoteDataSource: DiscussionsRemoteDataSource =
        DiscussionsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)
    private val discussionsRepository: DiscussionsRepository =
        DiscussionsRepositoryImpl(discussionsRemoteDataSource)

    override val discussionsInteractor: DiscussionsInteractor
        get() = DiscussionsInteractor(
            discussionsRepository,
            appGraph.buildCommentsDataComponent().commentsRepository
        )
}