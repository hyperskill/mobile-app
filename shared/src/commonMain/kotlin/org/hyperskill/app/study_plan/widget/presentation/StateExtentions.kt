package org.hyperskill.app.study_plan.widget.presentation

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType
import org.hyperskill.app.subscriptions.domain.model.SubscriptionLimitType
import org.hyperskill.app.subscriptions.domain.model.getSubscriptionLimitType

/**
 * @return current [StudyPlanSection].
 *
 * `studyPlanSections` map preserves the entry iteration order, so we can use the first element as the current section.
 *
 * @see StudyPlanWidgetReducer.handleLearningActivitiesWithSectionsFetchSuccess
 */
internal fun StudyPlanWidgetFeature.State.getCurrentSection(): StudyPlanSection? =
    studyPlanSections.values.firstOrNull()?.studyPlanSection

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

/**
 * Returns unlocked activities ids list in case of MobileContentTrial subscription.
 * Otherwise returns null (all the activities are unlocked).
 */
internal fun StudyPlanWidgetFeature.State.getUnlockedActivitiesIds(sectionId: Long): List<Long>? {
    val section = studyPlanSections[sectionId]?.studyPlanSection ?: return null
    val isRootTopicsSection = section.type == StudyPlanSectionType.ROOT_TOPICS
    val isTopicsLimitEnabled =
        subscription?.getSubscriptionLimitType(
            isMobileContentTrialEnabled = isMobileContentTrialEnabled,
            canMakePayments = canMakePayments
        ) == SubscriptionLimitType.TOPICS
    val unlockedActivitiesCount = profile?.feautureValues?.mobileContentTrialFreeTopics?.minus(learnedTopicsCount)
    return if (isRootTopicsSection && isTopicsLimitEnabled && unlockedActivitiesCount != null) {
        section.activities.take(unlockedActivitiesCount)
    } else {
        null
    }
}

internal fun StudyPlanWidgetFeature.State.isPaywallShown(): Boolean {
    val rootTopicsSections =
        studyPlanSections
            .values
            .filter { sectionInfo ->
                sectionInfo.studyPlanSection.type == StudyPlanSectionType.ROOT_TOPICS
            }
    val hasOnlyOneRootTopicSection = rootTopicsSections.count() == 1
    return if (hasOnlyOneRootTopicSection) {
        val rootTopicsSectionId = rootTopicsSections.first().studyPlanSection.id
        val unlockedActivitiesIds = getUnlockedActivitiesIds(rootTopicsSectionId)
        unlockedActivitiesIds?.isEmpty() == true
    } else {
        false
    }
}