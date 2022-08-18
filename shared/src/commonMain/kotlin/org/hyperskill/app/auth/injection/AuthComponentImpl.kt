package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.cache.AuthCacheDataSourceImpl
import org.hyperskill.app.auth.data.repository.AuthRepositoryImpl
import org.hyperskill.app.auth.data.source.AuthCacheDataSource
import org.hyperskill.app.auth.data.source.AuthRemoteDataSource
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.domain.repository.AuthRepository
import org.hyperskill.app.auth.remote.source.AuthRemoteDataSourceImpl
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.profile.cache.ProfileCacheDataSourceImpl
import org.hyperskill.app.profile.data.repository.ProfileRepositoryImpl
import org.hyperskill.app.profile.data.source.ProfileCacheDataSource
import org.hyperskill.app.profile.data.source.ProfileRemoteDataSource
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.profile.remote.ProfileRemoteDataSourceImpl

class AuthComponentImpl(
    appGraph: AppGraph
) : AuthComponent {

    private val authCacheDataSource: AuthCacheDataSource = AuthCacheDataSourceImpl(appGraph.commonComponent.settings)
    private val authRemoteDataSource: AuthRemoteDataSource = AuthRemoteDataSourceImpl(
        appGraph.networkComponent.authMutex,
        appGraph.networkComponent.authorizationFlow,
        appGraph.networkComponent.authSocialHttpClient,
        appGraph.networkComponent.authCredentialsHttpClient,
        appGraph.commonComponent.json,
        appGraph.commonComponent.settings
    )

    private val authRepository: AuthRepository = AuthRepositoryImpl(
        authCacheDataSource,
        authRemoteDataSource
    )

    private val profileCacheDataSource: ProfileCacheDataSource = ProfileCacheDataSourceImpl(
        appGraph.commonComponent.json,
        appGraph.commonComponent.settings
    )
    private val profileRemoteDataSource: ProfileRemoteDataSource = ProfileRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )

    private val profileRepository: ProfileRepository = ProfileRepositoryImpl(
        profileRemoteDataSource,
        profileCacheDataSource
    )

    override val authInteractor: AuthInteractor = AuthInteractor(authRepository)
    override val profileInteractor: ProfileInteractor = ProfileInteractor(profileRepository, appGraph.submissionDataComponent.submissionRepository)
}