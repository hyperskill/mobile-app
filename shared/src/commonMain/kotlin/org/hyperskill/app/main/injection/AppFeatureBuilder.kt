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
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
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
        profileInteractor: ProfileInteractor,
        sentryInteractor: SentryInteractor,
        stateRepositoriesComponent: StateRepositoriesComponent,
        clickedNotificationReducer: NotificationClickHandlingReducer,
        notificationClickHandlingDispatcher: NotificationClickHandlingDispatcher
    ): Feature<State, Message, Action> {
        val appReducer = AppReducer(clickedNotificationReducer)
        val appActionDispatcher = AppActionDispatcher(
            ActionDispatcherOptions(),
            appInteractor,
            authInteractor,
            profileInteractor,
            sentryInteractor,
            stateRepositoriesComponent
        )

        return ReduxFeature(initialState ?: State.Idle, appReducer)
            .wrapWithActionDispatcher(appActionDispatcher)
            .wrapWithActionDispatcher(
                notificationClickHandlingDispatcher.transform(
                    transformAction = { it.safeCast<Action.ClickedNotificationAction>()?.action },
                    transformMessage = Message::ClickedNotificationMessage
                )
            )
    }
}