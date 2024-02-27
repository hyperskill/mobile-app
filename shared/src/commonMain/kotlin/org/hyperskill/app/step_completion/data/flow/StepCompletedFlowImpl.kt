package org.hyperskill.app.step_completion.data.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.step_completion.domain.flow.StepCompletedFlow

internal class StepCompletedFlowImpl : StepCompletedFlow {
    private val stepSolvedMutableSharedFlow = MutableSharedFlow<Long>()

    override fun observe(): Flow<Long> =
        stepSolvedMutableSharedFlow

    override suspend fun notifyDataChanged(data: Long) {
        stepSolvedMutableSharedFlow.emit(data)
    }
}