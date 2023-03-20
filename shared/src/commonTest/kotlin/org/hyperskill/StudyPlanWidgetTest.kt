package org.hyperskill

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanStatus
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewState
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewStateMapper
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class StudyPlanWidgetTest {

    private val reducer = StudyPlanWidgetReducer()

    @Test
    fun `Initialize message should trigger studyPLan fetching`() {
        val initialState = StudyPlanWidgetFeature.State()
        val (state, actions) = reducer.reduce(initialState, StudyPlanWidgetFeature.Message.Initialize)
        assertContains(actions, StudyPlanWidgetFeature.InternalActions.FetchStudyPlan)
        assertEquals(state.sectionsStatus, StudyPlanWidgetFeature.ContentStatus.LOADING)
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
        val initialState = StudyPlanWidgetFeature.State(sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADING)
        val (state, actions) =
            reducer.reduce(initialState, StudyPlanWidgetFeature.StudyPlanFetchResult.Success(studyPlanStub))
        assertContains(actions, StudyPlanWidgetFeature.InternalActions.FetchSections(expectedSections))
        assertEquals(studyPlanStub, state.studyPlan)
        assertEquals(initialState.sectionsStatus, state.sectionsStatus)
    }

    @Test
    fun `Sections status should be Loaded after loading finish`() {
        val (state, _) = reducer.reduce(
            StudyPlanWidgetFeature.State(),
            StudyPlanWidgetFeature.SectionsFetchResult.Success(emptyList())
        )
        assertEquals(StudyPlanWidgetFeature.ContentStatus.LOADED, state.sectionsStatus)
    }

    @Test
    fun `Loaded sections should filtered by visibility`() {
        val visibleSection = studyPlanSectionStub(id = 0, isVisible = true)
        val hiddenSection = studyPlanSectionStub(id = 1, isVisible = false)
        val (state, _) = reducer.reduce(
            StudyPlanWidgetFeature.State(),
            StudyPlanWidgetFeature.SectionsFetchResult.Success(listOf(visibleSection, hiddenSection))
        )
        assertEquals(visibleSection, state.studyPlanSections[visibleSection.id]?.studyPlanSection)
        assertEquals(null, state.studyPlanSections[hiddenSection.id])
    }

    @Test
    fun `First section should be expanded and its activities loading should be triggered`() {
        val firstSection = studyPlanSectionStub(id = 0, activities = listOf(0, 1, 2))
        val secondSection = studyPlanSectionStub(id = 1)
        val (state, actions) = reducer.reduce(
            StudyPlanWidgetFeature.State(),
            StudyPlanWidgetFeature.SectionsFetchResult.Success(listOf(firstSection, secondSection))
        )
        assertContains(actions, StudyPlanWidgetFeature.InternalActions.FetchActivities(firstSection.id, firstSection.activities))
        assertEquals(true, state.studyPlanSections[firstSection.id]?.isExpanded)
    }

    @Test
    fun `Section content status should be Loaded after success activities loading`() {
        val sectionId = 0L
        val initialState = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                sectionId to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(sectionId),
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADING,
                    isExpanded = true
                )
            )
        )
        val (state, _) =
            reducer.reduce(
                initialState,
                StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success(sectionId = sectionId, emptyList())
            )
        assertEquals(StudyPlanWidgetFeature.ContentStatus.LOADED, state.studyPlanSections[sectionId]?.contentStatus)
    }

    @Test
    fun `All activities before current one should be dropped`() {
        val sectionId = 0L
        val skippedActivity = stubLearningActivity(id = 0L, state = LearningActivityState.SKIPPED)
        val completedActivity = stubLearningActivity(id = 1L, state = LearningActivityState.COMPLETED)
        val todoActivity = stubLearningActivity(id = 2L, state = LearningActivityState.TODO)
        val (state, _) =
            reducer.reduce(
                StudyPlanWidgetFeature.State(),
                StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success(
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
                StudyPlanWidgetFeature.State(),
                StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success(sectionId, firstActivities)
            )
        val (secondState, _) =
            reducer.reduce(
                firstState,
                StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success(sectionId, secondActivities)
            )

        val resultActivitiesIds = secondState.activities[sectionId]?.map { it.id }

        assertEquals(expectedActivitiesIds, resultActivitiesIds)
    }

    @Test
    fun `Collapsed section or IDLE state section should be collapsed in view state`() {
        val sectionId = 0L
        val section = studyPlanSectionStub(id = sectionId)

        val collapsedSection = StudyPlanWidgetFeature.StudyPlanSectionInfo(
            studyPlanSection = section,
            contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
            isExpanded = false
        )

        val idleSection = StudyPlanWidgetFeature.StudyPlanSectionInfo(
            studyPlanSection = section,
            contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
            isExpanded = false
        )

        listOf(collapsedSection, idleSection).forEach { givenSection ->
            val state = StudyPlanWidgetFeature.State(
                studyPlanSections = mapOf(sectionId to givenSection),
                sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
            )

            val viewState = StudyPlanWidgetViewStateMapper.map(state)

            val expectedViewStateSections = StudyPlanWidgetViewState.Content(
                sections = listOf(
                    contentViewState(
                        section = section,
                        content = StudyPlanWidgetViewState.SectionContent.Collapsed
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
        val section = StudyPlanWidgetFeature.StudyPlanSectionInfo(
            studyPlanSection = studyPlanSectionStub(sectionId),
            contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADING,
            isExpanded = true
        )
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(sectionId to section),
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
            activities = mapOf(sectionId to setOf(stubLearningActivity(activityId)))
        )

        val viewState = StudyPlanWidgetViewStateMapper.map(state)

        val expectedViewState = StudyPlanWidgetViewState.Content(
            sections = listOf(
                contentViewState(
                    section = section.studyPlanSection,
                    content = StudyPlanWidgetViewState.SectionContent.Content(
                        listOf(StudyPlanWidgetViewState.SectionItem(id = activityId))
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

        val section = StudyPlanWidgetFeature.StudyPlanSectionInfo(
            studyPlanSection = studyPlanSectionStub(sectionId),
            contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
            isExpanded = true
        )
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(sectionId to section),
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
            activities = mapOf(sectionId to setOf(stubLearningActivity(activityId)))
        )

        val viewState = StudyPlanWidgetViewStateMapper.map(state)

        val expectedViewState = StudyPlanWidgetViewState.Content(
            sections = listOf(
                contentViewState(
                    section = section.studyPlanSection,
                    content = StudyPlanWidgetViewState.SectionContent.Content(
                        listOf(StudyPlanWidgetViewState.SectionItem(id = activityId))
                    )
                )
            )
        )

        assertEquals(expectedViewState, viewState)
    }

    private fun contentViewState(section: StudyPlanSection, content: StudyPlanWidgetViewState.SectionContent) =
        StudyPlanWidgetViewState.Section(
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