package org.hyperskill.app.main.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.main.presentation.AppFeature
import org.hyperskill.app.notification.click_handling.injection.NotificationClickHandlingComponent
import org.hyperskill.app.streak_recovery.injection.StreakRecoveryComponent
import org.hyperskill.app.welcome_onboarding.injection.WelcomeOnboardingComponent
import ru.nobird.app.presentation.redux.feature.Feature

internal class MainComponentImpl(private val appGraph: AppGraph) : MainComponent {
    private val streakRecoveryComponent: StreakRecoveryComponent =
        appGraph.buildStreakRecoveryComponent()

    private val clickedNotificationComponent: NotificationClickHandlingComponent =
        appGraph.buildClickedNotificationComponent()

    private val welcomeOnboardingComponent: WelcomeOnboardingComponent =
        appGraph.buildWelcomeOnboardingComponent()

    override fun appFeature(
        initialState: AppFeature.State?
    ): Feature<AppFeature.State, AppFeature.Message, AppFeature.Action> =
        AppFeatureBuilder.build(
            initialState,
            appGraph.buildMainDataComponent().appInteractor,
            appGraph.authComponent.authInteractor,
            appGraph.profileDataComponent.currentProfileStateRepository,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.stateRepositoriesComponent,
            streakRecoveryComponent.streakRecoveryReducer,
            streakRecoveryComponent.streakRecoveryActionDispatcher,
            clickedNotificationComponent.notificationClickHandlingReducer,
            clickedNotificationComponent.notificationClickHandlingDispatcher,
            appGraph.buildNotificationComponent().notificationInteractor,
            appGraph.buildPushNotificationsComponent().pushNotificationsInteractor,
            welcomeOnboardingComponent.welcomeOnboardingReducer,
            welcomeOnboardingComponent.welcomeOnboardingActionDispatcher,
            appGraph.commonComponent.platform,
            appGraph.loggerComponent.logger,
            appGraph.commonComponent.buildKonfig.buildVariant
        )

    override fun appFeature(): Feature<AppFeature.State, AppFeature.Message, AppFeature.Action> =
        appFeature(null)
}