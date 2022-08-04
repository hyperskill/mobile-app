package org.hyperskill.app.profile.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.profile.cache.ProfileSettingsCacheDataSourceImpl
import org.hyperskill.app.profile.data.repository.ProfileSettingsRepositoryImpl
import org.hyperskill.app.profile.data.source.ProfileSettingsCacheDataSource
import org.hyperskill.app.profile.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile.domain.repository.ProfileSettingsRepository
import org.hyperskill.app.profile.presentation.ProfileSettingsFeature
import ru.nobird.app.presentation.redux.feature.Feature

class ProfileSettingsComponentImpl(private val appGraph: AppGraph) : ProfileSettingsComponent {
    private val profileSettingsCacheDataSource: ProfileSettingsCacheDataSource = ProfileSettingsCacheDataSourceImpl(
        appGraph.commonComponent.json,
        appGraph.commonComponent.settings
    )

    private val profileSettingsRepository: ProfileSettingsRepository = ProfileSettingsRepositoryImpl(profileSettingsCacheDataSource)
    private val profileSettingsInteractor: ProfileSettingsInteractor = ProfileSettingsInteractor(profileSettingsRepository)

    override val profileSettingsFeature: Feature<ProfileSettingsFeature.State, ProfileSettingsFeature.Message, ProfileSettingsFeature.Action>
        get() = ProfileSettingsFeatureBuilder.build(profileSettingsInteractor)
}