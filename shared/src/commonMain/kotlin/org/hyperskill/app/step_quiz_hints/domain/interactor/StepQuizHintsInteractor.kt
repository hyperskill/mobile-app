package org.hyperskill.app.step_quiz_hints.domain.interactor

import kotlinx.serialization.json.jsonObject
import org.hyperskill.app.comments.domain.interactor.CommentsDataInteractor
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor
import org.hyperskill.app.user_storage.domain.model.UserStoragePathBuilder

class StepQuizHintsInteractor(
    private val commentsDataInteractor: CommentsDataInteractor,
    private val userStorageInteractor: UserStorageInteractor,
) {
    suspend fun getNotSeenHintsIds(stepId: Long): List<Long> {
        val seenHintsIds = getSeenHintsIds(stepId)

        return commentsDataInteractor
            .getHintsIDs(stepId)
            .filter { seenHintsIds?.contains(it)?.not() ?: true }
    }

    suspend fun getLastSeenHint(stepId: Long): Comment? {
        val seenHintsIds = getSeenHintsIds(stepId) ?: return null

        if (seenHintsIds.isEmpty()) {
            return null
        }
        // first is used because elements from hintsIds
        // in StepQuizHintsFeature are removed like from stack
        val lastHintId = commentsDataInteractor
            .getHintsIDs(stepId)
            .first { seenHintsIds.contains(it) }

        return commentsDataInteractor.getCommentDetails(lastHintId).getOrNull()
    }

    /**
     * Returns seen hints IDs from user storage
     *
     * Seen hints user storage JSON example:
     * ```
     * {
     *     "seenHints": {
     *         "7364": {
     *             "1346367": "seen",
     *             "1425492": "helpful"
     *         },
     *         "9901": {
     *             "708139": "seen"
     *         }
     *     }
     * }
     * ```
     * @param stepId step ID to get seen hints IDs for this step
     * @return list of seen hints IDs or null if there is no step ID key in user storage
     */
    private suspend fun getSeenHintsIds(stepId: Long): List<Long>? =
        userStorageInteractor
            .getUserStorageValue(
                key = UserStoragePathBuilder.buildSeenHints(stepId),
                DataSourceType.REMOTE
            )
            .getOrNull()?.jsonObject?.keys?.map { it.toLong() }
}
