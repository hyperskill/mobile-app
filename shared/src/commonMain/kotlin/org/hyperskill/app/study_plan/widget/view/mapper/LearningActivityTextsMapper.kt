package org.hyperskill.app.study_plan.widget.view.mapper

import org.hyperskill.app.learning_activities.domain.model.LearningActivity

internal object LearningActivityTextsMapper {
    fun mapLearningActivityToTitle(learningActivity: LearningActivity): String {
        val defaultTitle = learningActivity.title.ifBlank { learningActivity.id.toString() }
        return if (learningActivity.description.isNullOrBlank()) {
            defaultTitle
        } else {
            learningActivity.description
        }
    }

    fun mapLearningActivityToSubtitle(learningActivity: LearningActivity): String? =
        if (learningActivity.description.isNullOrBlank()) {
            null
        } else {
            learningActivity.title.ifBlank { null }
        }

    fun mapLearningActivityToProgressString(learningActivity: LearningActivity): String? =
        learningActivity.progressPercentage.takeIf { it > 0 }?.let { "$it%" }
}