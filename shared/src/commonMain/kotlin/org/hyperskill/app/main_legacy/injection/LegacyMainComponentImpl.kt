package org.hyperskill.app.main_legacy.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.legacy_welcome_onboarding.injection.LegacyWelcomeOnboardingComponent
import org.hyperskill.app.main_legacy.presentation.LegacyAppFeature.Action
import org.hyperskill.app.main_legacy.presentation.LegacyAppFeature.Message
import org.hyperskill.app.main_legacy.presentation.LegacyAppFeature.State
import org.hyperskill.app.notification.click_handling.injection.NotificationClickHandlingComponent
import org.hyperskill.app.streak_recovery.injection.StreakRecoveryComponent
import ru.nobird.app.presentation.redux.feature.Feature

@Deprecated("Should be removed in ALTAPPS-1276")
internal class LegacyMainComponentImpl(private val appGraph: AppGraph) : LegacyMainComponent {
    private val streakRecoveryComponent: StreakRecoveryComponent =
        appGraph.buildStreakRecoveryComponent()

    private val clickedNotificationComponent: NotificationClickHandlingComponent =
        appGraph.buildClickedNotificationComponent()

    private val legacyWelcomeOnboardingComponent: LegacyWelcomeOnboardingComponent =
        appGraph.buildLegacyWelcomeOnboardingComponent()


    /*ktlint-disable*/
    @Suppress("MaxLineLength")
    override fun legacyAppFeature(): Feature<State, Message, Action> =
        LegacyAppFeatureBuilder.build(
            initialState = null,
            appInteractor = appGraph.buildMainDataComponent().appInteractor,
            authInteractor = appGraph.authComponent.authInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            stateRepositoriesComponent = appGraph.stateRepositoriesComponent,
            streakRecoveryReducer = streakRecoveryComponent.streakRecoveryReducer,
            streakRecoveryActionDispatcher = streakRecoveryComponent.streakRecoveryActionDispatcher,
            clickedNotificationReducer = clickedNotificationComponent.notificationClickHandlingReducer,
            notificationClickHandlingActionDispatcher = clickedNotificationComponent.notificationClickHandlingActionDispatcher,
            notificationsInteractor = appGraph.buildNotificationComponent().notificationInteractor,
            pushNotificationsInteractor = appGraph.buildPushNotificationsComponent().pushNotificationsInteractor,
            legacyWelcomeOnboardingReducer = legacyWelcomeOnboardingComponent.legacyWelcomeOnboardingReducer,
            legacyWelcomeOnboardingActionDispatcher = legacyWelcomeOnboardingComponent.legacyWelcomeOnboardingActionDispatcher,
            purchaseInteractor = appGraph.buildPurchaseComponent().purchaseInteractor,
            currentSubscriptionStateRepository = appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository,
            subscriptionsInteractor = appGraph.subscriptionDataComponent.subscriptionsInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}