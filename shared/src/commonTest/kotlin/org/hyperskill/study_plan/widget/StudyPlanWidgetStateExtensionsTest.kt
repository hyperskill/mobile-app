package org.hyperskill.study_plan.widget

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.profile.domain.model.FeatureKeys
import org.hyperskill.app.profile.domain.model.FeatureValues
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.getCurrentActivity
import org.hyperskill.app.study_plan.widget.presentation.getCurrentSection
import org.hyperskill.app.study_plan.widget.presentation.getLoadedSectionActivities
import org.hyperskill.app.study_plan.widget.presentation.getRootTopicsActivitiesToBeLoaded
import org.hyperskill.app.study_plan.widget.presentation.getUnlockedActivitiesCount
import org.hyperskill.app.study_plan.widget.presentation.isActivityLocked
import org.hyperskill.app.study_plan.widget.presentation.isPaywallShown
import org.hyperskill.app.subscriptions.domain.model.SubscriptionLimitType
import org.hyperskill.learning_activities.domain.model.stub
import org.hyperskill.profile.stub
import org.hyperskill.study_plan.domain.model.stub

class StudyPlanWidgetStateExtensionsTest {
    @Test
    fun `getCurrentSection should return the first section`() {
        val section1 = StudyPlanSection.stub(id = 1)
        val section2 = StudyPlanSection.stub(id = 2)
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                section1.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    section1,
                    true,
                    StudyPlanWidgetFeature.SectionContentStatus.IDLE
                ),
                section2.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    section2,
                    true,
                    StudyPlanWidgetFeature.SectionContentStatus.IDLE
                )
            )
        )

        val currentSection = state.getCurrentSection()
        assertEquals(section1, currentSection)
    }

    @Test
    fun `getCurrentActivity should return the next activity for ROOT_TOPICS section`() {
        val nextActivityId = 1L
        val section = StudyPlanSection.stub(
            id = 1,
            type = StudyPlanSectionType.ROOT_TOPICS,
            nextActivityId = nextActivityId,
            activities = listOf(nextActivityId)
        )
        val activity = LearningActivity.stub(id = nextActivityId)
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                section.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    section,
                    true,
                    StudyPlanWidgetFeature.SectionContentStatus.IDLE
                )
            ),
            activities = mapOf(activity.id to activity)
        )

        val currentActivity = state.getCurrentActivity()
        assertEquals(activity, currentActivity)
    }

    @Test
    fun `getCurrentActivity should return first TODO activity`() {
        val section = StudyPlanSection.stub(id = 1, activities = listOf(1L, 2L))
        val activity1 = LearningActivity.stub(id = 1, state = LearningActivityState.COMPLETED)
        val activity2 = LearningActivity.stub(id = 2)

        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                section.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    section,
                    isExpanded = false,
                    sectionContentStatus = StudyPlanWidgetFeature.SectionContentStatus.IDLE
                )
            ),
            activities = mapOf(activity1.id to activity1, activity2.id to activity2)
        )

        val currentActivity = state.getCurrentActivity()
        assertEquals(activity2, currentActivity)
    }

    @Test
    fun `getLoadedSectionActivities should return activities for given section`() {
        val sectionId = 1L
        val activities = listOf(1L, 2L).map { LearningActivity.stub(id = it) }
        val section = StudyPlanSection.stub(id = sectionId, activities = activities.map { it.id })
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                section.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    section,
                    true,
                    StudyPlanWidgetFeature.SectionContentStatus.IDLE
                )
            ),
            activities = activities.associateBy { it.id }
        )

        val loadedActivities = state.getLoadedSectionActivities(sectionId).toList()
        assertEquals(activities, loadedActivities)
    }

    @Test
    fun `getActivitiesToBeLoaded should return all the section activities for ROOT_TOPICS section`() {
        val sectionId = 1L
        val sectionActivitiesIds = List(2) { it.toLong() }
        val section = StudyPlanSection.stub(
            id = sectionId,
            type = StudyPlanSectionType.ROOT_TOPICS,
            activities = sectionActivitiesIds
        )
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                section.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = section,
                    isExpanded = true,
                    sectionContentStatus = StudyPlanWidgetFeature.SectionContentStatus.IDLE
                )
            ),
            activities = emptyMap()
        )

        val activitiesToBeLoaded = state.getRootTopicsActivitiesToBeLoaded(sectionId)
        assertEquals(section.activities, activitiesToBeLoaded)
    }

    @Test
    fun `getActivitiesToBeLoaded should return all activities after last loaded activity for ROOT_TOPICS section`() {
        val sectionId = 1L
        val sectionActivitiesIds = List(10) { it.toLong() }
        val section = StudyPlanSection.stub(
            id = sectionId,
            type = StudyPlanSectionType.ROOT_TOPICS,
            activities = sectionActivitiesIds
        )
        val loadedActivitiesIds = sectionActivitiesIds.take(5)
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                section.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = section,
                    isExpanded = true,
                    sectionContentStatus = StudyPlanWidgetFeature.SectionContentStatus.IDLE
                )
            ),
            activities = loadedActivitiesIds.associateWith { id -> LearningActivity.stub(id = id) }
        )

        val expectedActivitiesToBeLoaded = sectionActivitiesIds.subtract(loadedActivitiesIds.toSet())
        val actualActivitiesToBeLoaded = state.getRootTopicsActivitiesToBeLoaded(sectionId)
        assertEquals(expectedActivitiesToBeLoaded.toList(), actualActivitiesToBeLoaded)
    }

    @Test
    fun `getActivitiesToBeLoaded should return empty list if all ROOT_TOPICS section activities are loaded`() {
        val sectionId = 1L
        val sectionActivitiesIds = List(10) { it.toLong() }
        val section = StudyPlanSection.stub(
            id = sectionId,
            type = StudyPlanSectionType.ROOT_TOPICS,
            activities = sectionActivitiesIds
        )
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                section.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = section,
                    isExpanded = true,
                    sectionContentStatus = StudyPlanWidgetFeature.SectionContentStatus.IDLE
                )
            ),
            activities = sectionActivitiesIds.associateWith { id -> LearningActivity.stub(id = id) }
        )

        val actualActivitiesToBeLoaded = state.getRootTopicsActivitiesToBeLoaded(sectionId)
        assertEquals(
            expected = emptyList(),
            actual = actualActivitiesToBeLoaded
        )
    }

    @Test
    fun `getActivitiesToBeLoaded should return empty list for ROOT_TOPICS sections`() {
        val nonRootSectionTypes =
            StudyPlanSectionType.entries - StudyPlanSectionType.ROOT_TOPICS
        nonRootSectionTypes.forEach { sectionType ->
            val sectionId = 1L
            val sectionActivitiesIds = List(2) { it.toLong() }
            val section = StudyPlanSection.stub(
                id = sectionId,
                type = sectionType,
                activities = sectionActivitiesIds
            )
            val state = StudyPlanWidgetFeature.State(
                studyPlanSections = mapOf(
                    section.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                        studyPlanSection = section,
                        isExpanded = true,
                        sectionContentStatus = StudyPlanWidgetFeature.SectionContentStatus.IDLE
                    )
                ),
                activities = emptyMap()
            )

            val activitiesToBeLoaded = state.getRootTopicsActivitiesToBeLoaded(sectionId)
            assertEquals(
                expected = emptyList(),
                actual = activitiesToBeLoaded
            )
        }
    }

    @Test
    fun `isActivityLocked should return true for locked activities in case of topics limit`() {
        val section = StudyPlanSection.stub(
            id = 1,
            type = StudyPlanSectionType.ROOT_TOPICS,
            activities = listOf(1L, 2L, 3L)
        )
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                section.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    section,
                    isExpanded = false,
                    sectionContentStatus = StudyPlanWidgetFeature.SectionContentStatus.IDLE
                )
            ),
            activities = mapOf(1L to LearningActivity.stub(id = 1)),
            profile = Profile.stub(),
            subscriptionLimitType = SubscriptionLimitType.TOPICS,
            learnedTopicsCount = 1
        )

        val isLocked = state.isActivityLocked(section.id, 3L)
        assertTrue(isLocked)
    }

    @Test
    fun `getUnlockedActivitiesCount should return unlocked activities count for root topics section`() {
        val section = StudyPlanSection.stub(
            id = 1,
            type = StudyPlanSectionType.ROOT_TOPICS,
            activities = listOf(1L, 2L, 3L)
        )
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                section.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    section,
                    isExpanded = false,
                    sectionContentStatus = StudyPlanWidgetFeature.SectionContentStatus.IDLE
                )
            ),
            profile = Profile.stub(
                featureValues = FeatureValues(mobileContentTrialFreeTopics = 2),
                featuresMap = mapOf(FeatureKeys.MOBILE_CONTENT_TRIAL to true),
            ),
            subscriptionLimitType = SubscriptionLimitType.TOPICS,
            learnedTopicsCount = 1
        )

        val unlockedCount = state.getUnlockedActivitiesCount(section.id)
        assertEquals(1, unlockedCount)
    }

    @Test
    fun `getUnlockedActivitiesCount should return null if not root topics section`() {
        val section = StudyPlanSection.stub(
            id = 1,
            type = StudyPlanSectionType.STAGE,
            activities = listOf(1L, 2L, 3L)
        )
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                section.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    section,
                    isExpanded = false,
                    sectionContentStatus = StudyPlanWidgetFeature.SectionContentStatus.IDLE
                )
            ),
            profile = Profile.stub(
                featureValues = FeatureValues(mobileContentTrialFreeTopics = 2),
                featuresMap = mapOf(FeatureKeys.MOBILE_CONTENT_TRIAL to true),
            ),
            subscriptionLimitType = SubscriptionLimitType.TOPICS,
            learnedTopicsCount = 1
        )

        val unlockedCount = state.getUnlockedActivitiesCount(section.id)
        assertNull(unlockedCount)
    }

    @Test
    fun `isPaywallShown should return true if all free topics are solved in root topics section`() {
        val section = StudyPlanSection.stub(
            id = 1,
            type = StudyPlanSectionType.ROOT_TOPICS,
            activities = listOf(1L, 2L, 3L)
        )
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                section.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    section,
                    isExpanded = false,
                    sectionContentStatus = StudyPlanWidgetFeature.SectionContentStatus.IDLE
                )
            ),
            profile = Profile.stub(
                featureValues = FeatureValues(mobileContentTrialFreeTopics = 10),
                featuresMap = mapOf(FeatureKeys.MOBILE_CONTENT_TRIAL to true),
            ),
            subscriptionLimitType = SubscriptionLimitType.TOPICS,
            learnedTopicsCount = 10
        )

        val isPaywallShown = state.isPaywallShown()
        assertTrue(isPaywallShown)
    }

    @Test
    fun `isPaywallShown should return false if not root topics section`() {
        val section = StudyPlanSection.stub(
            id = 1,
            type = StudyPlanSectionType.STAGE,
            activities = listOf(1L, 2L, 3L)
        )
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                section.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    section,
                    isExpanded = false,
                    sectionContentStatus = StudyPlanWidgetFeature.SectionContentStatus.IDLE
                )
            ),
            profile = Profile.stub(
                featureValues = FeatureValues(mobileContentTrialFreeTopics = 10),
                featuresMap = mapOf(FeatureKeys.MOBILE_CONTENT_TRIAL to true),
            ),
            subscriptionLimitType = SubscriptionLimitType.TOPICS,
            learnedTopicsCount = 10
        )

        val isPaywallShown = state.isPaywallShown()
        assertFalse(isPaywallShown)
    }

    @Test
    fun `isPaywallShown should return false if there are multiple root topics sections`() {
        val section1 = StudyPlanSection.stub(
            id = 1,
            type = StudyPlanSectionType.ROOT_TOPICS,
            activities = listOf(1L)
        )
        val section2 = StudyPlanSection.stub(
            id = 2,
            type = StudyPlanSectionType.ROOT_TOPICS,
            activities = listOf(2L)
        )

        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                section1.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    section1,
                    isExpanded = false,
                    sectionContentStatus = StudyPlanWidgetFeature.SectionContentStatus.IDLE
                ),
                section2.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    section2,
                    isExpanded = false,
                    sectionContentStatus = StudyPlanWidgetFeature.SectionContentStatus.IDLE
                )
            ),
            profile = Profile.stub(
                featureValues = FeatureValues(mobileContentTrialFreeTopics = 10),
                featuresMap = mapOf(FeatureKeys.MOBILE_CONTENT_TRIAL to true),
            ),
            subscriptionLimitType = SubscriptionLimitType.TOPICS,
            learnedTopicsCount = 10
        )

        val isPaywallShown = state.isPaywallShown()
        assertFalse(isPaywallShown)
    }

    @Test
    fun `isPaywallShown should return false if unlocked activities count is greater than zero`() {
        val section = StudyPlanSection.stub(
            id = 1,
            type = StudyPlanSectionType.ROOT_TOPICS,
            activities = listOf(1L, 2L, 3L)
        )
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                section.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    section,
                    isExpanded = false,
                    sectionContentStatus = StudyPlanWidgetFeature.SectionContentStatus.IDLE
                )
            ),
            profile = Profile.stub(
                featureValues = FeatureValues(mobileContentTrialFreeTopics = 10),
                featuresMap = mapOf(FeatureKeys.MOBILE_CONTENT_TRIAL to true),
            ),
            subscriptionLimitType = SubscriptionLimitType.TOPICS,
            learnedTopicsCount = 5
        )

        val isPaywallShown = state.isPaywallShown()
        assertFalse(isPaywallShown)
    }
}