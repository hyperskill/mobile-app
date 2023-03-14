package org.hyperskill.app.notification.data.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.notification.domain.flow.DailyStudyRemindersEnabledFlow

class DailyStudyRemindersEnabledFlowImpl : DailyStudyRemindersEnabledFlow {
    private val dailyStudyRemindersEnabledFlow = MutableSharedFlow<Boolean>()

    override fun observe(): Flow<Boolean> =
        dailyStudyRemindersEnabledFlow

    override suspend fun notifyDataChanged(data: Boolean) {
        dailyStudyRemindersEnabledFlow.emit(data)
    }
}