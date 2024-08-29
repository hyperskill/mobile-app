package org.hyperskill.app.study_plan.widget.presentation

import kotlin.math.max
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType
import org.hyperskill.app.subscriptions.domain.model.SubscriptionLimitType
import ru.nobird.app.core.model.slice

/**
 * @return current [StudyPlanSection].
 *
 * @see StudyPlanWidgetReducer.handleLearningActivitiesWithSectionsFetchSuccess
 */
internal fun StudyPlanWidgetFeature.State.getCurrentSection(): StudyPlanSection? =
    /**
     * [StudyPlanWidgetFeature.State.studyPlanSections] map preserves the entry iteration order,
     * so we can use the first element as the current section.
     */
    studyPlanSections.values.firstOrNull()?.studyPlanSection

/**
 * Finds current activity in the study plan. If the current section is root topics, then the next activity is returned.
 * Otherwise, the first activity with [LearningActivityState.TODO] state is returned.
 *
 * @return current [LearningActivity].
 */
internal fun StudyPlanWidgetFeature.State.getCurrentActivity(): LearningActivity? =
    getCurrentSection()?.let { section ->
        getLoadedSectionActivities(section.id)
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
 * @return A sequence of [LearningActivity] for the given section with [sectionId]
 * filtered by availability in [StudyPlanWidgetFeature.State.activities]
 */
internal fun StudyPlanWidgetFeature.State.getLoadedSectionActivities(sectionId: Long): Sequence<LearningActivity> =
    studyPlanSections[sectionId]
        ?.studyPlanSection
        ?.activities
        ?.asSequence()
        ?.mapNotNull { id -> activities[id] }
        ?: emptySequence()

/**
 * @param sectionId target section id
 * @return A list of activities to be loaded for ROOT_TOPICS section.
 * Activities to be loaded are activities
 * starting from next to the last loaded activity for ROOT_TOPICS section
 * and ending with the last ROOT_TOPICS section activity.
 */
internal fun StudyPlanWidgetFeature.State.getNextRootTopicsActivitiesToBeLoaded(sectionId: Long): List<Long> {
    val sectionInfo = studyPlanSections[sectionId] ?: return emptyList()
    val studyPlanSection = sectionInfo.studyPlanSection
    return if (studyPlanSection.type == StudyPlanSectionType.ROOT_TOPICS) {
        val lastLoadedActivityId = getLoadedSectionActivities(sectionId).lastOrNull()?.id
        val lastLoadedActivityIndex = if (lastLoadedActivityId != null) {
            max(0, studyPlanSection.activities.indexOf(lastLoadedActivityId) + 1)
        } else {
            0
        }
        studyPlanSection.activities.slice(from = lastLoadedActivityIndex)
    } else {
        emptyList()
    }
}

/**
 * @param sectionId target section id
 * @return A list of activities to be loaded before first loaded activity for current section.
 * If section with [sectionId] is not current, then returns empty list.
 */
internal fun StudyPlanWidgetFeature.State.getActivitiesBeforeCurrentActivityToBeLoaded(sectionId: Long): List<Long> {
    val currentSection = getCurrentSection()
    return if (currentSection?.id == sectionId) {
        val sectionInfo = studyPlanSections[sectionId] ?: return emptyList()
        val studyPlanSection = sectionInfo.studyPlanSection
        val firstLoadedActivityId = getLoadedSectionActivities(sectionId).firstOrNull()?.id
        val firstLoadedActivityIndex = if (firstLoadedActivityId != null) {
            max(0, studyPlanSection.activities.indexOf(firstLoadedActivityId))
        } else {
            0
        }
        studyPlanSection.activities.slice(from = 0, to = firstLoadedActivityIndex)
    } else {
        emptyList()
    }
}

/**
 * @param sectionId target section id.
 * @param activityId target activity id in the section with id = [sectionId]
 *
 * @return true in case of MobileContentTrial subscription and
 * this activity number is bigger then the free topics count.
 * Otherwise returns false (activity with [activityId] is unlocked).
 */
internal fun StudyPlanWidgetFeature.State.isActivityLocked(
    sectionId: Long,
    activityId: Long,
): Boolean {
    val unlockedActivitiesCount = getUnlockedActivitiesCount(sectionId)
    return unlockedActivitiesCount != null &&
        getLoadedSectionActivities(sectionId)
            .take(unlockedActivitiesCount)
            .map { it.id }
            .contains(activityId)
            .not()
}

/**
 * @param sectionId target sectionId.
 *
 * @return unlocked activities count for [sectionId] in case of MobileContentTrial subscription.
 * Otherwise returns null (all the activities are unlocked).
 */
internal fun StudyPlanWidgetFeature.State.getUnlockedActivitiesCount(sectionId: Long): Int? {
    val section = studyPlanSections[sectionId]?.studyPlanSection ?: return null
    val isRootTopicsSection = section.type == StudyPlanSectionType.ROOT_TOPICS
    val isTopicsLimitEnabled = subscriptionLimitType == SubscriptionLimitType.TOPICS
    val unlockedActivitiesCount = profile?.featureValues?.mobileContentTrialFreeTopics?.minus(learnedTopicsCount)
    return if (isRootTopicsSection && isTopicsLimitEnabled && unlockedActivitiesCount != null) {
        unlockedActivitiesCount
    } else {
        null
    }
}

/**
 * @return true in case of MobileContentTrial subscription,
 * only on root topics section and
 * all the free topics are solved.
 * Otherwise returns false.
 */
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
        val unlockedActivitiesCount = getUnlockedActivitiesCount(rootTopicsSectionId)
        unlockedActivitiesCount == 0
    } else {
        false
    }
}