package org.hyperskill.app.notifications_onboarding.view.mapper

import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature

internal object NotificationsOnboardingViewStateMapper {
    fun mapState(state: NotificationsOnboardingFeature.State): NotificationsOnboardingFeature.ViewState =
        NotificationsOnboardingFeature.ViewState(
            dailyStudyRemindersStartHour = state.dailyStudyRemindersStartHour,
            formattedDailyStudyRemindersInterval = formatDailyStudyRemindersInterval(
                startHour = state.dailyStudyRemindersStartHour
            )
        )

    private fun formatDailyStudyRemindersInterval(startHour: Int): String {
        val endHour = startHour + 1
        return buildString {
            append(if (startHour < 10) "0" else "")
            append(startHour)
            append(":00 - ")
            append(if (endHour < 10) "0" else "")
            append(endHour)
            append(":00")
        }
    }
}