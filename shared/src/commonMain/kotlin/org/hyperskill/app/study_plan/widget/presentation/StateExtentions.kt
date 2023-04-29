package org.hyperskill.app.study_plan.widget.presentation

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType

/**
 * Returns true if the section is supported in the study plan, when it is visible and its type is supported.
 * @see StudyPlanSectionType
 */
internal val StudyPlanSection.isSupportedInStudyPlan: Boolean
    get() = isVisible && StudyPlanSectionType.supportedTypes().contains(type)

/**
 * Finds first supported section in the study plan.
 *
 * @return first [StudyPlanSection] if it is supported in the study plan, otherwise null.
 * @see StudyPlanSection.isSupportedInStudyPlan
 */
internal fun StudyPlanWidgetFeature.State.getCurrentSection(): StudyPlanSection? =
    studyPlanSections.values
        .firstOrNull { it.studyPlanSection.isSupportedInStudyPlan }
        ?.studyPlanSection

/**
 * Finds current activity in the study plan. If the current section is root topics, then the next activity is returned.
 * Otherwise, the first activity with [LearningActivityState.TODO] state is returned.
 *
 * @return current [LearningActivity].
 */
internal fun StudyPlanWidgetFeature.State.getCurrentActivity(): LearningActivity? =
    getCurrentSection()?.let { section ->
        getSectionActivities(section.id)
            .firstOrNull {
                if (section.type == StudyPlanSectionType.ROOT_TOPICS) {
                    it.id == section.nextActivityId
                } else {
                    it.state == LearningActivityState.TODO
                }
            }
    }

/**
 * @param sectionId target section id.
 * @return list of [LearningActivity] for the given section with [sectionId].
 */
internal fun StudyPlanWidgetFeature.State.getSectionActivities(sectionId: Long): List<LearningActivity> =
    studyPlanSections[sectionId]
        ?.studyPlanSection
        ?.activities
        ?.mapNotNull { id -> activities[id] } ?: emptyList()
