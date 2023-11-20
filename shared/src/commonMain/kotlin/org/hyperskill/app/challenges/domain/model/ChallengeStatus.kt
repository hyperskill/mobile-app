package org.hyperskill.app.challenges.domain.model

enum class ChallengeStatus(val value: String) {
    NOT_STARTED("not started"),
    STARTED("started"),
    NOT_COMPLETED("not completed"),
    PARTIAL_COMPLETED("partial completed"),
    COMPLETED("completed");

    companion object {
        private val VALUES: Array<ChallengeStatus> = values()

        fun getByValue(value: String): ChallengeStatus? =
            VALUES.firstOrNull { it.value == value }
    }
}