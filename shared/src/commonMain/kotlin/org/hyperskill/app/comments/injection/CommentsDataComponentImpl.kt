package org.hyperskill.app.comments.injection

import org.hyperskill.app.comments.data.repository.CommentsRepositoryImpl
import org.hyperskill.app.comments.data.source.CommentsRemoteDataSource
import org.hyperskill.app.comments.domain.interactor.CommentsInteractor
import org.hyperskill.app.comments.domain.repository.CommentsRepository
import org.hyperskill.app.comments.remote.CommentsRemoteDataSourceImpl
import org.hyperskill.app.core.injection.AppGraph

class CommentsDataComponentImpl(appGraph: AppGraph) : CommentsDataComponent {
    private val commentsRemoteDataSource: CommentsRemoteDataSource =
        CommentsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    private val commentsRepository: CommentsRepository =
        CommentsRepositoryImpl(commentsRemoteDataSource)

    override val commentsInteractor: CommentsInteractor
        get() = CommentsInteractor(commentsRepository)
}