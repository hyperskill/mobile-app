package org.hyperskill.app.notification.local.injection

import org.hyperskill.app.notification.local.domain.flow.DailyStudyRemindersEnabledFlow

interface NotificationFlowDataComponent {
    val dailyStudyRemindersEnabledFlow: DailyStudyRemindersEnabledFlow
}