package org.hyperskill.app.profile.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.profile.cache.ProfileCacheDataSourceImpl
import org.hyperskill.app.profile.data.repository.ProfileRepositoryImpl
import org.hyperskill.app.profile.data.source.ProfileCacheDataSource
import org.hyperskill.app.profile.data.source.ProfileRemoteDataSource
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.profile.remote.ProfileRemoteDataSourceImpl

class ProfileDataComponentImpl(private val appGraph: AppGraph) : ProfileDataComponent {
    private val profileRemoteDataSource: ProfileRemoteDataSource = ProfileRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val profileCacheDataSource: ProfileCacheDataSource = ProfileCacheDataSourceImpl(
        appGraph.commonComponent.json,
        appGraph.commonComponent.settings
    )
    private val profileRepository: ProfileRepository = ProfileRepositoryImpl(
        profileRemoteDataSource,
        profileCacheDataSource
    )
    override val profileInteractor: ProfileInteractor
        get() = ProfileInteractor(
            profileRepository,
            appGraph.profileHypercoinsDataComponent.hypercoinsBalanceMutableSharedFlow,
            appGraph.profileStreakDataComponent.streakMutableSharedFlow,
            appGraph.submissionDataComponent.submissionRepository
        )
}