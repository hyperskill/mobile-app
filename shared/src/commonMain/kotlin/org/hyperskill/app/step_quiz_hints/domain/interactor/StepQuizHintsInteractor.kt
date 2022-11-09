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
        /*
        Seen hints JSON example:
        {
            "seenHints": {
                "7364": {
                    "1346367": "seen",
                    "1425492": "helpful"
                },
                "9901": {
                    "708139": "seen"
                }
            }
        }
        * */

        val seenHintsIds = userStorageInteractor
            .getUserStorageValue(key = UserStoragePathBuilder.buildSeenHints(stepId), DataSourceType.REMOTE)
            .getOrNull()?.jsonObject?.keys?.map { it.toLong() }

        return commentsDataInteractor
            .getHintsIDs(stepId)
            .filter { seenHintsIds?.contains(it)?.not() ?: true }
    }

    suspend fun getLastSeenHint(stepId: Long): Comment? {
        val seenHintsIds = userStorageInteractor
            .getUserStorageValue(key = UserStoragePathBuilder.buildSeenHints(stepId), DataSourceType.REMOTE)
            .getOrNull()?.jsonObject?.keys?.map { it.toLong() }

        if (seenHintsIds?.isEmpty() != false) {
            return null
        }
        // first is used because elements from hintsIds
        // in StepQuizHintsFeature are removed like from stack
        val lastHintId = commentsDataInteractor
            .getHintsIDs(stepId)
            .first { seenHintsIds.contains(it) }

        return commentsDataInteractor.getCommentDetails(lastHintId).getOrNull()
    }
}