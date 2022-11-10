package org.hyperskill.app.step_quiz_hints.domain.interactor

import kotlinx.serialization.json.jsonObject
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.repository.CommentsRepository
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.discussions.domain.repository.DiscussionsRepository
import org.hyperskill.app.user_storage.domain.model.UserStoragePathBuilder
import org.hyperskill.app.user_storage.domain.repository.UserStorageRepository

class StepQuizHintsInteractor(
    private val discussionsRepository: DiscussionsRepository,
    private val userStorageRepository: UserStorageRepository,
    private val commentsRepository: CommentsRepository
) {
    suspend fun getNotSeenHintsIds(stepId: Long): List<Long> {
        val hintsIds = getStepHintsIds(stepId)
        if (hintsIds.isEmpty()) {
            return emptyList()
        }

        val seenHintsIds = getSeenHintsIds(stepId).toSet()
        if (seenHintsIds.isEmpty()) {
            return hintsIds
        }

        return hintsIds.filter { !seenHintsIds.contains(it) }
    }

    suspend fun getLastSeenHint(stepId: Long): Comment? {
        val seenHintsIds = getSeenHintsIds(stepId).toSet()
        if (seenHintsIds.isEmpty()) {
            return null
        }

        // first is used because elements from hintsIds
        // in StepQuizHintsFeature are removed like from stack
        val lastHintId = getStepHintsIds(stepId)
            .firstOrNull { seenHintsIds.contains(it) }
            ?: return null

        return commentsRepository.getComment(lastHintId).getOrNull()
    }

    private suspend fun getStepHintsIds(stepId: Long): List<Long> =
        discussionsRepository
            .getStepHintsDiscussions(stepId)
            .getOrNull()
            ?.discussions?.map { it.id }
            ?: emptyList()

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
    private suspend fun getSeenHintsIds(stepId: Long): List<Long> =
        userStorageRepository
            .getUserStorageValue(
                key = UserStoragePathBuilder.buildSeenHints(stepId),
                DataSourceType.REMOTE
            )
            .getOrNull()?.jsonObject?.keys?.map { it.toLong() }
            ?: emptyList()
}
