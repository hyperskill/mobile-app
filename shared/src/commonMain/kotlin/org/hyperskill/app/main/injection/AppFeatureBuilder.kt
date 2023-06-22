package org.hyperskill.app.main.injection

import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.core.injection.StateRepositoriesComponent
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.main.domain.interactor.AppInteractor
import org.hyperskill.app.main.presentation.AppActionDispatcher
import org.hyperskill.app.main.presentation.AppFeature.Action
import org.hyperskill.app.main.presentation.AppFeature.Message
import org.hyperskill.app.main.presentation.AppFeature.State
import org.hyperskill.app.main.presentation.AppReducer
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingDispatcher
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingReducer
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryActionDispatcher
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryReducer
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object AppFeatureBuilder {
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
        notificationClickHandlingDispatcher: NotificationClickHandlingDispatcher
    ): Feature<State, Message, Action> {
        val appReducer = AppReducer(
            streakRecoveryReducer,
            clickedNotificationReducer
        )
        val appActionDispatcher = AppActionDispatcher(
            ActionDispatcherOptions(),
            appInteractor,
            authInteractor,
            currentProfileStateRepository,
            sentryInteractor,
            stateRepositoriesComponent
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
                notificationClickHandlingDispatcher.transform(
                    transformAction = { it.safeCast<Action.ClickedNotificationAction>()?.action },
                    transformMessage = Message::NotificationClickHandlingMessage
                )
            )
    }
}