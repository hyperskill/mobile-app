package org.hyperskill.app.android.auth.injection

import com.russhwolf.settings.Settings
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import org.hyperskill.app.android.network.injection.AuthSocialHttpClient
import org.hyperskill.app.android.network.injection.AuthCredentialsHttpClient
import org.hyperskill.app.auth.cache.AuthCacheDataSourceImpl
import org.hyperskill.app.auth.data.repository.AuthRepositoryImpl
import org.hyperskill.app.auth.data.source.AuthCacheDataSource
import org.hyperskill.app.auth.data.source.AuthRemoteDataSource
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.auth.domain.repository.AuthRepository
import org.hyperskill.app.auth.remote.source.AuthRemoteDataSourceImpl

@Module
object AuthDataModule {
    @Provides
    fun provideAuthCacheDataSource(settings: Settings): AuthCacheDataSource =
        AuthCacheDataSourceImpl(settings)

    @Provides
    fun provideAuthRemoteDataSource(
        deauthorizationFlow: Flow<UserDeauthorized>,
        @AuthSocialHttpClient
        authHttpClient: HttpClient,
        @AuthCredentialsHttpClient
        credentialsHttpClient: HttpClient,
        json: Json,
        settings: Settings
    ): AuthRemoteDataSource =
        AuthRemoteDataSourceImpl(deauthorizationFlow, authHttpClient, credentialsHttpClient, json, settings)

    @Provides
    fun provideAuthRepository(authCacheDataSource: AuthCacheDataSource, authRemoteDataSource: AuthRemoteDataSource): AuthRepository =
        AuthRepositoryImpl(authCacheDataSource, authRemoteDataSource)

    @Provides
    fun provideAuthInteractor(authRepository: AuthRepository): AuthInteractor =
        AuthInteractor(authRepository)
}