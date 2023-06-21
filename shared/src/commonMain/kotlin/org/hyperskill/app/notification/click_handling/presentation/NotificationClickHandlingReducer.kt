package org.hyperskill.app.notification.click_handling.presentation

import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.Action
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.InternalAction
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.Message
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.State
import org.hyperskill.app.notification.remote.domain.analytic.PushNotificationClickedHyperskillAnalyticEvent
import org.hyperskill.app.notification.remote.domain.model.PushNotificationType
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

class NotificationClickHandlingReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            is Message.NotificationClicked -> handleNotificationClicked(state, message)
        }

    private fun handleNotificationClicked(
        state: State,
        message: Message.NotificationClicked
    ): ReducerResult {
        val analyticsAction = InternalAction.LogAnalyticEvent(
            PushNotificationClickedHyperskillAnalyticEvent(message.notificationData)
        )
        val actions = when (message.notificationData.typeEnum) {
            PushNotificationType.STREAK_THREE,
            PushNotificationType.STREAK_WEEK,
            PushNotificationType.STREAK_RECORD_START,
            PushNotificationType.STREAK_RECORD_NEAR,
            PushNotificationType.STREAK_RECORD_COMPLETE,
            PushNotificationType.STREAK_NEW ->
                navigateToTheProblemOfTheDay()

            PushNotificationType.LEARN_TOPIC,
            PushNotificationType.REMIND_SHORT ->
                setOf(Action.ViewAction.NavigateTo.StudyPlan)

            PushNotificationType.REPETITION,
            PushNotificationType.REMIND_MEDIUM ->
                setOf(Action.ViewAction.NavigateTo.TopicRepetition)

            PushNotificationType.STEP_STREAK_FREEZE_TOKEN_USED,
            PushNotificationType.STREAK_FREEZE_ONBOARDING ->
                setOf(Action.ViewAction.NavigateTo.Profile)

            PushNotificationType.UNKNOWN ->
                setOf(Action.ViewAction.NavigateTo.Home)
        }

        return state to (actions + analyticsAction)
    }

    private fun navigateToTheProblemOfTheDay(): Set<Action> {
        TODO("Implement profile fetching and navigation to the step screen")
    }
}