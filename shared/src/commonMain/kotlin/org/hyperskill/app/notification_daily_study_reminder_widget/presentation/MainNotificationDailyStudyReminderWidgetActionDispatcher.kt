package org.hyperskill.app.notification_daily_study_reminder_widget.presentation

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.notification_daily_study_reminder_widget.domain.repository.NotificationDailyStudyReminderWidgetRepository
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.Action
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.InternalAction
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.InternalMessage
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.Message
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class MainNotificationDailyStudyReminderWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    private val notificationDailyStudyReminderWidgetRepository: NotificationDailyStudyReminderWidgetRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        currentProfileStateRepository.changes
            .map { it.gamification.passedTopicsCount }
            .distinctUntilChanged()
            .onEach { passedTopicsCount ->
                onNewMessage(
                    InternalMessage.PassedTopicsCountChanged(
                        passedTopicsCount
                    )
                )
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            InternalAction.FetchWidgetData ->
                handleFetchWidgetData(::onNewMessage)
            InternalAction.HideWidget ->
                notificationDailyStudyReminderWidgetRepository.setIsNotificationDailyStudyReminderWidgetHidden(true)
            is InternalAction.LogAnalyticEvent -> TODO()
        }
    }

    private suspend fun handleFetchWidgetData(onNewMessage: (Message) -> Unit) {
        val passedTopicsCount = currentProfileStateRepository
            .getState(forceUpdate = false)
            .map { it.gamification.passedTopicsCount }
            .getOrDefault(defaultValue = 0)

        val isWidgetHidden =
            notificationDailyStudyReminderWidgetRepository.getIsNotificationDailyStudyReminderWidgetHidden()

        onNewMessage(
            InternalMessage.FetchWidgetDataResult(
                passedTopicsCount = passedTopicsCount,
                isWidgetHidden = isWidgetHidden
            )
        )
    }
}