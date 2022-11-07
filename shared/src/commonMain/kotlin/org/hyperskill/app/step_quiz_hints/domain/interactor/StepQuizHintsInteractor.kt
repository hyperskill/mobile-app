package org.hyperskill.app.step_quiz_hints.domain.interactor

import kotlinx.serialization.json.jsonObject
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.discussions.domain.interactor.DiscussionsInteractor
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor
import org.hyperskill.app.user_storage.domain.model.UserStoragePathBuilder

class StepQuizHintsInteractor(
    private val discussionsInteractor: DiscussionsInteractor,
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

        val hintsIds = discussionsInteractor
            .getStepHints(stepId)
            .map { it.discussions }
            .getOrDefault(emptyList())
            .map { it.id }

        return hintsIds.filter { seenHintsIds?.contains(it) ?: true }
    }
}