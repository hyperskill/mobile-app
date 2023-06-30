package org.hyperskill.app.notification.local.data.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.notification.local.domain.flow.DailyStudyRemindersEnabledFlow

class DailyStudyRemindersEnabledFlowImpl : DailyStudyRemindersEnabledFlow {
    private val dailyStudyRemindersEnabledFlow = MutableSharedFlow<Boolean>()

    override fun observe(): Flow<Boolean> =
        dailyStudyRemindersEnabledFlow

    override suspend fun notifyDataChanged(data: Boolean) {
        dailyStudyRemindersEnabledFlow.emit(data)
    }
}