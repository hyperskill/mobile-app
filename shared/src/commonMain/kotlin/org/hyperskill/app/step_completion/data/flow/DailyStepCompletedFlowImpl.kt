package org.hyperskill.app.step_completion.data.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.step_completion.domain.flow.DailyStepCompletedFlow

internal class DailyStepCompletedFlowImpl : DailyStepCompletedFlow {
    private val dailyStepCompletedMutableSharedFlow = MutableSharedFlow<Long>()

    override fun observe(): Flow<Long> =
        dailyStepCompletedMutableSharedFlow

    override suspend fun notifyDataChanged(data: Long) {
        dailyStepCompletedMutableSharedFlow.emit(data)
    }
}