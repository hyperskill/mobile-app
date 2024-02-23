package org.hyperskill.app.main.presentation

import co.touchlab.kermit.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.injection.StateRepositoriesComponent
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.main.domain.interactor.AppInteractor
import org.hyperskill.app.main.presentation.AppFeature.Action
import org.hyperskill.app.main.presentation.AppFeature.Message
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor
import org.hyperskill.app.notification.remote.domain.interactor.PushNotificationsInteractor
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.model.isNewUser
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumbBuilder
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionStatus
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType
import org.hyperskill.app.subscriptions.domain.model.isValidTillPassed
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.subscriptions.domain.repository.SubscriptionsRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class AppActionDispatcher(
    config: ActionDispatcherOptions,
    private val appInteractor: AppInteractor,
    private val authInteractor: AuthInteractor,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val sentryInteractor: SentryInteractor,
    private val stateRepositoriesComponent: StateRepositoriesComponent,
    private val notificationsInteractor: NotificationInteractor,
    private val pushNotificationsInteractor: PushNotificationsInteractor,
    private val purchaseInteractor: PurchaseInteractor,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val subscriptionsRepository: SubscriptionsRepository,
    private val isSubscriptionPurchaseEnabled: Boolean,
    private val logger: Logger
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    private var refreshMobileOnlySubscriptionJob: Job? = null

    init {
        authInteractor
            .observeUserDeauthorization()
            .onEach {
                when (it.reason) {
                    UserDeauthorized.Reason.TOKEN_REFRESH_FAILURE -> {
                        authInteractor.clearCache()
                    }
                    UserDeauthorized.Reason.SIGN_OUT -> {
                        appInteractor.doCurrentUserSignedOutCleanUp()
                    }
                }

                refreshMobileOnlySubscriptionJob?.cancel()
                refreshMobileOnlySubscriptionJob = null

                stateRepositoriesComponent.resetRepositories()

                sentryInteractor.addBreadcrumb(HyperskillSentryBreadcrumbBuilder.buildAppUserDeauthorized(it.reason))

                onNewMessage(Message.UserDeauthorized(it.reason))
            }
            .launchIn(actionScope)

        if (isSubscriptionPurchaseEnabled) {
            currentSubscriptionStateRepository
                .changes
                .distinctUntilChangedBy {
                    Triple(it.type, it.status, it.validTill)
                }
                .onEach { subscription ->
                    onNewMessage(AppFeature.InternalMessage.SubscriptionChanged(subscription))
                    startSubscriptionRefreshIfNeeded(subscription)
                }
                .launchIn(actionScope)
        }
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchAppStartupConfig ->
                handleFetchAppStartupConfig(action, ::onNewMessage)
            is Action.IdentifyUserInSentry ->
                sentryInteractor.setUsedId(action.userId)
            is Action.ClearUserInSentry ->
                sentryInteractor.clearCurrentUser()
            is Action.UpdateDailyLearningNotificationTime ->
                handleUpdateDailyLearningNotificationTime()
            is Action.SendPushNotificationsToken ->
                pushNotificationsInteractor.renewFCMToken()
            is Action.IdentifyUserInPurchaseSdk ->
                handleIdentifyUserInPurchaseSdk(action.userId)
            is Action.LogAppLaunchFirstTimeAnalyticEventIfNeeded ->
                appInteractor.logAppLaunchFirstTimeAnalyticEventIfNeeded()
            is AppFeature.InternalAction.FetchSubscription ->
                handleFetchSubscription(::onNewMessage)
            else -> {}
        }
    }

    private suspend fun handleFetchAppStartupConfig(
        action: Action.FetchAppStartupConfig,
        onNewMessage: (Message) -> Unit
    ) {
        val isAuthorized = isUserAuthorized()

        sentryInteractor.withTransaction(
            transaction = HyperskillSentryTransactionBuilder.buildAppScreenRemoteDataLoading(isAuthorized),
            onError = { e ->
                sentryInteractor.addBreadcrumb(
                    HyperskillSentryBreadcrumbBuilder.buildAppDetermineUserAccountStatusError(e)
                )
                Message.FetchAppStartupConfigError
            }
        ) {
            coroutineScope {
                sentryInteractor.addBreadcrumb(HyperskillSentryBreadcrumbBuilder.buildAppDetermineUserAccountStatus())

                val profileDeferred = async { fetchProfile(isAuthorized) }
                val subscriptionDeferred = async { fetchSubscription(isAuthorized) }

                sentryInteractor.addBreadcrumb(
                    HyperskillSentryBreadcrumbBuilder.buildAppDetermineUserAccountStatusSuccess()
                )

                Message.FetchAppStartupConfigSuccess(
                    profile = profileDeferred.await().getOrThrow(),
                    subscription = subscriptionDeferred.await(),
                    notificationData = action.pushNotificationData
                )
            }
        }.let(onNewMessage)
    }

    private suspend fun isUserAuthorized(): Boolean =
        authInteractor.isAuthorized().getOrDefault(false)

    // TODO: Move this logic to reducer
    private suspend fun fetchProfile(isAuthorized: Boolean): Result<Profile> =
        if (isAuthorized) {
            currentProfileStateRepository
                .getStateWithSource(forceUpdate = false)
                .fold(
                    onSuccess = { (profile, usedDataSourceType) ->
                        /**
                         * ALTAPPS-693:
                         * If cached user is new, we need to fetch profile from remote to check if track selected
                         */
                        if (profile.isNewUser && usedDataSourceType == DataSourceType.CACHE) {
                            currentProfileStateRepository.getState(forceUpdate = true)
                        } else {
                            Result.success(profile)
                        }
                    },
                    onFailure = { currentProfileStateRepository.getState(forceUpdate = true) }
                )
        } else {
            currentProfileStateRepository.getState(forceUpdate = true)
        }

    private suspend fun fetchSubscription(isAuthorized: Boolean = true): Subscription? =
        if (isAuthorized && isSubscriptionPurchaseEnabled) {
            val subscription = currentSubscriptionStateRepository
                .getStateWithSource(forceUpdate = false)
                .fold(
                    onSuccess = { (subscription, usedDataSourceType) ->
                        // Fetch subscription from remote
                        // if the user has the mobile-only subscription
                        // and its valid time is passed
                        val shouldFetchSubscriptionFromRemote =
                            usedDataSourceType == DataSourceType.CACHE &&
                                subscription.type == SubscriptionType.MOBILE_ONLY &&
                                subscription.isValidTillPassed()
                        if (shouldFetchSubscriptionFromRemote) {
                            currentSubscriptionStateRepository
                                .getState(forceUpdate = true)
                                .getOrNull()
                        } else {
                            subscription
                        }
                    },
                    onFailure = {
                        currentSubscriptionStateRepository
                            .getState(forceUpdate = true)
                            .onFailure { e ->
                                logger.e(e) { "Failed to fetch subscription" }
                            }
                            .getOrNull()
                    }
                )
            subscription?.let(::startSubscriptionRefreshIfNeeded)
            subscription
        } else {
            null
        }

    private suspend fun handleUpdateDailyLearningNotificationTime() {
        notificationsInteractor
            .updateTimeZone()
            .onFailure {
                sentryInteractor.captureErrorMessage(
                    "AppActionDispatcher: failed to update timezone\n$it"
                )
            }
    }

    private suspend fun handleIdentifyUserInPurchaseSdk(userId: Long) {
        if (isSubscriptionPurchaseEnabled) {
            purchaseInteractor
                .login(userId)
                .onFailure {
                    logger.e(it) {
                        "Failed to login user in the purchase sdk"
                    }
                }
        }
    }

    private suspend fun handleFetchSubscription(onNewMessage: (Message) -> Unit) {
        fetchSubscription()?.let {
            startSubscriptionRefreshIfNeeded(it)
            onNewMessage(
                AppFeature.InternalMessage.SubscriptionChanged(it)
            )
        }
    }

    private fun startSubscriptionRefreshIfNeeded(subscription: Subscription) {
        refreshMobileOnlySubscriptionJob?.cancel()
        refreshMobileOnlySubscriptionJob = null

        val isActiveMobileOnlySubscription =
            subscription.type == SubscriptionType.MOBILE_ONLY &&
                subscription.status == SubscriptionStatus.ACTIVE

        if (isActiveMobileOnlySubscription && subscription.validTill != null) {
            refreshMobileOnlySubscriptionJob = actionScope.launch {
                refreshMobileOnlySubscriptionOnExpiration(subscription.validTill, ::onNewMessage)
            }
            refreshMobileOnlySubscriptionJob?.invokeOnCompletion {
                refreshMobileOnlySubscriptionJob = null
            }
        }
    }

    private suspend fun refreshMobileOnlySubscriptionOnExpiration(
        subscriptionValidTill: Instant,
        onNewMessage: (Message) -> Unit
    ) {
        val nowByUTC = Clock.System.now()
            .toLocalDateTime(TimeZone.UTC)
            .toInstant(TimeZone.UTC)
        val delayDuration = subscriptionValidTill - nowByUTC
        logger.d { "Wait ${delayDuration.inWholeSeconds} seconds for subscription expiration to refresh it" }
        delay(delayDuration)
        if (isUserAuthorized()) {
            val freshSubscription = subscriptionsRepository
                .syncSubscription()
                .onFailure { e ->
                    logger.e(e) { "Failed to refresh subscription" }
                }
                .onSuccess {
                    logger.d { "Subscription successfully refreshed" }
                }
                .getOrNull()
            if (freshSubscription != null) {
                onNewMessage(AppFeature.InternalMessage.SubscriptionChanged(freshSubscription))
                currentSubscriptionStateRepository.updateState(freshSubscription)
            }
        }
    }
}