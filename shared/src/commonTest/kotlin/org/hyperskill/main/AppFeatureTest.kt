package org.hyperskill.main

import kotlin.test.Test
import kotlin.test.assertTrue
import org.hyperskill.app.main.presentation.AppFeature
import org.hyperskill.app.main.presentation.AppReducer
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingReducer
import org.hyperskill.app.notification.remote.domain.model.PushNotificationCategory
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData
import org.hyperskill.app.notification.remote.domain.model.PushNotificationType
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryReducer
import org.hyperskill.profile.stub

class AppFeatureTest {
    private val appReducer = AppReducer(
        StreakRecoveryReducer(),
        NotificationClickHandlingReducer()
    )

    @Test
    fun `Streak recovery should be initialized only when user is authorized and already selected track`() {
        val (_, actions) = appReducer.reduce(
            AppFeature.State.Loading,
            AppFeature.Message.UserAccountStatus(
                Profile.stub(isGuest = false, trackId = 1),
                null
            )
        )
        assertTrue {
            actions.any {
                it is AppFeature.Action.StreakRecoveryAction &&
                    it.action is StreakRecoveryFeature.InternalAction.FetchStreak
            }
        }
    }

    @Test
    fun `Streak recovery should NOT be initialized when user is NOT authorized`() {
        val (_, actions) = appReducer.reduce(
            AppFeature.State.Loading,
            AppFeature.Message.UserAccountStatus(Profile.stub(isGuest = true), null)
        )
        assertNoStreakRecoveryActions(actions)
    }

    @Test
    fun `Streak recovery should NOT be initialized in case of push notification handling`() {
        val (_, actions) = appReducer.reduce(
            AppFeature.State.Loading,
            AppFeature.Message.UserAccountStatus(
                Profile.stub(isGuest = true, trackId = 1),
                PushNotificationData(
                    PushNotificationType.STREAK_NEW.name,
                    PushNotificationCategory.CONTINUE_LEARNING.backendName!!
                )
            )
        )
        assertNoStreakRecoveryActions(actions)
    }

    private fun assertNoStreakRecoveryActions(actions: Set<AppFeature.Action>) {
        assertTrue {
            actions.none {
                it is AppFeature.Action.StreakRecoveryAction
            }
        }
    }
}