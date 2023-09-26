package org.hyperskill.app.profile_settings.injection

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.domain.platform.Platform
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile_settings.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsActionDispatcher
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.State
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object ProfileSettingsFeatureBuilder {
    private const val LOG_TAG = "ProfileSettingsFeature"
    fun build(
        profileSettingsInteractor: ProfileSettingsInteractor,
        currentProfileStateRepository: CurrentProfileStateRepository,
        analyticInteractor: AnalyticInteractor,
        authorizationFlow: MutableSharedFlow<UserDeauthorized>,
        platform: Platform,
        userAgentInfo: UserAgentInfo,
        resourceProvider: ResourceProvider,
        urlPathProcessor: UrlPathProcessor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<State, Message, Action> {
        val profileSettingsReducer = ProfileSettingsReducer().wrapWithLogger(buildVariant, logger, LOG_TAG)
        val profileSettingsActionDispatcher = ProfileSettingsActionDispatcher(
            ActionDispatcherOptions(),
            profileSettingsInteractor,
            currentProfileStateRepository,
            analyticInteractor,
            authorizationFlow,
            platform,
            userAgentInfo,
            resourceProvider,
            urlPathProcessor
        )

        return ReduxFeature(State.Idle, profileSettingsReducer)
            .wrapWithActionDispatcher(profileSettingsActionDispatcher)
    }
}