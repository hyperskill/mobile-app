package org.hyperskill.app.main.presentation

import co.touchlab.kermit.Logger
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
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.model.isNewUser
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumbBuilder
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
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
    private val isPaywallFeatureEnabled: Boolean,
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

        if (isPaywallFeatureEnabled) {
            currentSubscriptionStateRepository
                .changes
                .onEach { subscription ->
                    onNewMessage(AppFeature.InternalMessage.SubscriptionTypeChanged(subscription.type))
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
            else -> {}
        }
    }

    private suspend fun handleFetchAppStartupConfig(
        action: Action.FetchAppStartupConfig,
        onNewMessage: (Message) -> Unit
    ) {
        val isAuthorized = authInteractor.isAuthorized()
            .getOrDefault(false)

        sentryInteractor.withTransaction(
            transaction = HyperskillSentryTransactionBuilder.buildAppScreenRemoteDataLoading(isAuthorized),
            onError = { e ->
                sentryInteractor.addBreadcrumb(
                    HyperskillSentryBreadcrumbBuilder.buildAppDetermineUserAccountStatusError(e)
                )
                Message.FetchAppStartupConfigError
            }
        ) {
            sentryInteractor.addBreadcrumb(HyperskillSentryBreadcrumbBuilder.buildAppDetermineUserAccountStatus())

            val profile = fetchProfile(isAuthorized).getOrThrow()
            val subscription =
                if (isPaywallFeatureEnabled) {
                    currentSubscriptionStateRepository.getState(forceUpdate = false).getOrNull()
                } else {
                    null
                }

            sentryInteractor.addBreadcrumb(
                HyperskillSentryBreadcrumbBuilder.buildAppDetermineUserAccountStatusSuccess()
            )

            Message.FetchAppStartupConfigSuccess(
                profile = profile,
                subscription = subscription,
                notificationData = action.pushNotificationData
            )
        }.let(onNewMessage)
    }

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
}