package org.hyperskill.app.study_plan.widget.presentation

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType
import org.hyperskill.app.study_plan.domain.model.isRootTopicsSection

internal fun StudyPlanWidgetFeature.State.firstSection(): StudyPlanSection? =
    studyPlanSections.values
        .firstOrNull { it.studyPlanSection.isSupportedInStudyPlan }
        ?.studyPlanSection

internal fun StudyPlanWidgetFeature.State.getSectionActivities(sectionId: Long): List<LearningActivity> =
    studyPlanSections[sectionId]
        ?.studyPlanSection
        ?.activities
        ?.mapNotNull { id -> activities[id] } ?: emptyList()

internal val StudyPlanSection.isSupportedInStudyPlan: Boolean
    get() = isVisible && StudyPlanSectionType.supportedTypes().contains(type)

internal fun StudyPlanWidgetFeature.State.getNextActivityId(section: StudyPlanSection): Long? {
    if (firstSection()?.id != section.id) return null

    return getSectionActivities(section.id)
        .firstOrNull {
            if (section.isRootTopicsSection()) {
                it.id == section.nextActivityId
            } else {
                it.state == LearningActivityState.TODO
            }
        }?.id
}