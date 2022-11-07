package org.hyperskill.app.discussions.remote.model

class DiscussionsRequest(
    val target: TargetType,
    val targetId: Long,
    val thread: ThreadType,
    val order: OrderType = OrderType.LAST_POSTED,
    val pageSize: Int = 20,
    val page: Int = 1
) {
    companion object {
        private const val PARAM_TARGET_TYPE = "target_type"
        private const val PARAM_TARGET_ID = "target_id"
        private const val PARAM_THREAD = "thread"
        private const val PARAM_ORDERING = "ordering"
        private const val PARAM_PAGE_SIZE = "page_size"
        private const val PARAM_PAGE = "page"
    }

    val parameters: List<Pair<String, String>> =
        listOf(
            Pair(PARAM_TARGET_TYPE, target.parameterValue),
            Pair(PARAM_TARGET_ID, targetId.toString()),
            Pair(PARAM_THREAD, thread.parameterValue),
            Pair(PARAM_ORDERING, order.parameterValue),
            Pair(PARAM_PAGE_SIZE, pageSize.toString()),
            Pair(PARAM_PAGE, page.toString())
        )

    enum class TargetType(val parameterValue: String) {
        STEP("step")
    }

    enum class ThreadType(val parameterValue: String) {
        COMMENT("comment")
    }

    enum class OrderType(val parameterValue: String) {
        LAST_POSTED("time"),
        MOST_POPULAR("best"),
        BEST_RATED("popular"),
    }
}