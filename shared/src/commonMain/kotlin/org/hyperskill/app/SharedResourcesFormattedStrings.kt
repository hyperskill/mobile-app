package org.hyperskill.app

import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import org.hyperskill.app.step.domain.model.CommentThread

object SharedResourcesFormattedStrings {
    fun getFormattedStepCommentThreadStatistics(thread: CommentThread, count: Int): StringDesc {
        val threadName = when (thread) {
            CommentThread.COMMENT -> SharedResources.strings.step_comment_thread_type_comment_text.desc().localized()
            CommentThread.SOLUTIONS -> SharedResources.strings.step_comment_thread_type_solutions_text.desc().localized()
            CommentThread.HINT -> SharedResources.strings.step_comment_thread_type_hint_text.desc().localized()
            CommentThread.USEFUL_LINK -> SharedResources.strings.step_comment_thread_type_useful_link_text.desc().localized()
            else -> thread.name
        }

        return StringDesc.ResourceFormatted(
            SharedResources.strings.step_comment_thread_statistics_text,
            threadName,
            count.toString()
        )
    }
}