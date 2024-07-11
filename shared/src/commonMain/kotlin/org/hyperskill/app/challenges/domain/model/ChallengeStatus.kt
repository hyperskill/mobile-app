package org.hyperskill.app.challenges.domain.model

enum class ChallengeStatus(val value: String) {
    NOT_STARTED("not started"),
    STARTED("started"),
    NOT_COMPLETED("not completed"),
    PARTIAL_COMPLETED("partial completed"),
    COMPLETED("completed");

    companion object {
        fun getByValue(value: String): ChallengeStatus? =
            entries.firstOrNull { it.value == value }
    }
}