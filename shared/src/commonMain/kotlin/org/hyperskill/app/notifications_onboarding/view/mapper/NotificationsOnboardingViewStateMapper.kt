package org.hyperskill.app.notifications_onboarding.view.mapper

import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature

internal object NotificationsOnboardingViewStateMapper {
    fun mapState(state: NotificationsOnboardingFeature.State): NotificationsOnboardingFeature.ViewState =
        NotificationsOnboardingFeature.ViewState(
            formattedDailyStudyRemindersInterval = formatDailyStudyRemindersInterval(
                startHour = state.dailyStudyRemindersStartHour
            )
        )

    fun formatDailyStudyRemindersInterval(startHour: Int): String =
        buildString {
            append(if (startHour < 10) "0" else "")
            append(startHour)
            append(":00")
        }
}