package org.hyperskill.app.main_legacy.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.injection.StateRepositoriesComponent
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.legacy_welcome_onboarding.presentation.LegacyWelcomeOnboardingActionDispatcher
import org.hyperskill.app.legacy_welcome_onboarding.presentation.LegacyWelcomeOnboardingReducer
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.main.domain.interactor.AppInteractor
import org.hyperskill.app.main_legacy.presentation.LegacyAppActionDispatcher
import org.hyperskill.app.main_legacy.presentation.LegacyAppFeature.Action
import org.hyperskill.app.main_legacy.presentation.LegacyAppFeature.Message
import org.hyperskill.app.main_legacy.presentation.LegacyAppFeature.State
import org.hyperskill.app.main_legacy.presentation.LegacyAppReducer
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingActionDispatcher
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingReducer
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor
import org.hyperskill.app.notification.remote.domain.interactor.PushNotificationsInteractor
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryActionDispatcher
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryReducer
import org.hyperskill.app.subscriptions.domain.interactor.SubscriptionsInteractor
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

@Deprecated("Should be removed in ALTAPPS-1276")
internal object LegacyAppFeatureBuilder {
    private const val LOG_TAG = "AppFeature"

    fun build(
        initialState: State?,
        appInteractor: AppInteractor,
        authInteractor: AuthInteractor,
        sentryInteractor: SentryInteractor,
        stateRepositoriesComponent: StateRepositoriesComponent,
        streakRecoveryReducer: StreakRecoveryReducer,
        streakRecoveryActionDispatcher: StreakRecoveryActionDispatcher,
        clickedNotificationReducer: NotificationClickHandlingReducer,
        notificationClickHandlingActionDispatcher: NotificationClickHandlingActionDispatcher,
        notificationsInteractor: NotificationInteractor,
        pushNotificationsInteractor: PushNotificationsInteractor,
        legacyWelcomeOnboardingReducer: LegacyWelcomeOnboardingReducer,
        legacyWelcomeOnboardingActionDispatcher: LegacyWelcomeOnboardingActionDispatcher,
        purchaseInteractor: PurchaseInteractor,
        currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
        subscriptionsInteractor: SubscriptionsInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<State, Message, Action> {
        val legacyAppReducer = LegacyAppReducer(
            streakRecoveryReducer = streakRecoveryReducer,
            notificationClickHandlingReducer = clickedNotificationReducer,
            legacyWelcomeOnboardingReducer = legacyWelcomeOnboardingReducer
        ).wrapWithLogger(buildVariant, logger, LOG_TAG)

        val legacyAppActionDispatcher = LegacyAppActionDispatcher(
            config = ActionDispatcherOptions(),
            appInteractor = appInteractor,
            authInteractor = authInteractor,
            sentryInteractor = sentryInteractor,
            stateRepositoriesComponent = stateRepositoriesComponent,
            notificationsInteractor = notificationsInteractor,
            pushNotificationsInteractor = pushNotificationsInteractor,
            purchaseInteractor = purchaseInteractor,
            currentSubscriptionStateRepository = currentSubscriptionStateRepository,
            subscriptionsInteractor = subscriptionsInteractor,
            logger.withTag(LOG_TAG)
        )

        return ReduxFeature(initialState ?: State.Idle, legacyAppReducer)
            .wrapWithActionDispatcher(legacyAppActionDispatcher)
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
                legacyWelcomeOnboardingActionDispatcher.transform(
                    transformAction = { it.safeCast<Action.WelcomeOnboardingAction>()?.action },
                    transformMessage = Message::WelcomeOnboardingMessage
                )
            )
    }
}