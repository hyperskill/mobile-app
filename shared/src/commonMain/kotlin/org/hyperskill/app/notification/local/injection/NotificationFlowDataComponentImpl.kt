package org.hyperskill.app.notification.local.injection

import org.hyperskill.app.notification.local.data.flow.DailyStudyRemindersEnabledFlowImpl
import org.hyperskill.app.notification.local.domain.flow.DailyStudyRemindersEnabledFlow

class NotificationFlowDataComponentImpl : NotificationFlowDataComponent {
    override val dailyStudyRemindersEnabledFlow: DailyStudyRemindersEnabledFlow =
        DailyStudyRemindersEnabledFlowImpl()
}