package org.hyperskill.app.notification.click_handling.presentation

import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.Action
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.InternalAction
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.Message
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.ProfileFetchResult
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.State
import org.hyperskill.app.notification.remote.domain.analytic.PushNotificationClickedHyperskillAnalyticEvent
import org.hyperskill.app.notification.remote.domain.model.PushNotificationType
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

class NotificationClickHandlingReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        state to when (message) {
            is Message.NotificationClicked -> handleNotificationClicked(message)
            is ProfileFetchResult -> handleProfileFetchResult(message)
        }

    private fun handleNotificationClicked(
        message: Message.NotificationClicked
    ): Set<Action> {
        val analyticsAction = InternalAction.LogAnalyticEvent(
            PushNotificationClickedHyperskillAnalyticEvent(message.notificationData)
        )
        if (!message.isUserAuthorized) return setOf(analyticsAction)

        val actions = when (message.notificationData.typeEnum) {
            PushNotificationType.STREAK_THREE,
            PushNotificationType.STREAK_WEEK,
            PushNotificationType.STREAK_RECORD_START,
            PushNotificationType.STREAK_RECORD_NEAR,
            PushNotificationType.STREAK_RECORD_COMPLETE,
            PushNotificationType.STREAK_NEW ->
                setOf(
                    Action.ViewAction.SetLoadingShowed(true),
                    InternalAction.FetchProfile
                )

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

        return actions + analyticsAction
    }

    private fun handleProfileFetchResult(
        message: ProfileFetchResult
    ): Set<Action> =
        setOfNotNull(
            Action.ViewAction.SetLoadingShowed(false),
            when (message) {
                is ProfileFetchResult.Success -> {
                    message.profile.dailyStep?.let { dailyStep ->
                        Action.ViewAction.NavigateTo.StepScreen(
                            StepRoute.LearnDaily(dailyStep)
                        )
                    }
                }
                ProfileFetchResult.Error -> null
            }
        )
}