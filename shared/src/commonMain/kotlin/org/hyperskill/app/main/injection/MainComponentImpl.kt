package org.hyperskill.app.main.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.main.presentation.AppFeature
import org.hyperskill.app.notification.click_handling.injection.NotificationClickHandlingComponent
import org.hyperskill.app.streak_recovery.injection.StreakRecoveryComponent
import org.hyperskill.app.welcome_onboarding.injection.LegacyWelcomeOnboardingComponent
import ru.nobird.app.presentation.redux.feature.Feature

internal class MainComponentImpl(private val appGraph: AppGraph) : MainComponent {
    private val streakRecoveryComponent: StreakRecoveryComponent =
        appGraph.buildStreakRecoveryComponent()

    private val clickedNotificationComponent: NotificationClickHandlingComponent =
        appGraph.buildClickedNotificationComponent()

    private val legacyWelcomeOnboardingComponent: LegacyWelcomeOnboardingComponent =
        appGraph.buildLegacyWelcomeOnboardingComponent()

    /*ktlint-disable*/
    override fun appFeature(
        initialState: AppFeature.State?
    ): Feature<AppFeature.State, AppFeature.Message, AppFeature.Action> =
        AppFeatureBuilder.build(
            initialState = initialState,
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

    override fun appFeature(): Feature<AppFeature.State, AppFeature.Message, AppFeature.Action> =
        appFeature(null)
}