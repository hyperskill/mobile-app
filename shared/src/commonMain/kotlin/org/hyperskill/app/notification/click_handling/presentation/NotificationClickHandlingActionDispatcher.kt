package org.hyperskill.app.notification.click_handling.presentation

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.badges.domain.repository.BadgesRepository
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.Action
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.Message
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class NotificationClickHandlingActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val nextLearningActivityStateRepository: NextLearningActivityStateRepository,
    private val badgesRepository: BadgesRepository,
    private val logger: Logger
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    companion object {
        private const val LOG_TAG = "NotificationClickHandlingDispatcher"
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is NotificationClickHandlingFeature.InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            is NotificationClickHandlingFeature.InternalAction.FetchProfile -> {
                val profile =
                    currentProfileStateRepository
                        .getState()
                        .getOrElse {
                            logger.log(
                                severity = Severity.Error,
                                tag = LOG_TAG,
                                throwable = null,
                                message = "can't fetch profile\n$it"
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
                        logger.log(
                            severity = Severity.Error,
                            tag = LOG_TAG,
                            throwable = null,
                            message = "can't fetch badge\n$it"
                        )
                        onNewMessage(NotificationClickHandlingFeature.EarnedBadgeFetchResult.Error)
                        return
                    }
                onNewMessage(NotificationClickHandlingFeature.EarnedBadgeFetchResult.Success(badge))
            }
            is NotificationClickHandlingFeature.InternalAction.FetchNextLearningActivity -> {
                val activity = nextLearningActivityStateRepository
                    .getState(forceUpdate = true)
                    .getOrElse {
                        logger.log(
                            severity = Severity.Error,
                            tag = LOG_TAG,
                            throwable = null,
                            message = "can't fetch next learning activity\n$it"
                        )
                        onNewMessage(NotificationClickHandlingFeature.NextLearningActivityFetchResult.Error)
                        return
                    }
                onNewMessage(NotificationClickHandlingFeature.NextLearningActivityFetchResult.Success(activity))
            }
            else -> {
                // no op
            }
        }
    }
}