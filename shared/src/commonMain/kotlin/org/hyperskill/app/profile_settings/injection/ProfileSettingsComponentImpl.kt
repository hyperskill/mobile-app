package org.hyperskill.app.profile_settings.injection

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
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature
import org.hyperskill.app.profile_settings.cache.ProfileSettingsCacheDataSourceImpl
import org.hyperskill.app.profile_settings.data.repository.ProfileSettingsRepositoryImpl
import org.hyperskill.app.profile_settings.data.source.ProfileSettingsCacheDataSource
import org.hyperskill.app.profile_settings.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile_settings.domain.repository.ProfileSettingsRepository
import ru.nobird.app.presentation.redux.feature.Feature

class ProfileSettingsComponentImpl(private val appGraph: AppGraph) : ProfileSettingsComponent {
    private val profileSettingsCacheDataSource: ProfileSettingsCacheDataSource =
        ProfileSettingsCacheDataSourceImpl(
            appGraph.commonComponent.json,
            appGraph.commonComponent.settings
        )

    private val profileSettingsRepository: ProfileSettingsRepository =
        ProfileSettingsRepositoryImpl(profileSettingsCacheDataSource)
    private val profileSettingsInteractor: ProfileSettingsInteractor =
        ProfileSettingsInteractor(profileSettingsRepository)

    private val authCacheDataSource: AuthCacheDataSource =
        AuthCacheDataSourceImpl(appGraph.commonComponent.settings)
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
    private val authInteractor: AuthInteractor =
        AuthInteractor(authRepository)

    private val profileRemoteDataSource: ProfileRemoteDataSource = ProfileRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val profileCacheDataSource: ProfileCacheDataSource = ProfileCacheDataSourceImpl(
        appGraph.commonComponent.json,
        appGraph.commonComponent.settings
    )
    private val profileRepository: ProfileRepository =
        ProfileRepositoryImpl(profileRemoteDataSource, profileCacheDataSource)
    private val profileInteractor: ProfileInteractor = ProfileInteractor(profileRepository)

    override val profileSettingsFeature: Feature<ProfileSettingsFeature.State, ProfileSettingsFeature.Message, ProfileSettingsFeature.Action>
        get() = ProfileSettingsFeatureBuilder.build(profileSettingsInteractor, authInteractor, profileInteractor)
}