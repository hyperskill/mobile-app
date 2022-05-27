package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.cache.AuthCacheDataSourceImpl
import org.hyperskill.app.auth.data.repository.AuthRepositoryImpl
import org.hyperskill.app.auth.data.source.AuthCacheDataSource
import org.hyperskill.app.auth.data.source.AuthRemoteDataSource
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.domain.repository.AuthRepository
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature
import org.hyperskill.app.auth.presentation.AuthSocialFeature
import org.hyperskill.app.auth.remote.source.AuthRemoteDataSourceImpl
import org.hyperskill.app.core.injection.AppGraph
import ru.nobird.app.presentation.redux.feature.Feature

class AuthComponentImpl(
    appGraph: AppGraph
) : AuthComponentManual {

    private val authCacheDataSource: AuthCacheDataSource = AuthCacheDataSourceImpl(appGraph.commonComponent.settings)
    private val authRemoteDataSource: AuthRemoteDataSource = AuthRemoteDataSourceImpl(
        appGraph.networkComponentManual.authMutex,
        appGraph.networkComponentManual.authorizationFlow,
        appGraph.networkComponentManual.authSocialHttpClient,
        appGraph.networkComponentManual.authCredentialsHttpClient,
        appGraph.commonComponent.json,
        appGraph.commonComponent.settings
    )

    private val authRepository: AuthRepository = AuthRepositoryImpl(
        authCacheDataSource,
        authRemoteDataSource
    )

    override val authInteractor: AuthInteractor = AuthInteractor(authRepository)

    override val authCredentialsFeature: Feature<AuthCredentialsFeature.State, AuthCredentialsFeature.Message, AuthCredentialsFeature.Action> =
        AuthCredentialsFeatureBuilder.build(authInteractor)

    override val authSocialFeature: Feature<AuthSocialFeature.State, AuthSocialFeature.Message, AuthSocialFeature.Action> =
        AuthSocialFeatureBuilder.build(authInteractor)
}