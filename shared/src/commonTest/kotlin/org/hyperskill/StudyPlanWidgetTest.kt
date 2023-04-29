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
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedActivityHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedRetryActivitiesLoadingHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedRetryContentLoadingHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedSectionHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType
import org.hyperskill.app.study_plan.domain.model.StudyPlanStatus
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import org.hyperskill.app.study_plan.widget.presentation.getCurrentSection
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
    fun `Get first section works correctly`() {
        val expectedSectionId = 3L

        val studyPlan = studyPlanStub(id = 0, sections = listOf(0, 1, 2, 3, 4))
        val studyPlanSections = listOf(
            studyPlanSectionStub(id = 0, isVisible = false),
            studyPlanSectionStub(id = 1, isVisible = true, type = StudyPlanSectionType.NEXT_TRACK),
            studyPlanSectionStub(id = 2, isVisible = false),
            studyPlanSectionStub(id = 3, isVisible = true, type = StudyPlanSectionType.STAGE),
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

        assertEquals(studyPlanSections.first { it.id == expectedSectionId }, state.getCurrentSection())
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
    fun `Loaded sections should be filtered by supportance`() {
        val visibleUnsupportedSection = studyPlanSectionStub(
            id = 0,
            isVisible = true,
            type = StudyPlanSectionType.NEXT_TRACK
        )
        val hiddenSection = studyPlanSectionStub(id = 1, isVisible = false)
        val visibleSupportedSection = studyPlanSectionStub(
            id = 2,
            isVisible = true,
            type = StudyPlanSectionType.ROOT_TOPICS
        )

        val (state, _) = reducer.reduce(
            StudyPlanWidgetFeature.State(studyPlan = studyPlanStub(id = 0, sections = listOf(0, 1, 2))),
            StudyPlanWidgetFeature.SectionsFetchResult.Success(
                listOf(
                    visibleUnsupportedSection,
                    hiddenSection,
                    visibleSupportedSection
                )
            )
        )

        assertEquals(1, state.studyPlanSections.size)
        assertEquals(visibleSupportedSection, state.studyPlanSections[visibleSupportedSection.id]?.studyPlanSection)

        assertEquals(null, state.studyPlanSections[hiddenSection.id])
        assertEquals(null, state.studyPlanSections[visibleUnsupportedSection.id])
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
            StudyPlanWidgetFeature.InternalAction.FetchActivities(
                sectionId = firstSection.id,
                activitiesIds = firstSection.activities,
                sentryTransaction = HyperskillSentryTransactionBuilder.buildStudyPlanWidgetFetchLearningActivities(true)
            )
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

        assertContains(
            actions,
            StudyPlanWidgetFeature.InternalAction.FetchActivities(
                sectionId = section.id,
                activitiesIds = activities,
                sentryTransaction = HyperskillSentryTransactionBuilder.buildStudyPlanWidgetFetchLearningActivities(true)
            )
        )
        assertEquals(StudyPlanWidgetFeature.ContentStatus.LOADING, state.studyPlanSections[section.id]?.contentStatus)

        val analyticAction = actions.last() as StudyPlanWidgetFeature.InternalAction.LogAnalyticEvent
        if (analyticAction.analyticEvent is StudyPlanClickedRetryActivitiesLoadingHyperskillAnalyticEvent) {
            assertEquals(section.id, analyticAction.analyticEvent.sectionId)
        } else {
            fail("Wrong analytic event")
        }
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
    fun `Section content statistics in ViewState should be always visible for first visible section`() {
        fun makeState(isExpanded: Boolean): StudyPlanWidgetFeature.State =
            StudyPlanWidgetFeature.State(
                studyPlan = studyPlanStub(id = 0, sections = listOf(0)),
                studyPlanSections = mapOf(
                    0L to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                        studyPlanSection = studyPlanSectionStub(
                            id = 0,
                            completedTopicsCount = 1,
                            topicsCount = 10
                        ),
                        isExpanded = isExpanded,
                        contentStatus = StudyPlanWidgetFeature.ContentStatus.IDLE
                    )
                ),
                sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
            )

        val expectedViewState = StudyPlanWidgetViewState.Content(
            listOf(
                sectionViewState(
                    section = studyPlanSectionStub(id = 0),
                    content = StudyPlanWidgetViewState.SectionContent.Collapsed,
                    formattedTopicsCount = "1 / 10",
                    formattedTimeToComplete = null // TODO: add DateFormatter stub and test this
                )
            )
        )

        assertEquals(expectedViewState, studyPlanWidgetViewStateMapper.map(makeState(isExpanded = true)))
        assertEquals(expectedViewState, studyPlanWidgetViewStateMapper.map(makeState(isExpanded = false)))
    }

    @Test
    fun `Section content statistics in ViewState should be visible for non first expanded visible section`() {
        val expectedViewState = StudyPlanWidgetViewState.Content(
            listOf(
                sectionViewState(
                    section = studyPlanSectionStub(id = 0),
                    content = StudyPlanWidgetViewState.SectionContent.Content(
                        listOf(
                            studyPlanSectionItemStub(
                                activityId = 0,
                                state = StudyPlanWidgetViewState.SectionItemState.NEXT
                            )
                        )
                    )
                ),
                sectionViewState(
                    section = studyPlanSectionStub(id = 1),
                    content = StudyPlanWidgetViewState.SectionContent.Content(
                        listOf(
                            studyPlanSectionItemStub(
                                activityId = 1,
                                state = StudyPlanWidgetViewState.SectionItemState.LOCKED
                            )
                        )
                    ),
                    formattedTopicsCount = "1 / 10",
                    formattedTimeToComplete = null // TODO: add DateFormatter stub and test this
                )
            )
        )

        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlanStub(id = 0, sections = listOf(0, 1)),
            studyPlanSections = mapOf(
                0L to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(id = 0, activities = listOf(0)),
                    isExpanded = true,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                ),
                1L to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(
                        id = 1,
                        activities = listOf(1),
                        completedTopicsCount = 1,
                        topicsCount = 10
                    ),
                    isExpanded = true,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            ),
            activities = mapOf(
                0L to stubLearningActivity(id = 0, isCurrent = true),
                1L to stubLearningActivity(id = 1)
            ),
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
        assertTrue { actions.filterIsInstance<StudyPlanWidgetFeature.Action.ViewAction>().isEmpty() }

        assertClickedActivityAnalyticEvent(actions, newState.activities[activityId]!!)
    }

    @Test
    fun `Click on stage implement learning activity should navigate to stage implementation`() {
        val activityId = 0L
        val projectId = 1L
        val stageId = 2L
        val sectionId = 3L
        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlanStub(id = 0, projectId = projectId),
            activities = mapOf(
                activityId to stubLearningActivity(
                    activityId,
                    isCurrent = true,
                    type = LearningActivityType.IMPLEMENT_STAGE,
                    targetType = LearningActivityTargetType.STAGE,
                    targetId = stageId,
                    sectionId = sectionId
                )
            ),
            studyPlanSections = listOf(
                studyPlanSectionStub(
                    id = sectionId, activities = listOf(activityId), nextActivityId = activityId
                )
            ).associate {
                it.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = it,
                    isExpanded = false,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            }
        )

        val (newState, actions) = reducer.reduce(state, StudyPlanWidgetFeature.Message.ActivityClicked(activityId))

        assertEquals(state, newState)
        assertContains(
            actions, StudyPlanWidgetFeature.Action.ViewAction.NavigateTo.StageImplement(stageId, projectId)
        )

        assertClickedActivityAnalyticEvent(actions, newState.activities[activityId]!!)
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
        assertTrue { actions.filterIsInstance<StudyPlanWidgetFeature.Action.ViewAction>().isEmpty() }

        assertClickedActivityAnalyticEvent(actions, newState.activities[activityId]!!)
    }

    @Test
    fun `Click on learn topic learning activity should navigate to step screen`() {
        val activityId = 0L
        val stepId = 1L
        val sectionId = 2L
        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlanStub(id = 0),
            activities = mapOf(
                activityId to stubLearningActivity(
                    activityId,
                    isCurrent = true,
                    type = LearningActivityType.LEARN_TOPIC,
                    targetType = LearningActivityTargetType.STEP,
                    targetId = stepId,
                    sectionId = sectionId
                )
            ),
            studyPlanSections = listOf(
                studyPlanSectionStub(
                    id = sectionId, activities = listOf(activityId), nextActivityId = activityId
                )
            )
                .associate {
                    it.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                        studyPlanSection = it,
                        isExpanded = false,
                        contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                    )
                }
        )

        val (newState, actions) = reducer.reduce(state, StudyPlanWidgetFeature.Message.ActivityClicked(activityId))

        assertEquals(state, newState)

        val targetViewAction = actions.first()
        if (targetViewAction is StudyPlanWidgetFeature.Action.ViewAction.NavigateTo.StepScreen &&
            targetViewAction.stepRoute is StepRoute.Learn
        ) {
            assertEquals(targetViewAction.stepRoute.stepId, stepId)
        } else {
            fail("Unexpected action: $targetViewAction")
        }

        assertClickedActivityAnalyticEvent(actions, newState.activities[activityId]!!)
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
        assertTrue { actions.filterIsInstance<StudyPlanWidgetFeature.Action.ViewAction>().isEmpty() }

        assertClickedActivityAnalyticEvent(actions, newState.activities[activityId]!!)
    }

    @Test
    fun `Click on implement stage activity with ide required should show unsupported modal`() {
        val activityId = 0L
        val sectionId = 1L
        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlanStub(id = 0, projectId = 1L),
            activities = mapOf(
                activityId to stubLearningActivity(
                    activityId,
                    isCurrent = true,
                    type = LearningActivityType.IMPLEMENT_STAGE,
                    targetType = LearningActivityTargetType.STAGE,
                    targetId = 1L,
                    isIdeRequired = true,
                    sectionId = sectionId
                )
            ),
            studyPlanSections = listOf(
                studyPlanSectionStub(
                    id = sectionId, activities = listOf(activityId), nextActivityId = activityId
                )
            ).associate {
                it.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = it,
                    isExpanded = false,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            }
        )

        val (newState, actions) = reducer.reduce(state, StudyPlanWidgetFeature.Message.ActivityClicked(activityId))

        assertEquals(state, newState)
        assertContains(actions, StudyPlanWidgetFeature.Action.ViewAction.ShowStageImplementUnsupportedModal)

        assertClickedActivityAnalyticEvent(actions, newState.activities[activityId]!!)
    }

    @Test
    fun `Retry content loading message should trigger logging analytic event`() {
        val (_, actions) = reducer.reduce(
            StudyPlanWidgetFeature.State(),
            StudyPlanWidgetFeature.Message.RetryContentLoading
        )

        assertEquals(actions.size, 2)
        val targetAction = actions.last() as StudyPlanWidgetFeature.InternalAction.LogAnalyticEvent
        if (targetAction.analyticEvent is StudyPlanClickedRetryContentLoadingHyperskillAnalyticEvent) {
            // pass
        } else {
            fail("Unexpected action: $targetAction")
        }
    }

    @Test
    fun `Clicking on section should change section expansion state`() {
        val sectionId = 0L
        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlanStub(id = 0),
            studyPlanSections = mapOf(
                sectionId to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(id = sectionId),
                    isExpanded = false,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            )
        )

        val (newState, actions) = reducer.reduce(state, StudyPlanWidgetFeature.Message.SectionClicked(sectionId))
        assertEquals(newState.studyPlanSections[sectionId]?.isExpanded, true)

        val analyticAction = actions.last() as StudyPlanWidgetFeature.InternalAction.LogAnalyticEvent
        if (analyticAction.analyticEvent is StudyPlanClickedSectionHyperskillAnalyticEvent) {
            assertEquals(analyticAction.analyticEvent.sectionId, sectionId)
            assertEquals(analyticAction.analyticEvent.isExpanded, true)
        } else {
            fail("Unexpected action: $analyticAction")
        }
    }

    @Test
    fun `Activity with id = section next_activity_id will be next for root topics section`() {
        val nextActivityId = 0L
        val notNextActivityId = 1L
        val sectionId = 2L
        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlanStub(id = 0, projectId = 1L, sections = listOf(sectionId)),
            activities = mapOf(
                notNextActivityId to stubLearningActivity(
                    notNextActivityId,
                    type = LearningActivityType.LEARN_TOPIC,
                    targetType = LearningActivityTargetType.STEP,
                    targetId = 1L,
                    isCurrent = false,
                    sectionId = sectionId
                ),
                nextActivityId to stubLearningActivity(
                    nextActivityId,
                    type = LearningActivityType.LEARN_TOPIC,
                    targetType = LearningActivityTargetType.STEP,
                    targetId = 1L,
                    isCurrent = true,
                    sectionId = sectionId
                )
            ),
            studyPlanSections = listOf(
                studyPlanSectionStub(
                    id = sectionId,
                    activities = listOf(nextActivityId, notNextActivityId),
                    nextActivityId = nextActivityId
                )
            )
                .associate {
                    it.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                        studyPlanSection = it,
                        isExpanded = true,
                        contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                    )
                },
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
        )

        val viewState = studyPlanWidgetViewStateMapper.map(state)

        val viewSectionItems = (
            (viewState as? StudyPlanWidgetViewState.Content)
                ?.sections
                ?.firstOrNull()
                ?.content as? StudyPlanWidgetViewState.SectionContent.Content
            )?.sectionItems ?: fail("Unexpected view state: $viewState")

        assertEquals(
            StudyPlanWidgetViewState.SectionItemState.NEXT,
            viewSectionItems.first { it.id == nextActivityId }.state
        )
        assertEquals(
            StudyPlanWidgetViewState.SectionItemState.LOCKED,
            viewSectionItems.first { it.id == notNextActivityId }.state
        )
    }

    @Test
    fun `First activity will be next for not root topics section`() {
        val firstActivityId = 0L
        val secondActivityId = 1L
        val sectionId = 2L
        val state = StudyPlanWidgetFeature.State(
            studyPlan = studyPlanStub(id = 0, projectId = 1L, sections = listOf(sectionId)),
            activities = mapOf(
                firstActivityId to stubLearningActivity(
                    firstActivityId,
                    type = LearningActivityType.LEARN_TOPIC,
                    targetType = LearningActivityTargetType.STEP,
                    targetId = 1L,
                    isCurrent = false,
                    sectionId = sectionId
                ),
                secondActivityId to stubLearningActivity(
                    secondActivityId,
                    type = LearningActivityType.LEARN_TOPIC,
                    targetType = LearningActivityTargetType.STEP,
                    targetId = 1L,
                    isCurrent = true,
                    sectionId = sectionId
                )
            ),
            studyPlanSections = listOf(
                studyPlanSectionStub(
                    id = sectionId,
                    activities = listOf(firstActivityId, secondActivityId),
                    nextActivityId = null
                )
            )
                .associate {
                    it.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                        studyPlanSection = it,
                        isExpanded = true,
                        contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                    )
                },
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
        )

        val viewState = studyPlanWidgetViewStateMapper.map(state)

        val viewSectionItems = (
            (viewState as? StudyPlanWidgetViewState.Content)
                ?.sections
                ?.firstOrNull()
                ?.content as? StudyPlanWidgetViewState.SectionContent.Content
            )?.sectionItems ?: fail("Unexpected view state: $viewState")

        assertEquals(
            StudyPlanWidgetViewState.SectionItemState.NEXT,
            viewSectionItems.first { it.id == firstActivityId }.state
        )
        assertEquals(
            StudyPlanWidgetViewState.SectionItemState.LOCKED,
            viewSectionItems.first { it.id == secondActivityId }.state
        )
    }

    private fun sectionViewState(
        section: StudyPlanSection,
        content: StudyPlanWidgetViewState.SectionContent,
        formattedTopicsCount: String? = null,
        formattedTimeToComplete: String? = null
    ) =
        StudyPlanWidgetViewState.Section(
            id = section.id,
            title = section.title,
            subtitle = section.subtitle.takeIf { it.isNotEmpty() },
            content = content,
            formattedTopicsCount = formattedTopicsCount,
            formattedTimeToComplete = formattedTimeToComplete
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
        type: StudyPlanSectionType = StudyPlanSectionType.STAGE,
        isVisible: Boolean = true,
        topicsCount: Int = 0,
        completedTopicsCount: Int = 0,
        secondsToComplete: Float = 0f,
        activities: List<Long> = emptyList(),
        nextActivityId: Long? = null
    ) =
        StudyPlanSection(
            id = id,
            studyPlanId = 0,
            typeValue = type.value,
            targetId = 0,
            targetType = "",
            nextActivityId = nextActivityId,
            isVisible = isVisible,
            title = "",
            subtitle = "",
            topicsCount = topicsCount,
            completedTopicsCount = completedTopicsCount,
            secondsToComplete = secondsToComplete,
            activities = activities
        )

    private fun stubLearningActivity(
        id: Long,
        state: LearningActivityState = LearningActivityState.TODO,
        targetId: Long = 0L,
        type: LearningActivityType = LearningActivityType.LEARN_TOPIC,
        targetType: LearningActivityTargetType = LearningActivityTargetType.STEP,
        isCurrent: Boolean = false,
        title: String = "",
        isIdeRequired: Boolean = false,
        sectionId: Long? = null
    ) =
        LearningActivity(
            id = id,
            stateValue = state.value,
            targetId = targetId,
            typeValue = type.value,
            isCurrent = isCurrent,
            targetTypeValue = targetType.value,
            title = title,
            isIdeRequired = isIdeRequired,
            sectionId = sectionId
        )

    private fun studyPlanSectionItemStub(
        activityId: Long,
        title: String = "",
        state: StudyPlanWidgetViewState.SectionItemState = StudyPlanWidgetViewState.SectionItemState.LOCKED,
        isIdeRequired: Boolean = false
    ) =
        StudyPlanWidgetViewState.SectionItem(
            id = activityId,
            title = title.ifBlank { activityId.toString() },
            formattedProgress = null,
            progress = null,
            state = state,
            isIdeRequired = isIdeRequired,
            hypercoinsAward = null
        )

    private fun assertClickedActivityAnalyticEvent(
        actions: Set<StudyPlanWidgetFeature.Action>,
        activity: LearningActivity
    ) {
        val analyticAction = actions.last() as StudyPlanWidgetFeature.InternalAction.LogAnalyticEvent
        if (analyticAction.analyticEvent is StudyPlanClickedActivityHyperskillAnalyticEvent) {
            assertEquals(activity.id, analyticAction.analyticEvent.activityId)
            assertEquals(activity.type?.value, analyticAction.analyticEvent.activityType)
            assertEquals(activity.targetType?.value, analyticAction.analyticEvent.activityTargetType)
            assertEquals(activity.targetId, analyticAction.analyticEvent.activityTargetId)
        } else {
            fail("Expected StudyPlanClickedActivityHyperskillAnalyticEvent")
        }
    }
}