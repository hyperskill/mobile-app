package org.hyperskill.app.core.injection

import org.hyperskill.app.core.data.repository_cache.InMemoryRepositoryCache
import org.hyperskill.app.progresses.cache.ProjectProgressesCacheDataSourceImpl
import org.hyperskill.app.progresses.cache.TrackProgressesCacheDataSourceImpl
import org.hyperskill.app.progresses.data.source.ProjectProgressesCacheDataSource
import org.hyperskill.app.progresses.data.source.TrackProgressesCacheDataSource
import org.hyperskill.app.subscriptions.data.repository.CurrentSubscriptionStateRepositoryImpl
import org.hyperskill.app.subscriptions.data.source.SubscriptionsRemoteDataSource
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.subscriptions.remote.SubscriptionsRemoteDataSourceImpl

class SingletonRepositoriesComponentImpl(appGraph: AppGraph) : SingletonRepositoriesComponent {

    private val authorizedHttpClient = appGraph.networkComponent.authorizedHttpClient

    /**
     * Current subscription
     */
    private val subscriptionsRemoteDataSource: SubscriptionsRemoteDataSource =
        SubscriptionsRemoteDataSourceImpl(authorizedHttpClient)

    override val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository =
        CurrentSubscriptionStateRepositoryImpl(subscriptionsRemoteDataSource)

    /**
     * Progress
     */
    override val trackProgressesCacheDataSource: TrackProgressesCacheDataSource by lazy {
        TrackProgressesCacheDataSourceImpl(InMemoryRepositoryCache())
    }

    override val projectProgressesCacheDataSource: ProjectProgressesCacheDataSource by lazy {
        ProjectProgressesCacheDataSourceImpl(InMemoryRepositoryCache())
    }
}