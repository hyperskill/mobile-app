package org.hyperskill.app.profile_settings.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.notification.cache.NotificationCacheDataSourceImpl
import org.hyperskill.app.notification.data.repository.NotificationRepositoryImpl
import org.hyperskill.app.notification.data.source.NotificationCacheDataSource
import org.hyperskill.app.notification.domain.NotificationInteractor
import org.hyperskill.app.notification.domain.repository.NotificationRepository
import org.hyperskill.app.profile.cache.ProfileCacheDataSourceImpl
import org.hyperskill.app.profile.data.repository.ProfileRepositoryImpl
import org.hyperskill.app.profile.data.source.ProfileCacheDataSource
import org.hyperskill.app.profile.data.source.ProfileRemoteDataSource
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.profile.remote.ProfileRemoteDataSourceImpl
import org.hyperskill.app.profile_settings.cache.ProfileSettingsCacheDataSourceImpl
import org.hyperskill.app.profile_settings.data.repository.ProfileSettingsRepositoryImpl
import org.hyperskill.app.profile_settings.data.source.ProfileSettingsCacheDataSource
import org.hyperskill.app.profile_settings.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile_settings.domain.repository.ProfileSettingsRepository
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature
import ru.nobird.app.presentation.redux.feature.Feature

class ProfileSettingsComponentImpl(private val appGraph: AppGraph) : ProfileSettingsComponent {
    private val profileSettingsCacheDataSource: ProfileSettingsCacheDataSource =
        ProfileSettingsCacheDataSourceImpl(
            appGraph.commonComponent.json,
            appGraph.commonComponent.settings
        )

    private val profileSettingsRepository: ProfileSettingsRepository =
        ProfileSettingsRepositoryImpl(profileSettingsCacheDataSource)
    override val profileSettingsInteractor: ProfileSettingsInteractor =
        ProfileSettingsInteractor(profileSettingsRepository)

    private val profileRemoteDataSource: ProfileRemoteDataSource = ProfileRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val profileCacheDataSource: ProfileCacheDataSource = ProfileCacheDataSourceImpl(
        appGraph.commonComponent.json,
        appGraph.commonComponent.settings
    )
    private val profileRepository: ProfileRepository =
        ProfileRepositoryImpl(profileRemoteDataSource, profileCacheDataSource)
    private val profileInteractor: ProfileInteractor = ProfileInteractor(profileRepository, appGraph.submissionDataComponent.submissionRepository)

    private val notificationCacheDataSource: NotificationCacheDataSource = NotificationCacheDataSourceImpl(
        appGraph.commonComponent.settings,
        appGraph.commonComponent.resourceProvider
    )
    private val notificationRepository: NotificationRepository = NotificationRepositoryImpl(notificationCacheDataSource)
    private val notificationInteractor: NotificationInteractor = NotificationInteractor(
        notificationRepository,
        appGraph.submissionDataComponent.submissionRepository
    )

    override val profileSettingsFeature: Feature<ProfileSettingsFeature.State, ProfileSettingsFeature.Message, ProfileSettingsFeature.Action>
        get() = ProfileSettingsFeatureBuilder.build(
            profileSettingsInteractor,
            profileInteractor,
            notificationInteractor,
            appGraph.networkComponent.authorizationFlow
        )
}