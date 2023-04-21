package org.hyperskill

import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.StringResource
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail
import org.hyperskill.app.core.view.mapper.DateFormatter
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityTargetType
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanStatus
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import org.hyperskill.app.study_plan.widget.presentation.firstVisibleSection
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewState
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewStateMapper

class StudyPlanWidgetTest {

    private val reducer = StudyPlanWidgetReducer()

    private val resourceProviderStub = object : ResourceProvider {
        override fun getString(stringResource: StringResource): String =
            ""

        override fun getString(stringResource: StringResource, vararg args: Any): String =
            ""

        override fun getQuantityString(pluralsResource: PluralsResource, quantity: Int): String =
            ""

        override fun getQuantityString(pluralsResource: PluralsResource, quantity: Int, vararg args: Any): String =
            ""
    }

    private val studyPlanWidgetViewStateMapper = StudyPlanWidgetViewStateMapper(DateFormatter(resourceProviderStub))

    @Test
    fun `Get first visible section works correctly`() {
        val studyPlan = studyPlanStub(id = 0, sections = listOf(0, 1, 2, 3, 4))
        val studyPlanSections = listOf(
            studyPlanSectionStub(id = 0, isVisible = false),
            studyPlanSectionStub(id = 1, isVisible = true),
            studyPlanSectionStub(id = 2, isVisible = false),
            studyPlanSectionStub(id = 3, isVisible = true),
            studyPlanSectionStub(id = 4, isVisible = false)
        )
        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlan,
            studyPlanSections = studyPlanSections.associate {
                it.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = it,
                    isExpanded = false,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            }
        )

