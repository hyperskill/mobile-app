package org.hyperskill.study_plan.widget

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.ContentStatus
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.PageContentStatus
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import org.hyperskill.app.study_plan.widget.presentation.getNextRootTopicsActivitiesToBeLoaded
import org.hyperskill.learning_activities.domain.model.stub
import org.hyperskill.study_plan.domain.model.stub

class StudyPlanLoadMoreActivitiesTest {

    private val reducer = StudyPlanWidgetReducer()

    @Test
    fun `getNextRootTopicsActivitiesToBeLoaded should return all the section activities for ROOT_TOPICS section`() {
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
                    mainPageContentStatus = ContentStatus.IDLE,
                    nextPageContentStatus = PageContentStatus.IDLE,
                    completedPageContentStatus = PageContentStatus.IDLE
                )
            ),
            activities = emptyMap()
        )

        val activitiesToBeLoaded = state.getNextRootTopicsActivitiesToBeLoaded(sectionId)
        assertEquals(section.activities, activitiesToBeLoaded)
    }

    // ktlint-disable
    @Test
    fun `getNextRootTopicsActivitiesToBeLoaded should return all activities after last loaded activity for ROOT_TOPICS section`() {
        val sectionId = 1L
        val sectionActivitiesIds = List(10) { it.toLong() }
        val section = StudyPlanSection.stub(
            id = sectionId,
            type = StudyPlanSectionType.ROOT_TOPICS,
            activities = sectionActivitiesIds
        )
        val loadedActivitiesIds = listOf(/*0L,*/ 1L, /*2L,*/ 3L, 4L)
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                section.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = section,
                    isExpanded = true,
                    mainPageContentStatus = ContentStatus.IDLE,
                    nextPageContentStatus = PageContentStatus.IDLE,
                    completedPageContentStatus = PageContentStatus.IDLE
                )
            ),
            activities = loadedActivitiesIds.associateWith { id -> LearningActivity.stub(id = id) }
        )

        val expectedActivitiesToBeLoaded = listOf(5L, 6L, 7L, 8L, 9L)
        val actualActivitiesToBeLoaded = state.getNextRootTopicsActivitiesToBeLoaded(sectionId)
        assertEquals(expectedActivitiesToBeLoaded.toList(), actualActivitiesToBeLoaded)
    }

    /*ktlint-disable*/
    @Test
    fun `getNextRootTopicsActivitiesToBeLoaded should return empty list if all ROOT_TOPICS section activities are loaded`() {
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
                    mainPageContentStatus = ContentStatus.IDLE,
                    nextPageContentStatus = PageContentStatus.IDLE,
                    completedPageContentStatus = PageContentStatus.IDLE
                )
            ),
            activities = sectionActivitiesIds.associateWith { id -> LearningActivity.stub(id = id) }
        )

        val actualActivitiesToBeLoaded = state.getNextRootTopicsActivitiesToBeLoaded(sectionId)
        assertEquals(
            expected = emptyList(),
            actual = actualActivitiesToBeLoaded
        )
    }

    @Test
    fun `getNextRootTopicsActivitiesToBeLoaded should return empty list for ROOT_TOPICS sections`() {
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
                        mainPageContentStatus = ContentStatus.IDLE,
                        nextPageContentStatus = PageContentStatus.IDLE,
                        completedPageContentStatus = PageContentStatus.IDLE
                    )
                ),
                activities = emptyMap()
            )

            val activitiesToBeLoaded = state.getNextRootTopicsActivitiesToBeLoaded(sectionId)
            assertEquals(
                expected = emptyList(),
                actual = activitiesToBeLoaded
            )
        }
    }

    /*ktlint-disable*/
    @Test
    fun `After successfully fetch completed activities page status should become LOADED even if not all activities are loaded`() {
        val allActivities = List(10) { it.toLong() }
        val mainPageActivities = listOf(5L, 6L, 7L, 8L, 9L)
        val loadedCompletedPageActivities = listOf(/*0L,*/ 1L, /*2L, 3L,*/ 4L)

        val sectionId = 0L
        val section = StudyPlanSection.stub(
            id = sectionId,
            type = StudyPlanSectionType.EXTRA_TOPICS,
            activities = allActivities
        )

        val initialState = StudyPlanWidgetFeature.State(
            sectionsStatus = ContentStatus.LOADED,
            studyPlanSections = mapOf(
                section.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = section,
                    isExpanded = true,
                    mainPageContentStatus = ContentStatus.LOADED,
                    nextPageContentStatus = PageContentStatus.IDLE,
                    completedPageContentStatus = PageContentStatus.LOADING
                )
            ),
            activities = mainPageActivities.associateWith { id -> LearningActivity.stub(id = id) }
        )

        val (state, _) = reducer.reduce(
            initialState,
            StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success(
                sectionId = sectionId,
                activities = loadedCompletedPageActivities.map { id -> LearningActivity.stub(id = id) },
                targetPage = StudyPlanWidgetFeature.SectionPage.NEXT
            )
        )

        assertEquals(
            expected = PageContentStatus.LOADED,
            actual = state.studyPlanSections[sectionId]?.nextPageContentStatus
        )
    }
}