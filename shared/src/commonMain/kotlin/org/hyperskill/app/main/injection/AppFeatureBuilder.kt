package org.hyperskill.app.main.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.domain.platform.Platform
import org.hyperskill.app.core.injection.StateRepositoriesComponent
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.main.domain.interactor.AppInteractor
import org.hyperskill.app.main.presentation.AppActionDispatcher
import org.hyperskill.app.main.presentation.AppFeature.Action
import org.hyperskill.app.main.presentation.AppFeature.Message
import org.hyperskill.app.main.presentation.AppFeature.State
import org.hyperskill.app.main.presentation.AppReducer
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingActionDispatcher
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingReducer
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor
import org.hyperskill.app.notification.remote.domain.interactor.PushNotificationsInteractor
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryActionDispatcher
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryReducer
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingActionDispatcher
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingReducer
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object AppFeatureBuilder {
    private const val LOG_TAG = "AppFeature"

    fun build(
        initialState: State?,
        appInteractor: AppInteractor,
        authInteractor: AuthInteractor,
        currentProfileStateRepository: CurrentProfileStateRepository,
        sentryInteractor: SentryInteractor,
        stateRepositoriesComponent: StateRepositoriesComponent,
        streakRecoveryReducer: StreakRecoveryReducer,
        streakRecoveryActionDispatcher: StreakRecoveryActionDispatcher,
        clickedNotificationReducer: NotificationClickHandlingReducer,
        notificationClickHandlingActionDispatcher: NotificationClickHandlingActionDispatcher,
        notificationsInteractor: NotificationInteractor,
        pushNotificationsInteractor: PushNotificationsInteractor,
        welcomeOnboardingReducer: WelcomeOnboardingReducer,
        welcomeOnboardingActionDispatcher: WelcomeOnboardingActionDispatcher,
        purchaseInteractor: PurchaseInteractor,
        currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
        platform: Platform,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<State, Message, Action> {
        val appReducer = AppReducer(
            streakRecoveryReducer = streakRecoveryReducer,
            notificationClickHandlingReducer = clickedNotificationReducer,
            welcomeOnboardingReducer = welcomeOnboardingReducer,
            platformType = platform.platformType
        ).wrapWithLogger(buildVariant, logger, LOG_TAG)

        val appActionDispatcher = AppActionDispatcher(
            config = ActionDispatcherOptions(),
            appInteractor = appInteractor,
            authInteractor = authInteractor,
            currentProfileStateRepository = currentProfileStateRepository,
            sentryInteractor = sentryInteractor,
            stateRepositoriesComponent = stateRepositoriesComponent,
            notificationsInteractor = notificationsInteractor,
            pushNotificationsInteractor = pushNotificationsInteractor,
            purchaseInteractor = purchaseInteractor,
            currentSubscriptionStateRepository = currentSubscriptionStateRepository,
            isSubscriptionPurchaseEnabled = platform.isSubscriptionPurchaseEnabled,
            logger.withTag(LOG_TAG)
        )

        return ReduxFeature(initialState ?: State.Idle, appReducer)
            .wrapWithActionDispatcher(appActionDispatcher)
            .wrapWithActionDispatcher(
                streakRecoveryActionDispatcher.transform(
                    transformAction = { it.safeCast<Action.StreakRecoveryAction>()?.action },
                    transformMessage = Message::StreakRecoveryMessage
                )
            )
            .wrapWithActionDispatcher(
                notificationClickHandlingActionDispatcher.transform(
                    transformAction = { it.safeCast<Action.ClickedNotificationAction>()?.action },
                    transformMessage = Message::NotificationClickHandlingMessage
                )
            )
            .wrapWithActionDispatcher(
                welcomeOnboardingActionDispatcher.transform(
                    transformAction = { it.safeCast<Action.WelcomeOnboardingAction>()?.action },
                    transformMessage = Message::WelcomeOnboardingMessage
                )
            )
    }
}