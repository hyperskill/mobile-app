package org.hyperskill.app.topics_repetitions.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.topics_repetitions.data.repository.TopicsRepetitionsRepositoryImpl
import org.hyperskill.app.topics_repetitions.data.source.TopicsRepetitionsRemoteDataSource
import org.hyperskill.app.topics_repetitions.domain.interactor.TopicsRepetitionsInteractor
import org.hyperskill.app.topics_repetitions.domain.repository.TopicsRepetitionsRepository
import org.hyperskill.app.topics_repetitions.remote.TopicsRepetitionsRemoteDataSourceImpl

class TopicsRepetitionsDataComponentImpl(appGraph: AppGraph) : TopicsRepetitionsDataComponent {
    private val topicsRepetitionsRemoteDataSource: TopicsRepetitionsRemoteDataSource =
        TopicsRepetitionsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    private val topicsRepetitionsRepository: TopicsRepetitionsRepository =
        TopicsRepetitionsRepositoryImpl(topicsRepetitionsRemoteDataSource)

    override val topicsRepetitionsInteractor: TopicsRepetitionsInteractor
        get() = TopicsRepetitionsInteractor(topicsRepetitionsRepository)
}