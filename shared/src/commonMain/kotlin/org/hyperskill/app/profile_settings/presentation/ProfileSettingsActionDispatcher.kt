package org.hyperskill.app.profile_settings.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.SharedResources.strings
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.domain.platform.Platform
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile_settings.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile_settings.domain.model.FeedbackEmailDataBuilder
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProfileSettingsActionDispatcher(
    config: ActionDispatcherOptions,
    private val profileSettingsInteractor: ProfileSettingsInteractor,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val analyticInteractor: AnalyticInteractor,
    private val authorizationFlow: MutableSharedFlow<UserDeauthorized>,
    private val platform: Platform,
    private val userAgentInfo: UserAgentInfo,
    private val resourceProvider: ResourceProvider,
    private val urlPathProcessor: UrlPathProcessor,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchProfileSettings -> {
                val profileSettings = profileSettingsInteractor.getProfileSettings()
                onNewMessage(
                    Message.ProfileSettingsSuccess(
                        profileSettings,
                        currentSubscriptionStateRepository.getState(forceUpdate = true).getOrNull()
                    )
                )
            }
            is Action.ChangeTheme ->
                profileSettingsInteractor.changeTheme(action.theme)
            is Action.SignOut ->
                authorizationFlow.tryEmit(UserDeauthorized(reason = UserDeauthorized.Reason.SIGN_OUT))
            is Action.PrepareFeedbackEmailData -> {
                val currentProfile = currentProfileStateRepository
                    .getState()
                    .getOrNull()

                val feedbackEmailData = FeedbackEmailDataBuilder.build(
                    supportEmail = resourceProvider.getString(strings.settings_send_feedback_support_email),
                    applicationName = resourceProvider.getString(platform.appNameResource),
                    platform = platform,
                    userId = currentProfile?.id,
                    applicationVersion = userAgentInfo.versionCode
                )

                onNewMessage(Message.FeedbackEmailDataPrepared(feedbackEmailData))
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            is Action.GetMagicLink -> getLink(action.path, ::onNewMessage)
            else -> {
                // no op
            }
        }
    }

    private suspend fun getLink(path: HyperskillUrlPath, onNewMessage: (Message) -> Unit): Unit =
        urlPathProcessor.processUrlPath(path)
            .fold(
                onSuccess = { url ->
                    onNewMessage(Message.GetMagicLinkReceiveSuccess(url))
                },
                onFailure = {
                    onNewMessage(Message.GetMagicLinkReceiveFailure)
                }
            )
}