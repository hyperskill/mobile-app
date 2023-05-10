package org.hyperskill.app.study_plan.domain.model

enum class StudyPlanSectionType(val value: String) {
    STAGE("stage"),
    WRAP_UP_PROJECT("wrap up your project"),
    WRAP_UP_TRACK("wrap up your track"),
    EXTRA_TOPICS("extra topics"),
    NEXT_PROJECT("next project"),
    ROOT_TOPICS("root topics"),
    NEXT_TRACK("next track");

    companion object {
        private val VALUES: Array<StudyPlanSectionType> = values()

        fun getByValue(value: String): StudyPlanSectionType? =
            VALUES.firstOrNull { it.value == value }

        fun supportedTypes(): Set<StudyPlanSectionType> =
            setOf(STAGE, EXTRA_TOPICS, ROOT_TOPICS, NEXT_PROJECT)
    }
}