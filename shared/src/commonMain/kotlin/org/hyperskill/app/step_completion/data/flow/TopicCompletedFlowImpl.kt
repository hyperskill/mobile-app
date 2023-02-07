package org.hyperskill.app.step_completion.data.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.step_completion.domain.flow.TopicCompletedFlow

class TopicCompletedFlowImpl : TopicCompletedFlow {
    private val topicCompletedMutableSharedFlow = MutableSharedFlow<Long>()

    override fun observe(): Flow<Long> =
        topicCompletedMutableSharedFlow

    override suspend fun notifyDataChanged(data: Long) {
        topicCompletedMutableSharedFlow.emit(data)
    }
}