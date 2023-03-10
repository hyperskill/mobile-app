package org.hyperskill.app.notification.injection

import org.hyperskill.app.notification.data.flow.DailyStudyRemindersEnabledFlowImpl
import org.hyperskill.app.notification.domain.flow.DailyStudyRemindersEnabledFlow

class NotificationFlowDataComponentImpl : NotificationFlowDataComponent {
    override val dailyStudyRemindersEnabledFlow: DailyStudyRemindersEnabledFlow =
        DailyStudyRemindersEnabledFlowImpl()
}