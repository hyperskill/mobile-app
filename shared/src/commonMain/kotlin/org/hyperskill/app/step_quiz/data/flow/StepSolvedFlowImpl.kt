package org.hyperskill.app.step_quiz.data.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.step_quiz.domain.flow.StepSolvedFlow

internal class StepSolvedFlowImpl : StepSolvedFlow {
    private val stepSolvedMutableSharedFlow = MutableSharedFlow<Long>()

    override fun observe(): Flow<Long> =
        stepSolvedMutableSharedFlow

    override suspend fun notifyDataChanged(data: Long) {
        stepSolvedMutableSharedFlow.emit(data)
    }
}