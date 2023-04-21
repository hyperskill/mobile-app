package org.hyperskill.app.learning_activities.domain.model

enum class LearningActivityTargetType(val value: String) {
    STEP("step"),
    STAGE("stage");

    companion object {
        private val VALUES: Array<LearningActivityTargetType> = values()

        fun getByValue(value: String): LearningActivityTargetType? =
            VALUES.firstOrNull { it.value == value }
    }
}