package org.hyperskill.app.learning_activities.domain.model

enum class LearningActivityType(val value: Int) {
    SELECT_PROJECT(0),
    STAGE_PREVIEW(3),
    DAILY_CHALLENGE(7),
    LEARN_TOPIC(10),
    TRACK_MILESTONE(15),
    IMPLEMENT_STAGE(20),
    STAGE_FEEDBACK(21),
    PROJECT_GIVE_FEEDBACK(40),
    PROJECT_REFLECT(50),
    PROJECT_FEEDBACK(51),
    PUBLICATION(60),
    ABOUT_USER(70),
    ABOUT_USER_EXPERIENCE(80),
    CURRICULUM_PERSONALIZATION(90),
    TRACK_REFLECT(95),
    TRACK_GIVE_FEEDBACK(97),
    TRACK_FEEDBACK(98),
    SELECT_TRACK(100),
    JOIN_STUDY_GROUP(110);

    companion object {
        private val VALUES: Array<LearningActivityType> = LearningActivityType.values()

        fun getByValue(value: Int): LearningActivityType? =
            VALUES.firstOrNull { it.value == value }
    }
}