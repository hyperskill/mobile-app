package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.cache.AuthCacheDataSourceImpl
import org.hyperskill.app.auth.data.repository.AuthRepositoryImpl
import org.hyperskill.app.auth.data.source.AuthCacheDataSource
import org.hyperskill.app.auth.data.source.AuthRemoteDataSource
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.domain.repository.AuthRepository
import org.hyperskill.app.auth.remote.source.AuthRemoteDataSourceImpl
import org.hyperskill.app.core.injection.AppGraph

class AuthComponentImpl(
    appGraph: AppGraph
) : AuthComponent {
    private val authCacheDataSource: AuthCacheDataSource =
        AuthCacheDataSourceImpl(appGraph.commonComponent.settings)
    private val authRemoteDataSource: AuthRemoteDataSource =
        AuthRemoteDataSourceImpl(
            appGraph.networkComponent.authMutex,
            appGraph.networkComponent.authorizationFlow,
            appGraph.networkComponent.authSocialHttpClient,
            appGraph.networkComponent.authCredentialsHttpClient,
            appGraph.networkComponent.endpointConfigInfo,
            appGraph.commonComponent.json,
            appGraph.commonComponent.settings
        )
    private val authRepository: AuthRepository = AuthRepositoryImpl(
        authCacheDataSource,
        authRemoteDataSource
    )

    override val authInteractor: AuthInteractor = AuthInteractor(authRepository)
}