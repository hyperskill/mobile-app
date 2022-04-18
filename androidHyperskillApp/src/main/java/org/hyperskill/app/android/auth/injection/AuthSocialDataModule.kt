package org.hyperskill.app.android.auth.injection

import com.russhwolf.settings.Settings
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.hyperskill.app.android.network.injection.AuthHttpClient
import org.hyperskill.app.android.network.injection.CredentialsHttpClient
import org.hyperskill.app.auth.cache.AuthCacheDataSourceImpl
import org.hyperskill.app.auth.data.repository.AuthRepositoryImpl
import org.hyperskill.app.auth.data.source.AuthCacheDataSource
import org.hyperskill.app.auth.data.source.AuthRemoteDataSource
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.domain.repository.AuthRepository
import org.hyperskill.app.auth.remote.source.AuthRemoteDataSourceImpl

@Module
object AuthSocialDataModule {
    @Provides
    fun provideAuthCacheDataSource(settings: Settings): AuthCacheDataSource =
        AuthCacheDataSourceImpl(settings)

    @Provides
    fun provideAuthRemoteDataSource(
        @AuthHttpClient
        authHttpClient: HttpClient,
        @CredentialsHttpClient
        credentialsHttpClient: HttpClient,
        json: Json,
        settings: Settings
    ): AuthRemoteDataSource =
        AuthRemoteDataSourceImpl(authHttpClient, credentialsHttpClient, json, settings)

    @Provides
    fun provideAuthRepository(authCacheDataSource: AuthCacheDataSource, authRemoteDataSource: AuthRemoteDataSource): AuthRepository =
        AuthRepositoryImpl(authCacheDataSource, authRemoteDataSource)

    @Provides
    fun provideAuthInteractor(authRepository: AuthRepository): AuthInteractor =
        AuthInteractor(authRepository)
}