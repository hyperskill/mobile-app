package org.hyperskill.app.topics_repetitions.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.topics_repetitions.data.repository.TopicsRepetitionsRepositoryImpl
import org.hyperskill.app.topics_repetitions.data.source.TopicsRepetitionsRemoteDataSource
import org.hyperskill.app.topics_repetitions.domain.interactor.TopicsRepetitionsInteractor
import org.hyperskill.app.topics_repetitions.domain.repository.TopicsRepetitionsRepository
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature
import org.hyperskill.app.topics_repetitions.remote.TopicsRepetitionsRemoteDataSourceImpl
import org.hyperskill.app.topics_repetitions.view.mapper.TopicsRepetitionsViewDataMapper
import ru.nobird.app.presentation.redux.feature.Feature

class TopicsRepetitionsComponentImpl(private val appGraph: AppGraph) : TopicsRepetitionsComponent {
    private val topicsRepetitionsRemoteDataSource: TopicsRepetitionsRemoteDataSource =
        TopicsRepetitionsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    private val topicsRepetitionsRepository: TopicsRepetitionsRepository =
        TopicsRepetitionsRepositoryImpl(topicsRepetitionsRemoteDataSource)

    private val topicsRepetitionsInteractor: TopicsRepetitionsInteractor =
        TopicsRepetitionsInteractor(topicsRepetitionsRepository)

    override val topicsRepetitionsFeature: Feature<TopicsRepetitionsFeature.State, TopicsRepetitionsFeature.Message, TopicsRepetitionsFeature.Action>
        get() = TopicsRepetitionsFeatureBuilder.build(
            topicsRepetitionsInteractor,
            appGraph.buildTopicsDataComponent().topicsInteractor,
            appGraph.buildProgressesDataComponent().progressesInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.analyticComponent.analyticInteractor
        )
    override val topicsRepetitionsViewDataMapper: TopicsRepetitionsViewDataMapper
        get() = TopicsRepetitionsViewDataMapper(appGraph.commonComponent.resourceProvider)
}