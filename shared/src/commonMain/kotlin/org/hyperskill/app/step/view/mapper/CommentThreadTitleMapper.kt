package org.hyperskill.app.step.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.step.domain.model.CommentThread

class CommentThreadTitleMapper(
    private val resourceProvider: ResourceProvider
) {
    fun getFormattedStepCommentThreadStatistics(thread: CommentThread?, count: Int): String {
        val threadName = when (thread) {
            CommentThread.COMMENT ->
                resourceProvider.getString(SharedResources.strings.step_comment_thread_type_comment_text)
            CommentThread.SOLUTIONS ->
                resourceProvider.getString(SharedResources.strings.step_comment_thread_type_solutions_text)
            CommentThread.HINT ->
                resourceProvider.getString(SharedResources.strings.step_comment_thread_type_hint_text)
            CommentThread.USEFUL_LINK ->
                resourceProvider.getString(SharedResources.strings.step_comment_thread_type_useful_link_text)
            else -> thread?.name ?: ""
        }

        return resourceProvider.getString(
            SharedResources.strings.step_comment_thread_statistics_text,
            threadName,
            count.toString()
        )
    }
}