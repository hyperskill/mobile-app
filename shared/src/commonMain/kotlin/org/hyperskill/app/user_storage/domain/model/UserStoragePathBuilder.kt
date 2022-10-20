package org.hyperskill.app.user_storage.domain.model

object UserStoragePathBuilder {
    fun buildSeenHints(stepId: Long): String =
        "${UserStoragePathKeyValues.SEEN_HINTS}.$stepId"

    fun buildSeenHint(stepId: Long, hintId: Long): String =
        "${buildSeenHints(stepId)}.$hintId"
}