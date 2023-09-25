package org.hyperskill.notification

import kotlin.test.Test
import kotlin.test.assertTrue
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingReducer
import org.hyperskill.app.notification.remote.domain.model.PushNotificationCategory
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData
import org.hyperskill.app.notification.remote.domain.model.PushNotificationType

class NotificationClickHandlingFeatureTest {

    private val notificationClickHandlingReducer = NotificationClickHandlingReducer()

    @Test
    fun `Navigation should NOT be available in case the user is not authorized`() {
        val (_, actions) = notificationClickHandlingReducer.reduce(
            NotificationClickHandlingFeature.State,
            NotificationClickHandlingFeature.Message.NotificationClicked(
                PushNotificationData(
                    typeString = PushNotificationType.STREAK_NEW.name,
                    categoryString = PushNotificationCategory.CONTINUE_LEARNING.backendName!!
                ),
                isUserAuthorized = false,
                notificationLaunchedApp = true
            )
        )
        assertTrue {
            actions.all {
                it is NotificationClickHandlingFeature.InternalAction.LogAnalyticEvent
            }
        }
    }

    @Test
    fun `Navigation should be available only in case the user is authorized`() {
        val (_, actions) = notificationClickHandlingReducer.reduce(
            NotificationClickHandlingFeature.State,
            NotificationClickHandlingFeature.Message.NotificationClicked(
                PushNotificationData(
                    typeString = PushNotificationType.STREAK_NEW.name,
                    categoryString = PushNotificationCategory.CONTINUE_LEARNING.backendName!!
                ),
                isUserAuthorized = true,
                notificationLaunchedApp = true
            )
        )
        assertTrue {
            actions.any {
                it is NotificationClickHandlingFeature.InternalAction.LogAnalyticEvent
            }
            actions.any {
                it is NotificationClickHandlingFeature.Action.ViewAction
            }
        }
    }
}