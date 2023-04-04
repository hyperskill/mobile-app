package org.hyperskill.app.core.injection

import org.hyperskill.app.core.data.repository_cache.InMemoryRepositoryCache
import org.hyperskill.app.progresses.cache.ProjectProgressesCacheDataSourceImpl
import org.hyperskill.app.progresses.cache.TrackProgressesCacheDataSourceImpl
import org.hyperskill.app.progresses.data.source.ProjectProgressesCacheDataSource
import org.hyperskill.app.progresses.data.source.TrackProgressesCacheDataSource
import org.hyperskill.app.study_plan.data.repository.CurrentStudyPlanStateRepositoryImpl
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.study_plan.remote.StudyPlanRemoteDataSourceImpl
import org.hyperskill.app.subscriptions.data.repository.CurrentSubscriptionStateRepositoryImpl
import org.hyperskill.app.subscriptions.data.source.SubscriptionsRemoteDataSource
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.subscriptions.remote.SubscriptionsRemoteDataSourceImpl
import org.hyperskill.app.track.cache.TrackCacheDataSourceImpl
import org.hyperskill.app.track.data.source.TrackCacheDataSource

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
     * Study plan
     */
    override val studyPlanStateRepository: CurrentStudyPlanStateRepository by lazy {
        CurrentStudyPlanStateRepositoryImpl(StudyPlanRemoteDataSourceImpl(authorizedHttpClient))
    }

    /**
     * Progress
     */
    override val trackProgressesCacheDataSource: TrackProgressesCacheDataSource by lazy {
        TrackProgressesCacheDataSourceImpl(InMemoryRepositoryCache())
    }

    override val projectProgressesCacheDataSource: ProjectProgressesCacheDataSource by lazy {
        ProjectProgressesCacheDataSourceImpl(InMemoryRepositoryCache())
    }

    override val trackCacheDataSource: TrackCacheDataSource by lazy {
        TrackCacheDataSourceImpl(InMemoryRepositoryCache())
    }
}