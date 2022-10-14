package org.hyperskill.app.step_quiz_hints.domain.interactor

import kotlinx.serialization.json.jsonObject
import org.hyperskill.app.comments.domain.interactor.CommentsDataInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor

class StepQuizHintsInteractor(
    private val commentsDataInteractor: CommentsDataInteractor,
    private val userStorageInteractor: UserStorageInteractor,
) {
    suspend fun getNotSeenHintsIDs(stepID: Long): List<Long> {
        val seenHints = userStorageInteractor
            .getUserStorageValue(key = "seenHints.$stepID", DataSourceType.REMOTE)
            .getOrNull()?.jsonObject?.keys?.map { it.toLong() }

        return commentsDataInteractor.getHintsIDs(stepID).filter { seenHints?.contains(it) ?: true }
    }
}