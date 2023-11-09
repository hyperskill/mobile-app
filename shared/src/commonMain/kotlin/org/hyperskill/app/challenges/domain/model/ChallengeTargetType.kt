package org.hyperskill.app.challenges.domain.model

enum class ChallengeTargetType(val value: Int) {
    TOPIC(2),
    STEP(14),
    PROJECT(29),
    STAGE_ON_PRODUCTION(70),
    STAGE_ON_RELEASE(71);
    // Stage has different mapping on production and release
    // https://vyahhi.myjetbrains.com/youtrack/issue/ALT-9537/Backend-Customisable-challenges#focus=Comments-74-265582.0-0

    companion object {
        private val VALUES: Array<ChallengeTargetType> = values()

        fun getByValue(value: Int): ChallengeTargetType? =
            VALUES.firstOrNull { it.value == value }
    }
}