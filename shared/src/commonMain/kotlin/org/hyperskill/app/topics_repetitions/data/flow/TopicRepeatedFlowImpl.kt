package org.hyperskill.app.topics_repetitions.data.flow

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.hyperskill.app.topics_repetitions.domain.flow.TopicRepeatedFlow

class TopicRepeatedFlowImpl : TopicRepeatedFlow {
    private val topicRepeatedMutableSharedFlow = MutableSharedFlow<Long>()

    override fun observe(): SharedFlow<Long> =
        topicRepeatedMutableSharedFlow

    override suspend fun notifyDataChanged(data: Long) {
        topicRepeatedMutableSharedFlow.emit(data)
    }
}