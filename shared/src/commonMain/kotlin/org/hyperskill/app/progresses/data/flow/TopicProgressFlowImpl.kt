package org.hyperskill.app.progress_screen.data.flow

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.hyperskill.app.progress_screen.domain.flow.TopicProgressFlow
import org.hyperskill.app.topics.domain.model.TopicProgress

class TopicProgressFlowImpl : TopicProgressFlow {
    private val topicProgressMutableSharedFlow = MutableSharedFlow<TopicProgress>()

    override fun observe(): SharedFlow<TopicProgress> =
        topicProgressMutableSharedFlow

    override suspend fun notifyDataChanged(data: TopicProgress) {
        topicProgressMutableSharedFlow.emit(data)
    }
}