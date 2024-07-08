package org.hyperskill.app.learning_activities.domain.model

enum class LearningActivityState(val value: Int) {
    TODO(1),
    SKIPPED(2),
    COMPLETED(3);

    companion object {
        fun getByValue(value: Int): LearningActivityState? =
            entries.firstOrNull { it.value == value }
    }
}