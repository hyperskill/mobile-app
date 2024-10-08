package org.hyperskill.app.profile_settings.presentation

import co.touchlab.kermit.Logger
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.SharedResources.strings
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.domain.platform.Platform
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.model.isMobileOnlySubscriptionEnabled
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile_settings.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile_settings.domain.model.FeedbackEmailDataBuilder
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class ProfileSettingsActionDispatcher(
    config: ActionDispatcherOptions,
    private val profileSettingsInteractor: ProfileSettingsInteractor,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val authorizationFlow: MutableSharedFlow<UserDeauthorized>,
    private val platform: Platform,
    private val userAgentInfo: UserAgentInfo,
    private val resourceProvider: ResourceProvider,
    private val urlPathProcessor: UrlPathProcessor,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val purchaseInteractor: PurchaseInteractor,
    private val sentryInteractor: SentryInteractor,
    private val logger: Logger
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        currentSubscriptionStateRepository
            .changes
            .onEach { subscription ->
                onNewMessage(Message.OnSubscriptionChanged(subscription))
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchProfileSettings ->
                handleFetchProfileSettings(::onNewMessage)
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
                    applicationName = resourceProvider.getString(strings.app_name),
                    platform = platform,
                    userId = currentProfile?.id,
                    applicationVersion = userAgentInfo.versionCode
                )

                onNewMessage(Message.FeedbackEmailDataPrepared(feedbackEmailData))
            }
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

    private suspend fun handleFetchProfileSettings(onNewMessage: (Message) -> Unit) {
        val message = if (isMobileOnlySubscriptionEnabled()) {
            sentryInteractor.withTransaction(
                HyperskillSentryTransactionBuilder.buildProfileSettingsFeatureFetchSubscription(),
                onError = {
                    Message.ProfileSettingsSuccess(
                        profileSettings = profileSettingsInteractor.getProfileSettings()
                    )
                }
            ) {
                fetchProfileSettingsWithSubscription()
            }
        } else {
            Message.ProfileSettingsSuccess(
                profileSettings = profileSettingsInteractor.getProfileSettings()
            )
        }
        onNewMessage(message)
    }

    private suspend fun fetchProfileSettingsWithSubscription(): Message.ProfileSettingsSuccess =
        coroutineScope {
            val subscriptionDeferred = async {
                currentSubscriptionStateRepository.getState(forceUpdate = true)
            }
            val priceDeferred = async {
                purchaseInteractor
                    .getSubscriptionProducts()
                    .map { subscriptionProducts ->
                        subscriptionProducts.firstOrNull()?.formattedPricePerMonth
                    }
            }
            Message.ProfileSettingsSuccess(
                profileSettings = profileSettingsInteractor.getProfileSettings(),
                subscription = subscriptionDeferred
                    .await()
                    .onFailure {
                        logger.e(it) {
                            "Failed to load subscription"
                        }
                    }
                    .getOrNull(),
                mobileOnlyFormattedPrice = priceDeferred
                    .await()
                    .onFailure {
                        logger.e(it) {
                            "Failed to load subscription price"
                        }
                    }
                    .getOrNull()
            )
        }

    private suspend fun isMobileOnlySubscriptionEnabled(): Boolean =
        currentProfileStateRepository
            .getState()
            .getOrNull()
            ?.features
            ?.isMobileOnlySubscriptionEnabled
            ?: false
}