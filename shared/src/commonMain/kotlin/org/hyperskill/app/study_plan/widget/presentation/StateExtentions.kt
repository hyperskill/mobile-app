package org.hyperskill.app.study_plan.widget.presentation

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection

internal fun StudyPlanWidgetFeature.State.firstVisibleSection(): StudyPlanSection? =
    studyPlanSections.values
        .firstOrNull { it.studyPlanSection.isVisible }
        ?.studyPlanSection

internal fun StudyPlanWidgetFeature.State.getSectionActivities(sectionId: Long): List<LearningActivity> =
    studyPlanSections[sectionId]
        ?.studyPlanSection
        ?.activities
        ?.mapNotNull { id -> activities[id] } ?: emptyList()
