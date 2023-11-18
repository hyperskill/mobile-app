package org.hyperskill.app.challenges.domain.model

enum class ChallengeTargetType(val value: String) {
    DAILY_STEP("dailystep"),
    PROJECT("project"),
    STAGE("stage"),
    STEP("step"),
    TOPIC("topic");

    companion object {
        private val VALUES: Array<ChallengeTargetType> = values()

        fun getByValue(value: String): ChallengeTargetType? =
            VALUES.firstOrNull { it.value == value }
    }
}