        assertEquals(state.firstVisibleSection(), studyPlanSections[1])
    }

    @Test
    fun `Initialize message should trigger studyPLan fetching`() {
        val initialState = StudyPlanWidgetFeature.State()
        val (state, actions) = reducer.reduce(initialState, StudyPlanWidgetFeature.Message.Initialize)
        assertContains(actions, StudyPlanWidgetFeature.InternalAction.FetchStudyPlan())
        assertEquals(state.sectionsStatus, StudyPlanWidgetFeature.ContentStatus.LOADING)
    }

    @Test
    fun `Receiving not ready studyPlan should trigger fetch studyPlan again`() {
        val initialState = StudyPlanWidgetFeature.State()
        StudyPlanStatus.values()
            .asSequence()
            .filterNot { it == StudyPlanStatus.READY }
            .forEach { status ->
                val studyPlan = studyPlanStub(id = 0, status = status)
                val (_, actions) = reducer.reduce(
                    initialState,
                    StudyPlanWidgetFeature.StudyPlanFetchResult.Success(studyPlan, 1, true)
                )
                assertTrue {
                    actions.any {
                        it is StudyPlanWidgetFeature.InternalAction.FetchStudyPlan
                    }
                }
            }
    }

    @Test
    fun `Receiving not ready studyPlan should trigger fetch studyPlan with delay`() {
        val initialState = StudyPlanWidgetFeature.State()
        val studyPlan = studyPlanStub(id = 0, status = StudyPlanStatus.INITING)
        repeat(5) { tryNumber ->
            val (_, actions) = reducer.reduce(
                initialState,
                StudyPlanWidgetFeature.StudyPlanFetchResult.Success(studyPlan, tryNumber, true)
            )
            val expectedAction = StudyPlanWidgetFeature.InternalAction.FetchStudyPlan(
                delayBeforeFetching = StudyPlanWidgetFeature.STUDY_PLAN_FETCH_INTERVAL * tryNumber,
                attemptNumber = tryNumber + 1
            )
            assertContains(actions, expectedAction)
        }
    }

    @Test
    fun `Receiving ready studyPlan should stop study plan polling`() {
        val initialState = StudyPlanWidgetFeature.State()
        val studyPlan = studyPlanStub(id = 0, status = StudyPlanStatus.READY)
        val (_, actions) = reducer.reduce(
            initialState,
            StudyPlanWidgetFeature.StudyPlanFetchResult.Success(studyPlan, 1, true)
        )
        assertTrue {
            actions.none {
                it is StudyPlanWidgetFeature.InternalAction.FetchStudyPlan
            }
        }
    }

    @Test
    fun `Receiving not ready studyPlan loading state should be shown`() {
        val initialState = StudyPlanWidgetFeature.State()
        StudyPlanStatus.values()
            .asSequence()
            .filterNot { it == StudyPlanStatus.READY }
            .forEach { status ->
                val studyPlan = studyPlanStub(id = 0, status = status)
                val (state, _) = reducer.reduce(
                    initialState,
                    StudyPlanWidgetFeature.StudyPlanFetchResult.Success(studyPlan, 1, true)
                )
                val viewState = studyPlanWidgetViewStateMapper.map(state)
                assertEquals(StudyPlanWidgetViewState.Loading, viewState)
            }
    }

    @Test
    fun `Receiving ready studyPlan should trigger sections loading`() {
        val expectedSections = listOf(0L, 1L, 2L)
        val studyPlanStub = studyPlanStub(
            id = 0,
            status = StudyPlanStatus.READY,
            sections = expectedSections
        )
        val initialState = StudyPlanWidgetFeature.State(sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADING)
        val (state, actions) =
            reducer.reduce(initialState, StudyPlanWidgetFeature.StudyPlanFetchResult.Success(studyPlanStub, 1, true))
        assertContains(actions, StudyPlanWidgetFeature.InternalAction.FetchSections(expectedSections))
        assertEquals(studyPlanStub, state.studyPlan)
        assertEquals(initialState.sectionsStatus, state.sectionsStatus)
    }

    @Test
    fun `Sections status should be Loaded after loading finish`() {
        val (state, _) = reducer.reduce(
            StudyPlanWidgetFeature.State(
                studyPlan = studyPlanStub(id = 0)
            ),
            StudyPlanWidgetFeature.SectionsFetchResult.Success(emptyList())
        )
        assertEquals(StudyPlanWidgetFeature.ContentStatus.LOADED, state.sectionsStatus)
    }

    @Test
    fun `Loaded sections should be filtered by visibility`() {
        val visibleSection = studyPlanSectionStub(id = 0, isVisible = true)
        val hiddenSection = studyPlanSectionStub(id = 1, isVisible = false)
        val (state, _) = reducer.reduce(
            StudyPlanWidgetFeature.State(studyPlan = studyPlanStub(id = 0, sections = listOf(0, 1))),
            StudyPlanWidgetFeature.SectionsFetchResult.Success(listOf(visibleSection, hiddenSection))
        )
        assertEquals(visibleSection, state.studyPlanSections[visibleSection.id]?.studyPlanSection)
        assertEquals(null, state.studyPlanSections[hiddenSection.id])
    }

    @Test
    fun `Sections in ViewState should be sorted by sections order in studyPlan`() {
        val expectedSectionsIds = listOf<Long>(3, 5, 2, 1, 4)
        val expectedViewState = StudyPlanWidgetViewState.Content(
            expectedSectionsIds.mapIndexed { index, sectionId ->
                sectionViewState(
                    section = studyPlanSectionStub(id = sectionId),
                    content = if (index > 0) {
                        StudyPlanWidgetViewState.SectionContent.Collapsed
                    } else {
                        StudyPlanWidgetViewState.SectionContent.Loading
                    }
                )
            }
        )

        val fetchedSectionsIds = listOf<Long>(1, 2, 3, 4, 5)

        val (state, _) = reducer.reduce(
            StudyPlanWidgetFeature.State(
                studyPlan = studyPlanStub(
                    id = 0,
                    sections = expectedSectionsIds,
                    status = StudyPlanStatus.READY
                )
            ),
            StudyPlanWidgetFeature.SectionsFetchResult.Success(
                fetchedSectionsIds.map { sectionId ->
                    studyPlanSectionStub(id = sectionId)
                }
            )
        )

        val viewState = studyPlanWidgetViewStateMapper.map(state)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun `First section should be expanded and its activities loading should be triggered`() {
        val firstSection = studyPlanSectionStub(id = 0, activities = listOf(0, 1, 2))
        val secondSection = studyPlanSectionStub(id = 1)
        val (state, actions) = reducer.reduce(
            StudyPlanWidgetFeature.State(
                studyPlan = studyPlanStub(id = 0, sections = listOf(0, 1))
            ),
            StudyPlanWidgetFeature.SectionsFetchResult.Success(listOf(firstSection, secondSection))
        )
        assertContains(
            actions,
            StudyPlanWidgetFeature.InternalAction.FetchActivities(firstSection.id, firstSection.activities)
        )
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
    fun `New activities should added to activities map`() {
        val sectionId = 0L
        val activities = List(2) { index ->
            stubLearningActivity(id = index.toLong())
        }

        val expectedActivitiesIds: Set<Long> = setOf(0, 1)

        val (state, _) =
            reducer.reduce(
                StudyPlanWidgetFeature.State(),
                StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success(sectionId, activities)
            )

        val resultActivitiesIds = state.activities.keys

        assertEquals(expectedActivitiesIds, resultActivitiesIds)
    }

    @Test
    fun `Clicking reload activities should trigger activities loading`() {
        val activities = listOf<Long>(0, 1, 2)
        val section = studyPlanSectionStub(id = 0, activities = activities)
        val initialState = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                0L to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    section,
                    isExpanded = true,
                    StudyPlanWidgetFeature.ContentStatus.ERROR
                )
            )
        )

        val (state, actions) =
            reducer.reduce(initialState, StudyPlanWidgetFeature.Message.RetryActivitiesLoading(section.id))

        assertContains(actions, StudyPlanWidgetFeature.InternalAction.FetchActivities(section.id, activities))
        assertEquals(StudyPlanWidgetFeature.ContentStatus.LOADING, state.studyPlanSections[section.id]?.contentStatus)
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
                studyPlan = studyPlanStub(id = 0, sections = listOf(sectionId)),
                studyPlanSections = mapOf(sectionId to givenSection),
                sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
            )

            val viewState = studyPlanWidgetViewStateMapper.map(state)

            val expectedViewStateSections = StudyPlanWidgetViewState.Content(
                sections = listOf(
                    sectionViewState(
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
            studyPlanSection = studyPlanSectionStub(sectionId, activities = listOf(activityId)),
            contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADING,
            isExpanded = true
        )
        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlanStub(id = 0, sections = listOf(sectionId)),
            studyPlanSections = mapOf(sectionId to section),
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
            activities = mapOf(activityId to stubLearningActivity(activityId))
        )

        val viewState = studyPlanWidgetViewStateMapper.map(state)

        val expectedViewState = StudyPlanWidgetViewState.Content(
            sections = listOf(
                sectionViewState(
                    section = section.studyPlanSection,
                    content = StudyPlanWidgetViewState.SectionContent.Content(
                        listOf(studyPlanSectionItemStub(activityId))
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
            studyPlanSection = studyPlanSectionStub(sectionId, activities = listOf(activityId)),
            contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
            isExpanded = true
        )
        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlanStub(id = 0, sections = listOf(sectionId)),
            studyPlanSections = mapOf(sectionId to section),
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
            activities = mapOf(activityId to stubLearningActivity(activityId))
        )

        val viewState = studyPlanWidgetViewStateMapper.map(state)

        val expectedViewState = StudyPlanWidgetViewState.Content(
            sections = listOf(
                sectionViewState(
                    section = section.studyPlanSection,
                    content = StudyPlanWidgetViewState.SectionContent.Content(
                        listOf(studyPlanSectionItemStub(activityId))
                    )
                )
            )
        )

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun `Reload content in background should trigger fetch studyPlan without loading indicators`() {
        val state = StudyPlanWidgetFeature.State(studyPlan = studyPlanStub(id = 0))
        val (_, actions) = reducer.reduce(state, StudyPlanWidgetFeature.Message.ReloadContentInBackground)
        assertContains(actions, StudyPlanWidgetFeature.InternalAction.FetchStudyPlan(showLoadingIndicators = false))
    }

    @Test
    fun `Reload content in background should persist content status for first visible section`() {
        val studyPlan = studyPlanStub(id = 0, sections = listOf(0, 1, 2, 3, 4))
        val studyPlanSections = listOf(
            studyPlanSectionStub(id = 0, isVisible = false),
            studyPlanSectionStub(id = 1, isVisible = true),
            studyPlanSectionStub(id = 2, isVisible = false),
            studyPlanSectionStub(id = 3, isVisible = true),
            studyPlanSectionStub(id = 4, isVisible = false)
        )
        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlan,
            studyPlanSections = studyPlanSections.associate {
                it.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = it,
                    isExpanded = false,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            }
        )

        val (newState, actions) = reducer.reduce(state, StudyPlanWidgetFeature.Message.ReloadContentInBackground)
        assertEquals(state.studyPlanSections[1]?.contentStatus, newState.studyPlanSections[1]?.contentStatus)
        assertContains(actions, StudyPlanWidgetFeature.InternalAction.FetchStudyPlan(showLoadingIndicators = false))
    }

    @Test
    fun `Section content item title in ViewState should be equal to learning activity title`() {
        val expectedViewState = StudyPlanWidgetViewState.Content(
            listOf(
                sectionViewState(
                    section = studyPlanSectionStub(id = 0, activities = listOf(0)),
                    content = StudyPlanWidgetViewState.SectionContent.Content(
                        sectionItems = listOf(
                            studyPlanSectionItemStub(
                                activityId = 0,
                                title = "Activity 1",
                                state = StudyPlanWidgetViewState.SectionItemState.LOCKED
                            )
                        )
                    )
                )
            )
        )

        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlanStub(id = 0, sections = listOf(0)),
            studyPlanSections = mapOf(
                0L to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(id = 0, activities = listOf(0)),
                    isExpanded = true,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            ),
            activities = mapOf(
                0L to stubLearningActivity(id = 0, title = "Activity 1")
            ),
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
        )

        val viewState = studyPlanWidgetViewStateMapper.map(state)
        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun `Section content item title in ViewState should be equal to learning activity id if title is blank`() {
        val expectedViewState = StudyPlanWidgetViewState.Content(
            listOf(
                sectionViewState(
                    section = studyPlanSectionStub(id = 0, activities = listOf(0)),
                    content = StudyPlanWidgetViewState.SectionContent.Content(
                        sectionItems = listOf(
                            studyPlanSectionItemStub(
                                activityId = 0,
                                title = "0",
                                state = StudyPlanWidgetViewState.SectionItemState.LOCKED
                            )
                        )
                    )
                )
            )
        )

        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlanStub(id = 0, sections = listOf(0)),
            studyPlanSections = mapOf(
                0L to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(id = 0, activities = listOf(0)),
                    isExpanded = true,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            ),
            activities = mapOf(0L to stubLearningActivity(id = 0, title = "")),
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
        )

        val viewState = studyPlanWidgetViewStateMapper.map(state)
        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun `Click on non current learning activity should do nothing`() {
        val activityId = 0L
        val state = StudyPlanWidgetFeature.State(
            activities = mapOf(activityId to stubLearningActivity(activityId, isCurrent = false))
        )

        val (newState, actions) = reducer.reduce(state, StudyPlanWidgetFeature.Message.ActivityClicked(activityId))

        assertEquals(state, newState)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `Click on stage implement learning activity should navigate to stage implementation`() {
        val activityId = 0L
        val projectId = 1L
        val stageId = 2L
        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlanStub(id = 0, projectId = projectId),
            activities = mapOf(
                activityId to stubLearningActivity(
                    activityId,
                    isCurrent = true,
                    type = LearningActivityType.IMPLEMENT_STAGE,
                    targetType = LearningActivityTargetType.STAGE,
                    targetId = stageId
                )
            )
        )

        val (newState, actions) = reducer.reduce(state, StudyPlanWidgetFeature.Message.ActivityClicked(activityId))

        assertEquals(state, newState)
        assertEquals(
            actions, setOf(StudyPlanWidgetFeature.Action.ViewAction.NavigateTo.StageImplementation(stageId, projectId))
        )
    }

    @Test
    fun `Click on stage implement learning activity with non stage target should do nothing`() {
        val activityId = 0L
        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlanStub(id = 0),
            activities = mapOf(
                activityId to stubLearningActivity(
                    activityId,
                    isCurrent = true,
                    type = LearningActivityType.IMPLEMENT_STAGE,
                    targetType = LearningActivityTargetType.STEP,
                    targetId = 1L
                )
            )
        )

        val (newState, actions) = reducer.reduce(state, StudyPlanWidgetFeature.Message.ActivityClicked(activityId))

        assertEquals(state, newState)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `Click on learn topic learning activity should navigate to step screen`() {
        val activityId = 0L
        val stepId = 1L
        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlanStub(id = 0),
            activities = mapOf(
                activityId to stubLearningActivity(
                    activityId,
                    isCurrent = true,
                    type = LearningActivityType.LEARN_TOPIC,
                    targetType = LearningActivityTargetType.STEP,
                    targetId = stepId
                )
            )
        )

        val (newState, actions) = reducer.reduce(state, StudyPlanWidgetFeature.Message.ActivityClicked(activityId))

        assertEquals(state, newState)
        assertEquals(actions.size, 1)

        val targetAction = actions.first()
        if (targetAction is StudyPlanWidgetFeature.Action.ViewAction.NavigateTo.StepScreen &&
            targetAction.stepRoute is StepRoute.Learn
        ) {
            assertEquals(targetAction.stepRoute.stepId, stepId)
        } else {
            fail("Unexpected action: $targetAction")
        }
    }

    @Test
    fun `Click on learn topic learning activity with non step target should do nothing`() {
        val activityId = 0L
        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlanStub(id = 0),
            activities = mapOf(
                activityId to stubLearningActivity(
                    activityId,
                    isCurrent = true,
                    type = LearningActivityType.LEARN_TOPIC,
                    targetType = LearningActivityTargetType.STAGE,
                    targetId = 1L
                )
            )
        )

        val (newState, actions) = reducer.reduce(state, StudyPlanWidgetFeature.Message.ActivityClicked(activityId))

        assertEquals(state, newState)
        assertTrue(actions.isEmpty())
    }

    private fun sectionViewState(section: StudyPlanSection, content: StudyPlanWidgetViewState.SectionContent) =
        StudyPlanWidgetViewState.Section(
            id = section.id,
            title = section.title,
            subtitle = section.subtitle.takeIf { it.isNotEmpty() },
            content = content,
            formattedTopicsCount = null,
            formattedTimeToComplete = null
        )

    private fun studyPlanStub(
        id: Long,
        projectId: Long? = null,
        status: StudyPlanStatus = StudyPlanStatus.READY,
        sections: List<Long> = emptyList()
    ) =
        StudyPlan(
            id = id,
            trackId = null,
            projectId = projectId,
            sections = sections,
            status = status,
            createdAt = "",
            secondsToReachTrack = 0f,
            secondsToReachProject = 0f
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
        type: LearningActivityType = LearningActivityType.LEARN_TOPIC,
        targetType: LearningActivityTargetType = LearningActivityTargetType.STEP,
        isCurrent: Boolean = false,
        title: String = ""
    ) =
        LearningActivity(
            id = id,
            stateValue = state.value,
            targetId = targetId,
            typeValue = type.value,
            isCurrent = isCurrent,
            targetTypeValue = targetType.value,
            title = title
        )

    private fun studyPlanSectionItemStub(
        activityId: Long,
        title: String = "",
        state: StudyPlanWidgetViewState.SectionItemState = StudyPlanWidgetViewState.SectionItemState.LOCKED
    ) =
        StudyPlanWidgetViewState.SectionItem(
            id = activityId,
            title = title.ifBlank { activityId.toString() },
            formattedProgress = null,
            progress = null,
            state = state,
            hypercoinsAward = null
        )
}