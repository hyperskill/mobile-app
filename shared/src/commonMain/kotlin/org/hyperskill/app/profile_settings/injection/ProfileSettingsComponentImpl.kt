package org.hyperskill.app.profile_settings.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
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

    private val urlPathProcessor: UrlPathProcessor =
        appGraph.buildMagicLinksDataComponent().urlPathProcessor

    override val profileSettingsFeature: Feature<
        ProfileSettingsFeature.State, ProfileSettingsFeature.Message, ProfileSettingsFeature.Action>
        get() = ProfileSettingsFeatureBuilder.build(
            profileSettingsInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.networkComponent.authorizationFlow,
            appGraph.commonComponent.platform,
            appGraph.commonComponent.userAgentInfo,
            appGraph.commonComponent.resourceProvider,
            urlPathProcessor
        )
}