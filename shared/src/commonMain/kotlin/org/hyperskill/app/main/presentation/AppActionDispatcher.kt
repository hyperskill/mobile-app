package org.hyperskill.app.main.presentation

import co.touchlab.kermit.Logger
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumbBuilder
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.subscriptions.domain.interactor.SubscriptionsInteractor
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType
import org.hyperskill.app.subscriptions.domain.model.isExpired
import org.hyperskill.app.subscriptions.domain.model.isValidTillPassed
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class AppActionDispatcher(
    config: ActionDispatcherOptions,
    private val appInteractor: AppInteractor,
    private val authInteractor: AuthInteractor,
    private val sentryInteractor: SentryInteractor,
    private val stateRepositoriesComponent: StateRepositoriesComponent,
    private val notificationsInteractor: NotificationInteractor,
    private val pushNotificationsInteractor: PushNotificationsInteractor,
    private val purchaseInteractor: PurchaseInteractor,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val subscriptionsInteractor: SubscriptionsInteractor,
    private val logger: Logger
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

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

                stateRepositoriesComponent.resetRepositories()

                sentryInteractor.addBreadcrumb(HyperskillSentryBreadcrumbBuilder.buildAppUserDeauthorized(it.reason))

                onNewMessage(Message.UserDeauthorized(it.reason))
            }
            .launchIn(actionScope)

        currentSubscriptionStateRepository
            .changes
            .distinctUntilChanged()
            .onEach { subscription ->
                onNewMessage(AppFeature.InternalMessage.SubscriptionChanged(subscription))
            }
            .launchIn(actionScope)
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
            is AppFeature.InternalAction.RefreshSubscriptionOnExpiration ->
                subscriptionsInteractor.refreshSubscriptionOnExpirationIfNeeded(action.subscription)
            is AppFeature.InternalAction.CancelSubscriptionRefresh ->
                subscriptionsInteractor.cancelSubscriptionRefresh()
            else -> {}
        }
    }

    private suspend fun handleFetchAppStartupConfig(
        action: Action.FetchAppStartupConfig,
        onNewMessage: (Message) -> Unit
    ) {
        val isAuthorized =
            authInteractor.isAuthorized().getOrDefault(false)

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

                val profileDeferred = async { appInteractor.fetchProfile(isAuthorized) }
                val subscriptionDeferred = async { fetchSubscription(isAuthorized) }

                val profile = profileDeferred.await().getOrThrow()
                val subscription = subscriptionDeferred.await()

                sentryInteractor.addBreadcrumb(
                    HyperskillSentryBreadcrumbBuilder.buildAppDetermineUserAccountStatusSuccess()
                )

                Message.FetchAppStartupConfigSuccess(
                    profile = profile,
                    subscription = subscription,
                    notificationData = action.pushNotificationData
                )
            }
        }.let(onNewMessage)
    }

    private suspend fun fetchSubscription(isAuthorized: Boolean = true): Subscription? =
        if (isAuthorized) {
            currentSubscriptionStateRepository
                .getStateWithSource(forceUpdate = false)
                .fold(
                    onSuccess = { (subscription, usedDataSourceType) ->
                        // Fetch subscription from remote
                        // if the user has the mobile-only subscription
                        // and its valid time is passed
                        val shouldFetchSubscriptionFromRemote =
                            usedDataSourceType == DataSourceType.CACHE &&
                                subscription.type == SubscriptionType.MOBILE_ONLY &&
                                (subscription.isExpired || subscription.isValidTillPassed())
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
        purchaseInteractor
            .login(userId)
            .onFailure {
                logger.e(it) {
                    "Failed to login user in the purchase sdk"
                }
            }
    }

    private suspend fun handleFetchSubscription(onNewMessage: (Message) -> Unit) {
        fetchSubscription()?.let {
            onNewMessage(
                AppFeature.InternalMessage.SubscriptionChanged(it)
            )
        }
    }
}