package org.hyperskill.app.likes.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.likes.data.repository.LikesRepositoryImpl
import org.hyperskill.app.likes.data.source.LikesRemoteDataSource
import org.hyperskill.app.likes.domain.interactor.LikesInteractor
import org.hyperskill.app.likes.domain.repository.LikesRepository
import org.hyperskill.app.likes.remote.LikesRemoteDataSourceImpl

class LikesDataComponentImpl(
    appGraph: AppGraph
) : LikesDataComponent {
    private val likesRemoteDataSource: LikesRemoteDataSource =
        LikesRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)
    private val likesRepository: LikesRepository =
        LikesRepositoryImpl(likesRemoteDataSource)

    override val likesInteractor: LikesInteractor
        get() = LikesInteractor(likesRepository)
}