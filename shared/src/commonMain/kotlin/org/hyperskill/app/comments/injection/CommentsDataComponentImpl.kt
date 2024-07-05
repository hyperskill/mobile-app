package org.hyperskill.app.comments.injection

import org.hyperskill.app.comments.data.repository.CommentsRepositoryImpl
import org.hyperskill.app.comments.data.source.CommentsRemoteDataSource
import org.hyperskill.app.comments.domain.repository.CommentsRepository
import org.hyperskill.app.comments.remote.CommentsRemoteDataSourceImpl
import org.hyperskill.app.core.injection.AppGraph

internal class CommentsDataComponentImpl(appGraph: AppGraph) : CommentsDataComponent {
    private val commentsRemoteDataSource: CommentsRemoteDataSource =
        CommentsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val commentsRepository: CommentsRepository =
        CommentsRepositoryImpl(commentsRemoteDataSource)
}