package org.hyperskill.app.notification.click_handling.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.badges.domain.repository.BadgesRepository
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.Action
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.Message
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class NotificationClickHandlingDispatcher(
    scopeConfig: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val badgesRepository: BadgesRepository,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(scopeConfig.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is NotificationClickHandlingFeature.InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.event)
            is NotificationClickHandlingFeature.InternalAction.FetchProfile -> {
                val profile =
                    currentProfileStateRepository
                        .getState()
                        .getOrElse {
                            sentryInteractor.captureErrorMessage(
                                "NotificationClickHandlingDispatcher: can't fetch profile\n$it"
                            )
                            onNewMessage(NotificationClickHandlingFeature.ProfileFetchResult.Error)
                            return
                        }
                onNewMessage(NotificationClickHandlingFeature.ProfileFetchResult.Success(profile))
            }
            is NotificationClickHandlingFeature.InternalAction.FetchEarnedBadge -> {
                val badge = badgesRepository
                    .getBadge(action.badgeId)
                    .getOrElse {
                        sentryInteractor.captureErrorMessage(
                            "NotificationClickHandlingDispatcher: can't fetch badge\n$it"
                        )
                        onNewMessage(NotificationClickHandlingFeature.EarnedBadgeFetchResult.Error)
                        return
                    }

                onNewMessage(NotificationClickHandlingFeature.EarnedBadgeFetchResult.Success(badge))
            }
            else -> {
                // no op
            }
        }
    }
}