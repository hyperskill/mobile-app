package org.hyperskill.study_plan.widget

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.getRootTopicsActivitiesToBeLoaded
import org.hyperskill.learning_activities.domain.model.stub
import org.hyperskill.study_plan.domain.model.stub

class StudyPlanLoadMoreActivitiesTest {
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
}