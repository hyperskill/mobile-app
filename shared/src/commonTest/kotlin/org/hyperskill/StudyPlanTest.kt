package org.hyperskill

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanStatus
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature
import org.hyperskill.app.study_plan.presentation.StudyPlanReducer
import org.hyperskill.app.study_plan.view.StudyPlanViewState
import org.hyperskill.app.study_plan.view.StudyPlanViewStateMapper
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class StudyPlanTest {

    private val reducer = StudyPlanReducer()

    @Test
    fun `Initialize message should trigger studyPLan fetching`() {
        val initialState = StudyPlanFeature.State()
        val (state, actions) = reducer.reduce(initialState, StudyPlanFeature.Message.Initialize)
        assertContains(actions, StudyPlanFeature.InternalActions.FetchStudyPlan)
        assertEquals(state.sectionsStatus, StudyPlanFeature.ContentStatus.LOADING)
    }

    @Test
    fun `Sections loading is should be triggered after`() {
        val expectedSections = listOf(0L, 1L, 2L)
        val studyPlanStub = StudyPlan(
            id = 0,
            trackId = null,
            projectId = null,
            sections = expectedSections,
            createdAt = "",
            secondsToReachProject = 0L,
            secondsToReachTrack = 0L,
            status = StudyPlanStatus.READY
        )
        val initialState = StudyPlanFeature.State(sectionsStatus = StudyPlanFeature.ContentStatus.LOADING)
        val (state, actions) =
            reducer.reduce(initialState, StudyPlanFeature.StudyPlanFetchResult.Success(studyPlanStub))
        assertContains(actions, StudyPlanFeature.InternalActions.FetchSections(expectedSections))
        assertEquals(studyPlanStub, state.studyPlan)
        assertEquals(initialState.sectionsStatus, state.sectionsStatus)
    }

    @Test
    fun `Sections status should be Loaded after loading finish`() {
        val (state, _) = reducer.reduce(
            StudyPlanFeature.State(),
            StudyPlanFeature.SectionsFetchResult.Success(emptyList())
        )
        assertEquals(StudyPlanFeature.ContentStatus.LOADED, state.sectionsStatus)
    }

    @Test
    fun `Loaded sections should filtered by visibility`() {
        val visibleSection = studyPlanSectionStub(id = 0, isVisible = true)
        val hiddenSection = studyPlanSectionStub(id = 1, isVisible = false)
        val (state, _) = reducer.reduce(
            StudyPlanFeature.State(),
            StudyPlanFeature.SectionsFetchResult.Success(listOf(visibleSection, hiddenSection))
        )
        assertEquals(visibleSection, state.studyPlanSections[visibleSection.id]?.studyPlanSection)
        assertEquals(null, state.studyPlanSections[hiddenSection.id])
    }

    @Test
    fun `First section should be expanded and its activities loading should be triggered`() {
        val firstSection = studyPlanSectionStub(id = 0, activities = listOf(0, 1, 2))
        val secondSection = studyPlanSectionStub(id = 1)
        val (state, actions) = reducer.reduce(
            StudyPlanFeature.State(),
            StudyPlanFeature.SectionsFetchResult.Success(listOf(firstSection, secondSection))
        )
        assertContains(actions, StudyPlanFeature.InternalActions.FetchActivities(firstSection.id, firstSection.activities))
        assertEquals(true, state.studyPlanSections[firstSection.id]?.isExpanded)
    }

    @Test
    fun `Section content status should be Loaded after success activities loading`() {
        val sectionId = 0L
        val initialState = StudyPlanFeature.State(
            studyPlanSections = mapOf(
                sectionId to StudyPlanFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(sectionId),
                    contentStatus = StudyPlanFeature.ContentStatus.LOADING,
                    isExpanded = true
                )
            )
        )
        val (state, _) =
            reducer.reduce(
                initialState,
                StudyPlanFeature.LearningActivitiesFetchResult.Success(sectionId = sectionId, emptyList())
            )
        assertEquals(StudyPlanFeature.ContentStatus.LOADED, state.studyPlanSections[sectionId]?.contentStatus)
    }

    @Test
    fun `All activities before current one should be dropped`() {
        val sectionId = 0L
        val skippedActivity = stubLearningActivity(id = 0L, state = LearningActivityState.SKIPPED)
        val completedActivity = stubLearningActivity(id = 1L, state = LearningActivityState.COMPLETED)
        val todoActivity = stubLearningActivity(id = 2L, state = LearningActivityState.TODO)
        val (state, _) =
            reducer.reduce(
                StudyPlanFeature.State(),
                StudyPlanFeature.LearningActivitiesFetchResult.Success(
                    sectionId = sectionId,
                    listOf(skippedActivity, completedActivity, todoActivity)
                )
            )
        val resultActivities = state.activities[sectionId]
        assertNotNull(resultActivities)
        assertContains(resultActivities, todoActivity)
        assertTrue {
            resultActivities.none {
                it.state == LearningActivityState.SKIPPED || it.state == LearningActivityState.COMPLETED
            }
        }
    }

    @Test
    fun `New activities should be combined with previous activities and should be unique`() {
        val sectionId = 0L
        val firstActivities = List(2) { index ->
            stubLearningActivity(id = index.toLong())
        }
        val secondActivities = List(5) { index ->
            stubLearningActivity(id = index.toLong())
        }

        val expectedActivitiesIds: List<Long> = listOf(0, 1, 2, 3, 4)

        val (firstState, _) =
            reducer.reduce(
                StudyPlanFeature.State(),
                StudyPlanFeature.LearningActivitiesFetchResult.Success(sectionId, firstActivities)
            )
        val (secondState, _) =
            reducer.reduce(
                firstState,
                StudyPlanFeature.LearningActivitiesFetchResult.Success(sectionId, secondActivities)
            )

        val resultActivitiesIds = secondState.activities[sectionId]?.map { it.id }

        assertEquals(expectedActivitiesIds, resultActivitiesIds)
    }

    @Test
    fun `Collapsed section or IDLE state section should be collapsed in view state`() {
        val sectionId = 0L
        val section = studyPlanSectionStub(id = sectionId)

        val collapsedSection = StudyPlanFeature.StudyPlanSectionInfo(
            studyPlanSection = section,
            contentStatus = StudyPlanFeature.ContentStatus.LOADED,
            isExpanded = false
        )

        val idleSection = StudyPlanFeature.StudyPlanSectionInfo(
            studyPlanSection = section,
            contentStatus = StudyPlanFeature.ContentStatus.LOADED,
            isExpanded = false
        )

        listOf(collapsedSection, idleSection).forEach { givenSection ->
            val state = StudyPlanFeature.State(
                studyPlanSections = mapOf(sectionId to givenSection),
                sectionsStatus = StudyPlanFeature.ContentStatus.LOADED
            )

            val viewState = StudyPlanViewStateMapper.map(state)

            val expectedViewStateSections = StudyPlanViewState.Content(
                sections = listOf(
                    contentViewState(
                        section = section,
                        content = StudyPlanViewState.SectionContent.Collapsed
                    )
                )
            )

            assertEquals(expectedViewStateSections, viewState)
        }
    }

    @Test
    fun `Not empty sections should not show loading`() {
        val sectionId = 0L
        val activityId = 0L
        val section = StudyPlanFeature.StudyPlanSectionInfo(
            studyPlanSection = studyPlanSectionStub(sectionId),
            contentStatus = StudyPlanFeature.ContentStatus.LOADING,
            isExpanded = true
        )
        val state = StudyPlanFeature.State(
            studyPlanSections = mapOf(sectionId to section),
            sectionsStatus = StudyPlanFeature.ContentStatus.LOADED,
            activities = mapOf(sectionId to setOf(stubLearningActivity(activityId)))
        )

        val viewState = StudyPlanViewStateMapper.map(state)

        val expectedViewState = StudyPlanViewState.Content(
            sections = listOf(
                contentViewState(
                    section = section.studyPlanSection,
                    content = StudyPlanViewState.SectionContent.Content(
                        listOf(StudyPlanViewState.SectionItem(id = activityId))
                    )
                )
            )
        )

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun `Loaded section activities should be a section items in viewState`() {
        val sectionId = 0L
        val activityId = 0L

        val section = StudyPlanFeature.StudyPlanSectionInfo(
            studyPlanSection = studyPlanSectionStub(sectionId),
            contentStatus = StudyPlanFeature.ContentStatus.LOADED,
            isExpanded = true
        )
        val state = StudyPlanFeature.State(
            studyPlanSections = mapOf(sectionId to section),
            sectionsStatus = StudyPlanFeature.ContentStatus.LOADED,
            activities = mapOf(sectionId to setOf(stubLearningActivity(activityId)))
        )

        val viewState = StudyPlanViewStateMapper.map(state)

        val expectedViewState = StudyPlanViewState.Content(
            sections = listOf(
                contentViewState(
                    section = section.studyPlanSection,
                    content = StudyPlanViewState.SectionContent.Content(
                        listOf(StudyPlanViewState.SectionItem(id = activityId))
                    )
                )
            )
        )

        assertEquals(expectedViewState, viewState)
    }

    private fun contentViewState(section: StudyPlanSection, content: StudyPlanViewState.SectionContent) =
        StudyPlanViewState.Section(
            id = section.id,
            title = section.title,
            subtitle = section.subtitle,
            content = content
        )

    private fun studyPlanSectionStub(
        id: Long,
        isVisible: Boolean = true,
        activities: List<Long> = emptyList()
    ) =
        StudyPlanSection(
            id = id,
            studyPlanId = 0,
            targetId = 0,
            targetType = "",
            isVisible = isVisible,
            title = "",
            subtitle = "",
            topicsCount = 0,
            completedTopicsCount = 0,
            secondsToComplete = 0f,
            activities = activities
        )

    private fun stubLearningActivity(
        id: Long,
        state: LearningActivityState = LearningActivityState.TODO,
        targetId: Long = 0L,
        type: LearningActivityType = LearningActivityType.LEARN_TOPIC
    ) =
        LearningActivity(
            id = id,
            stateValue = state.value,
            targetId = 0L,
            typeValue = type.value
        )
}