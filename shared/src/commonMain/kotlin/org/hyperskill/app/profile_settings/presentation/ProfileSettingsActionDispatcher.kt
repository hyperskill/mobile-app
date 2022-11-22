package org.hyperskill.app.profile_settings.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.core.domain.Platform
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile_settings.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile_settings.domain.model.FeedbackEmailDataBuilder
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProfileSettingsActionDispatcher(
    config: ActionDispatcherOptions,
    private val profileSettingsInteractor: ProfileSettingsInteractor,
    private val profileInteractor: ProfileInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val authorizationFlow: MutableSharedFlow<UserDeauthorized>,
    private val platform: Platform,
    private val userAgentInfo: UserAgentInfo,
    private val urlPathProcessor: UrlPathProcessor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchProfileSettings -> {
                val profileSettings = profileSettingsInteractor.getProfileSettings()
                onNewMessage(Message.ProfileSettingsSuccess(profileSettings))
            }
            is Action.ChangeTheme ->
                profileSettingsInteractor.changeTheme(action.theme)
            is Action.SignOut ->
                authorizationFlow.tryEmit(UserDeauthorized(reason = UserDeauthorized.Reason.SIGN_OUT))
            is Action.PrepareFeedbackEmailData -> {
                val currentProfile = profileInteractor
                    .getCurrentProfile()
                    .getOrNull()

                val feedbackEmailData = FeedbackEmailDataBuilder.build(
                    applicationName = BuildKonfig.APP_NAME,
                    platform = platform,
                    userId = currentProfile?.id,
                    applicationVersion = userAgentInfo.versionCode
                )

                onNewMessage(Message.FeedbackEmailDataPrepared(feedbackEmailData))
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            is Action.GetLink -> getLink(action.path, ::onNewMessage)
        }
    }

    private suspend fun getLink(path: HyperskillUrlPath, onNewMessage: (Message) -> Unit): Unit =
        urlPathProcessor.processUrlPath(path)
            .fold(
                onSuccess = { url ->
                    onNewMessage(Message.LinkReceived(url))
                },
                onFailure = {
                    onNewMessage(Message.LinkReceiveFailed)
                }
            )
}