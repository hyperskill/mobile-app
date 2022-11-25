package org.hyperskill.app.profile_settings.injection

import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.core.domain.Platform
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
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
    fun build(
        profileSettingsInteractor: ProfileSettingsInteractor,
        profileInteractor: ProfileInteractor,
        analyticInteractor: AnalyticInteractor,
        authorizationFlow: MutableSharedFlow<UserDeauthorized>,
        platform: Platform,
        userAgentInfo: UserAgentInfo,
        urlPathProcessor: UrlPathProcessor
    ): Feature<State, Message, Action> {
        val profileSettingsReducer = ProfileSettingsReducer()
        val profileSettingsActionDispatcher = ProfileSettingsActionDispatcher(
            ActionDispatcherOptions(),
            profileSettingsInteractor,
            profileInteractor,
            analyticInteractor,
            authorizationFlow,
            platform,
            userAgentInfo,
            urlPathProcessor
        )

        return ReduxFeature(State.Idle, profileSettingsReducer)
            .wrapWithActionDispatcher(profileSettingsActionDispatcher)
    }
}