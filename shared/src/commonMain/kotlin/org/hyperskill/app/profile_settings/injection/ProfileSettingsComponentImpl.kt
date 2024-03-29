package org.hyperskill.app.profile_settings.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.profile_settings.cache.ProfileSettingsCacheDataSourceImpl
import org.hyperskill.app.profile_settings.data.repository.ProfileSettingsRepositoryImpl
import org.hyperskill.app.profile_settings.data.source.ProfileSettingsCacheDataSource
import org.hyperskill.app.profile_settings.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile_settings.domain.repository.ProfileSettingsRepository
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class ProfileSettingsComponentImpl(
    private val appGraph: AppGraph
) : ProfileSettingsComponent {
    private val profileSettingsCacheDataSource: ProfileSettingsCacheDataSource =
        ProfileSettingsCacheDataSourceImpl(
            appGraph.commonComponent.json,
            appGraph.commonComponent.settings
        )

    private val profileSettingsRepository: ProfileSettingsRepository =
        ProfileSettingsRepositoryImpl(profileSettingsCacheDataSource)

    override val profileSettingsInteractor: ProfileSettingsInteractor =
        ProfileSettingsInteractor(profileSettingsRepository)

    override val profileSettingsFeature: Feature<ViewState, Message, Action>
        get() = ProfileSettingsFeatureBuilder.build(
            profileSettingsInteractor = profileSettingsInteractor,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            authorizationFlow = appGraph.networkComponent.authorizationFlow,
            platform = appGraph.commonComponent.platform,
            userAgentInfo = appGraph.commonComponent.userAgentInfo,
            resourceProvider = appGraph.commonComponent.resourceProvider,
            urlPathProcessor = appGraph.buildMagicLinksDataComponent().urlPathProcessor,
            currentSubscriptionStateRepository = appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository,
            purchaseInteractor = appGraph.buildPurchaseComponent().purchaseInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}