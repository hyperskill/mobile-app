package org.hyperskill.app.discussions.remote.model

import ru.nobird.app.core.model.mapOfNotNull

class DiscussionsRequest(
    val target: TargetType,
    val targetId: Long,
    val thread: ThreadType,
    val order: OrderType = OrderType.LAST_POSTED,
    val isSpam: Boolean? = null,
    val pageSize: Int = 20,
    val page: Int = 1
) {
    companion object {
        private const val PARAM_TARGET_TYPE = "target_type"
        private const val PARAM_TARGET_ID = "target_id"
        private const val PARAM_THREAD = "thread"
        private const val PARAM_ORDERING = "ordering"
        private const val PARAM_IS_SPAM = "is_spam"
        private const val PARAM_PAGE_SIZE = "page_size"
        private const val PARAM_PAGE = "page"
    }

    val parameters: Map<String, Any> =
        mapOfNotNull(
            PARAM_TARGET_TYPE to target.parameterValue,
            PARAM_TARGET_ID to targetId,
            PARAM_THREAD to thread.parameterValue,
            PARAM_ORDERING to order.parameterValue,
            PARAM_IS_SPAM to isSpam,
            PARAM_PAGE_SIZE to pageSize,
            PARAM_PAGE to page
        )

    enum class TargetType(val parameterValue: String) {
        STEP("step")
    }

    enum class ThreadType(val parameterValue: String) {
        COMMENT("comment"),
        HINT("hint")
    }

    enum class OrderType(val parameterValue: String) {
        LAST_POSTED("time"),
        MOST_POPULAR("popular"),
        BEST_RATED("best"),
    }
}