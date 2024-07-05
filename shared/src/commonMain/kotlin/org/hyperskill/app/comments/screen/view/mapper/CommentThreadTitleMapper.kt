package org.hyperskill.app.comments.screen.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.comments.domain.model.CommentThread
import org.hyperskill.app.core.view.mapper.ResourceProvider

class CommentThreadTitleMapper(
    private val resourceProvider: ResourceProvider
) {
    fun getFormattedCommentThreadStatistics(thread: CommentThread, count: Int): String {
        val threadName = when (thread) {
            CommentThread.COMMENT ->
                resourceProvider.getString(SharedResources.strings.comment_thread_type_comment_text)
            CommentThread.SOLUTIONS ->
                resourceProvider.getString(SharedResources.strings.comment_thread_type_solutions_text)
            CommentThread.HINT ->
                resourceProvider.getString(SharedResources.strings.comment_thread_type_hint_text)
            CommentThread.USEFUL_LINK ->
                resourceProvider.getString(SharedResources.strings.comment_thread_type_useful_link_text)
            CommentThread.UNKNOWN -> ""
        }

        return resourceProvider.getString(
            SharedResources.strings.comment_thread_statistics_text,
            threadName,
            count.toString()
        )
    }
}