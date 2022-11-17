package org.hyperskill.app.topics.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.topics.data.repository.TopicsRepositoryImpl
import org.hyperskill.app.topics.data.source.TopicsRemoteDataSource
import org.hyperskill.app.topics.domain.interactor.TopicsInteractor
import org.hyperskill.app.topics.domain.repository.TopicsRepository
import org.hyperskill.app.topics.remote.TopicsRemoteDataSourceImpl

class TopicsDataComponentImpl(appGraph: AppGraph) : TopicsDataComponent {
    private val topicsRemoteDataSource: TopicsRemoteDataSource =
        TopicsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    private val topicsRepository: TopicsRepository =
        TopicsRepositoryImpl(topicsRemoteDataSource)

    override val topicsInteractor: TopicsInteractor
        get() = TopicsInteractor(topicsRepository)
}