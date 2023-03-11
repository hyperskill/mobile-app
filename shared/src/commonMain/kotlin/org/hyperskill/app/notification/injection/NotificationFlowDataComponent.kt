package org.hyperskill.app.notification.injection

import org.hyperskill.app.notification.domain.flow.DailyStudyRemindersEnabledFlow

interface NotificationFlowDataComponent {
    val dailyStudyRemindersEnabledFlow: DailyStudyRemindersEnabledFlow
}