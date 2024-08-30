package org.hyperskill.study_plan.widget

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.getActivitiesBeforeCurrentActivityToBeLoaded
import org.hyperskill.learning_activities.domain.model.stub
import org.hyperskill.study_plan.domain.model.stub

class StudyPlanExpandCompletedActivitiesTest {
    @Test
    fun `getActivitiesBeforeCurrentActivityToBeLoaded should return all the activities before last loaded activity`() {
        val sectionId = 1L
        val sectionActivitiesIds = List(10) { it.toLong() }
        val section = StudyPlanSection.stub(
            id = sectionId,
            type = StudyPlanSectionType.ROOT_TOPICS,
            activities = sectionActivitiesIds
        )
        val loadedActivitiesIds = listOf(4L, /*5L,*/ 6L, /*7L,*/ 8L)
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                section.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = section,
                    isExpanded = true,
                    mainPageContentStatus = StudyPlanWidgetFeature.ContentStatus.IDLE,
                    nextPageContentStatus = StudyPlanWidgetFeature.PageContentStatus.IDLE,
                    completedPageContentStatus = StudyPlanWidgetFeature.PageContentStatus.IDLE
                )
            ),
            activities = loadedActivitiesIds.associateWith { id -> LearningActivity.stub(id = id) }
        )

        val expectedActivitiesToBeLoaded = listOf(0L, 1L, 2L, 3L)
        val actualActivitiesToBeLoaded = state.getActivitiesBeforeCurrentActivityToBeLoaded(sectionId)
        assertEquals(
            expected = expectedActivitiesToBeLoaded.toList(),
            actual = actualActivitiesToBeLoaded
        )
    }

    @Test
    fun `getActivitiesBeforeCurrentActivityToBeLoaded should return empty list for non current sections`() {
        val allActivities = List(10) { it.toLong() }

        val currentSectionId = 0L
        val currentSectionActivities = listOf(/*0L, 1L, 2L,*/ 3L, 4L)
        val currentSection = StudyPlanSection.stub(
            id = currentSectionId,
            type = StudyPlanSectionType.EXTRA_TOPICS,
            activities = currentSectionActivities
        )

        val nextSectionId = 1L
        val nextSectionActivities = listOf(/*5L, 6L, 7L,*/ 8L, 9L)
        val nextSection = StudyPlanSection.stub(
            id = nextSectionId,
            type = StudyPlanSectionType.EXTRA_TOPICS,
            activities = nextSectionActivities
        )

        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                currentSection.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = currentSection,
                    isExpanded = true,
                    mainPageContentStatus = StudyPlanWidgetFeature.ContentStatus.IDLE,
                    nextPageContentStatus = StudyPlanWidgetFeature.PageContentStatus.IDLE,
                    completedPageContentStatus = StudyPlanWidgetFeature.PageContentStatus.IDLE
                ),
                nextSection.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = nextSection,
                    isExpanded = true,
                    mainPageContentStatus = StudyPlanWidgetFeature.ContentStatus.IDLE,
                    nextPageContentStatus = StudyPlanWidgetFeature.PageContentStatus.IDLE,
                    completedPageContentStatus = StudyPlanWidgetFeature.PageContentStatus.IDLE
                )
            ),
            activities = allActivities.associateWith { id -> LearningActivity.stub(id = id) }
        )

        assertEquals(
            expected = emptyList(),
            actual = state.getActivitiesBeforeCurrentActivityToBeLoaded(nextSectionId)
        )
    }
}