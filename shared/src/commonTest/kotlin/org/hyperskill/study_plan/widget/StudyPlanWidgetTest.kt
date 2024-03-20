package org.hyperskill.study_plan.widget

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityTargetType
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.learning_activities.presentation.model.LearningActivityTargetViewAction
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedActivityHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedRetryActivitiesLoadingHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedSectionHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import org.hyperskill.app.study_plan.widget.view.mapper.StudyPlanWidgetViewStateMapper
import org.hyperskill.app.study_plan.widget.view.model.StudyPlanWidgetViewState
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionStatus
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType
import org.hyperskill.profile.stub
import org.hyperskill.subscriptions.stub

class StudyPlanWidgetTest {

    private val reducer = StudyPlanWidgetReducer()

    private val resourceProviderStub = ResourceProviderStub()

    private val studyPlanWidgetViewStateMapper = StudyPlanWidgetViewStateMapper(
        SharedDateFormatter(resourceProviderStub)
    )

    @Test
    fun `Initialize message should trigger learning activities with sections fetching`() {
        val initialState = StudyPlanWidgetFeature.State()
        val (state, actions) = reducer.reduce(initialState, StudyPlanWidgetFeature.InternalMessage.Initialize())
        assertContains(actions, StudyPlanWidgetFeature.InternalAction.FetchLearningActivitiesWithSections())
        assertEquals(state.sectionsStatus, StudyPlanWidgetFeature.ContentStatus.LOADING)
    }

    @Test
    fun `Sections status should be Loaded after loading finish`() {
        val (state, _) = reducer.reduce(
            StudyPlanWidgetFeature.State(),
            StudyPlanWidgetFeature.LearningActivitiesWithSectionsFetchResult.Success(
                learningActivities = emptyList(),
                studyPlanSections = emptyList(),
                subscription = Subscription.stub()
            )
        )
        assertEquals(StudyPlanWidgetFeature.ContentStatus.LOADED, state.sectionsStatus)
    }

    @Test
    fun `Loaded sections should be filtered by visibility`() {
        val hiddenSection = studyPlanSectionStub(id = 1, isVisible = false)
        val visibleSection = studyPlanSectionStub(
            id = 2,
            isVisible = true,
            type = StudyPlanSectionType.ROOT_TOPICS,
            activities = listOf(1L)
        )

        val (state, _) = reducer.reduce(
            StudyPlanWidgetFeature.State(),
            StudyPlanWidgetFeature.LearningActivitiesWithSectionsFetchResult.Success(
                learningActivities = listOf(
                    stubLearningActivity(id = 1L)
                ),
                studyPlanSections = listOf(
                    hiddenSection,
                    visibleSection
                ),
                subscription = Subscription.stub()
            )
        )

        assertEquals(1, state.studyPlanSections.size)
        assertEquals(visibleSection, state.studyPlanSections[visibleSection.id]?.studyPlanSection)

        assertEquals(null, state.studyPlanSections[hiddenSection.id])
    }

    @Test
    fun `Loaded visible sections should be removed if their position precedes current section`() {
        val visibleSection = studyPlanSectionStub(
            id = 0,
            isVisible = true,
            type = StudyPlanSectionType.STAGE
        )
        val currentSection = studyPlanSectionStub(
            id = 1,
            isVisible = true,
            type = StudyPlanSectionType.STAGE,
            activities = listOf(1L)
        )

        val (state, _) = reducer.reduce(
            StudyPlanWidgetFeature.State(),
            StudyPlanWidgetFeature.LearningActivitiesWithSectionsFetchResult.Success(
                learningActivities = listOf(stubLearningActivity(id = 1L)),
                studyPlanSections = listOf(visibleSection, currentSection),
                subscription = Subscription.stub()
            )
        )

        assertEquals(1, state.studyPlanSections.size)
        assertEquals(currentSection, state.studyPlanSections[currentSection.id]?.studyPlanSection)

        assertEquals(null, state.studyPlanSections[visibleSection.id])
    }

    @Test
    fun `Next project sections should be removed for freemium users`() {
        val freemiumSubscription = Subscription.stub(type = SubscriptionType.FREEMIUM)
        val expiredMobileOnlySubscription = Subscription.stub(
            type = SubscriptionType.MOBILE_ONLY,
            status = SubscriptionStatus.EXPIRED
        )

        listOf(freemiumSubscription, expiredMobileOnlySubscription).forEach { subscription ->
            val (state, _) = reducer.reduce(
                StudyPlanWidgetFeature.State(),
                StudyPlanWidgetFeature.LearningActivitiesWithSectionsFetchResult.Success(
                    learningActivities = listOf(stubLearningActivity(id = 1L)),
                    studyPlanSections = listOf(
                        studyPlanSectionStub(
                            id = 1,
                            isVisible = true,
                            type = StudyPlanSectionType.STAGE,
                            activities = listOf(1L)
                        ),
                        studyPlanSectionStub(
                            id = 2,
                            isVisible = true,
                            type = StudyPlanSectionType.NEXT_PROJECT
                        )
                    ),
                    subscription = subscription
                )
            )
            assertTrue {
                state.studyPlanSections.values.none { it.studyPlanSection.type == StudyPlanSectionType.NEXT_PROJECT }
            }
        }
    }

    @Test
    fun `Study plan sections should be empty if loaded sections does not contains current section`() {
        val expectedState = StudyPlanWidgetFeature.State(
            studyPlanSections = emptyMap(),
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
            isRefreshing = false
        )

        val (state, _) = reducer.reduce(
            StudyPlanWidgetFeature.State(),
            StudyPlanWidgetFeature.LearningActivitiesWithSectionsFetchResult.Success(
                learningActivities = emptyList(),
                studyPlanSections = listOf(
                    studyPlanSectionStub(id = 0),
                    studyPlanSectionStub(id = 1, activities = listOf(1))
                ),
                subscription = Subscription.stub()
            )
        )

        assertEquals(expectedState, state)
    }

    @Test
    fun `Sections in ViewState should be sorted by backend order`() {
        val expectedSectionsIds = listOf<Long>(3, 5, 2, 1, 4)
        val expectedViewState = StudyPlanWidgetViewState.Content(
            expectedSectionsIds.mapIndexed { index, sectionId ->
                sectionViewState(
                    section = studyPlanSectionStub(
                        id = sectionId,
                        activities = if (index == 0) listOf(1L) else emptyList()
                    ),
                    content = if (index > 0) {
                        StudyPlanWidgetViewState.SectionContent.Collapsed
                    } else {
                        StudyPlanWidgetViewState.SectionContent.Content(
                            listOf(
                                studyPlanSectionItemStub(
                                    activityId = 1,
                                    state = StudyPlanWidgetViewState.SectionItemState.NEXT
                                )
                            )
                        )
                    },
                    isCurrent = sectionId == expectedSectionsIds[0]
                )
            }
        )

        val fetchedSectionsIds = listOf<Long>(3, 5, 2, 1, 4)

        val (state, _) = reducer.reduce(
            StudyPlanWidgetFeature.State(),
            StudyPlanWidgetFeature.LearningActivitiesWithSectionsFetchResult.Success(
                learningActivities = listOf(
                    stubLearningActivity(id = 1L)
                ),
                studyPlanSections = fetchedSectionsIds.mapIndexed { index, sectionId ->
                    studyPlanSectionStub(
                        id = sectionId,
                        activities = if (index == 0) listOf(1L) else emptyList()
                    )
                },
                subscription = Subscription.stub()
            )
        )

        val viewState = studyPlanWidgetViewStateMapper.map(state)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun `First section should be expanded and its activities should be loaded`() {
        val firstSection = studyPlanSectionStub(id = 0, activities = listOf(0, 1, 2))
        val secondSection = studyPlanSectionStub(id = 1)

        val (state, actions) = reducer.reduce(
            StudyPlanWidgetFeature.State(),
            StudyPlanWidgetFeature.LearningActivitiesWithSectionsFetchResult.Success(
                learningActivities = listOf(
                    stubLearningActivity(id = 0),
                    stubLearningActivity(id = 1),
                    stubLearningActivity(id = 2)
                ),
                studyPlanSections = listOf(firstSection, secondSection),
                subscription = Subscription.stub()
            )
        )

        assertTrue {
            actions.filterIsInstance<StudyPlanWidgetFeature.InternalAction.FetchLearningActivities>().isEmpty()
        }

        val actualFirstSection = state.studyPlanSections[firstSection.id]
        assertEquals(StudyPlanWidgetFeature.ContentStatus.LOADED, actualFirstSection?.contentStatus)
        assertEquals(true, actualFirstSection?.isExpanded)
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
                StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success(
                    sectionId = sectionId,
                    activities = listOf(stubLearningActivity(1L))
                )
            )
        assertEquals(StudyPlanWidgetFeature.ContentStatus.LOADED, state.studyPlanSections[sectionId]?.contentStatus)
    }

    @Test
    fun `Current section should be removed if no available activities loaded and next section should be expanded`() {
        val currentSectionId = 0L
        val nextSectionId = 1L
        val initialState = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                currentSectionId to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(currentSectionId),
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADING,
                    isExpanded = true
                ),
                nextSectionId to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(nextSectionId),
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.IDLE,
                    isExpanded = false
                )
            )
        )
        val (state, _) =
            reducer.reduce(
                initialState,
                StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success(sectionId = currentSectionId, emptyList())
            )
        assertTrue(state.studyPlanSections.containsKey(currentSectionId).not())
        val nextSection = state.studyPlanSections[nextSectionId]
        assertTrue(nextSection?.isExpanded == true)
        assertEquals(StudyPlanWidgetFeature.ContentStatus.LOADING, nextSection?.contentStatus)
    }

    @Test
    fun `Not current section should be removed if no available activities loaded`() {
        val currentSectionId = 0L
        val notCurrent = 1L
        val initialState = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                currentSectionId to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(currentSectionId),
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
                    isExpanded = true
                ),
                notCurrent to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(notCurrent),
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADING,
                    isExpanded = false
                )
            )
        )
        val (state, _) =
            reducer.reduce(
                initialState,
                StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success(sectionId = notCurrent, emptyList())
            )
        assertTrue(state.studyPlanSections.containsKey(notCurrent).not())
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
    fun `Successfully loaded activities for current section should update next learning activity state`() {
        val sectionId = 0L
        val activities = List(2) { index ->
            stubLearningActivity(id = index.toLong())
        }

        val initialState = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                sectionId to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(
                        sectionId,
                        activities = activities.map { it.id }
                    ),
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADING,
                    isExpanded = true
                )
            )
        )

        val expectedActivity = activities.first()

        val (_, resultActions) =
            reducer.reduce(
                initialState,
                StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success(sectionId, activities)
            )

        assertContains(
            resultActions,
            StudyPlanWidgetFeature.InternalAction.UpdateNextLearningActivityState(expectedActivity)
        )
    }

    @Test
    fun `New activities should replace old activities in activities map`() {
        // ALTAPPS-743: old activities [0, 1], new activities [1, 2], result activities [1, 2]
        val sectionId = 0L
        val oldActivities = List(2) { index ->
            stubLearningActivity(id = index.toLong())
        }
        val newActivities = List(2) { index ->
            stubLearningActivity(id = index.toLong() + 1)
        }

        val expectedActivitiesIds: Set<Long> = setOf(1, 2)

        val (state, _) =
            reducer.reduce(
                StudyPlanWidgetFeature.State(
                    activities = oldActivities.associateBy { it.id },
                    studyPlanSections = mapOf(
                        sectionId to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                            studyPlanSection = studyPlanSectionStub(
                                sectionId,
                                activities = oldActivities.map { it.id }
                            ),
                            contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
                            isExpanded = true
                        )
                    )
                ),
                StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success(sectionId, newActivities)
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
            StudyPlanWidgetFeature.InternalAction.FetchLearningActivities(
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
            contentStatus = StudyPlanWidgetFeature.ContentStatus.IDLE,
            isExpanded = false
        )

        listOf(collapsedSection, idleSection).forEach { givenSection ->
            val state = StudyPlanWidgetFeature.State(
                studyPlanSections = mapOf(sectionId to givenSection),
                sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
            )

            val viewState = studyPlanWidgetViewStateMapper.map(state)

            val expectedViewStateSections = StudyPlanWidgetViewState.Content(
                sections = listOf(
                    sectionViewState(
                        section = section,
                        content = StudyPlanWidgetViewState.SectionContent.Collapsed,
                        isCurrent = true
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
                        listOf(
                            studyPlanSectionItemStub(
                                activityId,
                                state = StudyPlanWidgetViewState.SectionItemState.NEXT
                            )
                        )
                    ),
                    isCurrent = true
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
                        listOf(
                            studyPlanSectionItemStub(
                                activityId,
                                state = StudyPlanWidgetViewState.SectionItemState.NEXT
                            )
                        )
                    ),
                    isCurrent = true
                )
            )
        )

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun `Reload content in background should trigger fetch learning activities with sections`() {
        val state = StudyPlanWidgetFeature.State()
        val (_, actions) = reducer.reduce(state, StudyPlanWidgetFeature.InternalMessage.ReloadContentInBackground)
        assertContains(actions, StudyPlanWidgetFeature.InternalAction.FetchLearningActivitiesWithSections())
    }

    @Test
    fun `Reload content in background should persist content status for current section`() {
        val sectionId = 0L
        val section = StudyPlanWidgetFeature.StudyPlanSectionInfo(
            studyPlanSection = studyPlanSectionStub(sectionId),
            contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
            isExpanded = true
        )
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(sectionId to section),
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
        )

        val (newState, actions) =
            reducer.reduce(state, StudyPlanWidgetFeature.InternalMessage.ReloadContentInBackground)

        assertEquals(
            state.studyPlanSections[sectionId]?.contentStatus,
            newState.studyPlanSections[sectionId]?.contentStatus
        )
        assertContains(actions, StudyPlanWidgetFeature.InternalAction.FetchLearningActivitiesWithSections())
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
                                state = StudyPlanWidgetViewState.SectionItemState.NEXT
                            )
                        )
                    ),
                    isCurrent = true
                )
            )
        )

        val state = StudyPlanWidgetFeature.State(
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
                                state = StudyPlanWidgetViewState.SectionItemState.NEXT
                            )
                        )
                    ),
                    isCurrent = true
                )
            )
        )

        val state = StudyPlanWidgetFeature.State(
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
    fun `Section content item title in ViewState should be equal to learning activity description`() {
        val expectedViewState = StudyPlanWidgetViewState.Content(
            listOf(
                sectionViewState(
                    section = studyPlanSectionStub(id = 0, activities = listOf(0)),
                    content = StudyPlanWidgetViewState.SectionContent.Content(
                        sectionItems = listOf(
                            studyPlanSectionItemStub(
                                activityId = 0,
                                title = "Work on project. Stage: 1/6",
                                subtitle = "Hello, coffee!",
                                state = StudyPlanWidgetViewState.SectionItemState.NEXT
                            )
                        )
                    ),
                    isCurrent = true
                )
            )
        )

        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                0L to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(id = 0, activities = listOf(0)),
                    isExpanded = true,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            ),
            activities = mapOf(
                0L to stubLearningActivity(
                    id = 0,
                    title = "Hello, coffee!",
                    description = "Work on project. Stage: 1/6"
                )
            ),
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
        )

        val viewState = studyPlanWidgetViewStateMapper.map(state)
        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun `Section content statistics in ViewState should be always visible for first visible section`() {
        fun makeState(isExpanded: Boolean): StudyPlanWidgetFeature.State =
            StudyPlanWidgetFeature.State(
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
                    isCurrent = true,
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
                    ),
                    isCurrent = true
                ),
                sectionViewState(
                    section = studyPlanSectionStub(id = 1),
                    content = StudyPlanWidgetViewState.SectionContent.Content(
                        listOf(
                            studyPlanSectionItemStub(
                                activityId = 1,
                                state = StudyPlanWidgetViewState.SectionItemState.IDLE
                            )
                        )
                    ),
                    isCurrent = false,
                    formattedTopicsCount = "1 / 10",
                    formattedTimeToComplete = null // TODO: add DateFormatter stub and test this
                )
            )
        )

        val state = StudyPlanWidgetFeature.State(
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
                0L to stubLearningActivity(id = 0),
                1L to stubLearningActivity(id = 1)
            ),
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
        )

        val viewState = studyPlanWidgetViewStateMapper.map(state)
        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun `Click on stage implement learning activity should navigate to stage implementation`() {
        val activityId = 0L
        val projectId = 1L
        val stageId = 2L
        val sectionId = 3L

        val state = StudyPlanWidgetFeature.State(
            profile = Profile.stub(projectId = projectId),
            studyPlanSections = mapOf(
                sectionId to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(id = sectionId, activities = listOf(activityId)),
                    isExpanded = true,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            ),
            activities = mapOf(
                activityId to stubLearningActivity(
                    activityId,
                    type = LearningActivityType.IMPLEMENT_STAGE,
                    targetType = LearningActivityTargetType.STAGE,
                    targetId = stageId
                )
            )
        )

        val (newState, actions) = reducer.reduce(state, StudyPlanWidgetFeature.Message.ActivityClicked(activityId))

        assertEquals(state, newState)
        assertContains(
            actions,
            StudyPlanWidgetFeature.Action.ViewAction.NavigateTo.LearningActivityTarget(
                LearningActivityTargetViewAction.NavigateTo.StageImplement(stageId = stageId, projectId = projectId)
            )
        )

        assertClickedActivityAnalyticEvent(actions, newState.activities[activityId]!!)
    }

    @Test
    fun `Click on stage implement learning activity with non stage target should do nothing`() {
        val activityId = 0L
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                0L to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(id = 0, activities = listOf(activityId)),
                    isExpanded = true,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            ),
            activities = mapOf(
                activityId to stubLearningActivity(
                    activityId,
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
            studyPlanSections = mapOf(
                sectionId to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(id = sectionId, activities = listOf(activityId)),
                    isExpanded = true,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            ),
            activities = mapOf(
                activityId to stubLearningActivity(
                    activityId,
                    type = LearningActivityType.LEARN_TOPIC,
                    targetType = LearningActivityTargetType.STEP,
                    targetId = stepId
                )
            )
        )

        val (newState, actions) = reducer.reduce(state, StudyPlanWidgetFeature.Message.ActivityClicked(activityId))

        assertEquals(state, newState)

        when (val targetViewAction = actions.first()) {
            is StudyPlanWidgetFeature.Action.ViewAction.NavigateTo.LearningActivityTarget -> {
                when (val activityViewAction = targetViewAction.viewAction) {
                    is LearningActivityTargetViewAction.NavigateTo.Step -> {
                        assertEquals(activityViewAction.stepRoute, StepRoute.Learn.Step(stepId))
                    }
                    else -> {
                        fail("Unexpected action: $activityViewAction")
                    }
                }
            }
            else -> {
                fail("Unexpected action: $targetViewAction")
            }
        }

        assertClickedActivityAnalyticEvent(actions, newState.activities[activityId]!!)
    }

    @Test
    fun `Click on learn topic learning activity with non step target should do nothing`() {
        val activityId = 0L
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                0L to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(id = 0, activities = listOf(activityId)),
                    isExpanded = true,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            ),
            activities = mapOf(
                activityId to stubLearningActivity(
                    activityId,
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
            profile = Profile.stub(projectId = 1),
            studyPlanSections = mapOf(
                sectionId to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(id = sectionId, activities = listOf(activityId)),
                    isExpanded = true,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            ),
            activities = mapOf(
                activityId to stubLearningActivity(
                    activityId,
                    type = LearningActivityType.IMPLEMENT_STAGE,
                    targetType = LearningActivityTargetType.STAGE,
                    targetId = 1L,
                    isIdeRequired = true
                )
            )
        )

        val (newState, actions) = reducer.reduce(state, StudyPlanWidgetFeature.Message.ActivityClicked(activityId))

        assertEquals(state, newState)
        assertContains(
            actions,
            StudyPlanWidgetFeature.Action.ViewAction.NavigateTo.LearningActivityTarget(
                LearningActivityTargetViewAction.ShowStageImplementIDERequiredModal
            )
        )

        assertClickedActivityAnalyticEvent(actions, newState.activities[activityId]!!)
    }

    @Test
    fun `Click on select project learning activity should navigate to select project`() {
        val activityId = 0L
        val sectionId = 1L
        val trackId = 2L
        val state = StudyPlanWidgetFeature.State(
            profile = Profile.stub(trackId = trackId),
            studyPlanSections = mapOf(
                sectionId to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(id = sectionId, activities = listOf(activityId)),
                    isExpanded = true,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            ),
            activities = mapOf(
                activityId to stubLearningActivity(activityId, type = LearningActivityType.SELECT_PROJECT)
            )
        )

        val (newState, actions) = reducer.reduce(state, StudyPlanWidgetFeature.Message.ActivityClicked(activityId))

        assertEquals(state, newState)
        assertContains(
            actions,
            StudyPlanWidgetFeature.Action.ViewAction.NavigateTo.LearningActivityTarget(
                LearningActivityTargetViewAction.NavigateTo.SelectProject(trackId = trackId)
            )
        )
        assertClickedActivityAnalyticEvent(actions, newState.activities[activityId]!!)
    }

    @Test
    fun `Click on select track learning activity should navigate to select track`() {
        val activityId = 0L
        val sectionId = 1L
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                sectionId to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(id = sectionId, activities = listOf(activityId)),
                    isExpanded = true,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            ),
            activities = mapOf(
                activityId to stubLearningActivity(activityId, type = LearningActivityType.SELECT_TRACK)
            )
        )

        val (newState, actions) = reducer.reduce(state, StudyPlanWidgetFeature.Message.ActivityClicked(activityId))

        assertEquals(state, newState)
        assertContains(
            actions,
            StudyPlanWidgetFeature.Action.ViewAction.NavigateTo.LearningActivityTarget(
                LearningActivityTargetViewAction.NavigateTo.SelectTrack
            )
        )
        assertClickedActivityAnalyticEvent(actions, newState.activities[activityId]!!)
    }

    @Test
    fun `Clicking on section should change section expansion state`() {
        val sectionId = 0L
        val state = StudyPlanWidgetFeature.State(
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
    fun `Get paginated activities ids in non root topics section`() {
        val expectedActivitiesIds = listOf(1L, 2L, 3L, 4L, 5L)
        val section = StudyPlanWidgetFeature.StudyPlanSectionInfo(
            studyPlanSection = studyPlanSectionStub(
                id = 0,
                type = StudyPlanSectionType.STAGE,
                activities = expectedActivitiesIds
            ),
            isExpanded = false,
            contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
        )

        assertEquals(
            expectedActivitiesIds,
            reducer.getPaginatedActivitiesIds(section, isLearningPathDividedTrackTopicsEnabled = false)
        )
    }

    @Test
    fun `Get paginated activities ids in empty root topics section`() {
        val expectedActivitiesIds = emptyList<Long>()
        val section = StudyPlanWidgetFeature.StudyPlanSectionInfo(
            studyPlanSection = studyPlanSectionStub(
                id = 0,
                type = StudyPlanSectionType.ROOT_TOPICS,
                activities = expectedActivitiesIds
            ),
            isExpanded = false,
            contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
        )

        assertEquals(
            expectedActivitiesIds,
            reducer.getPaginatedActivitiesIds(section, isLearningPathDividedTrackTopicsEnabled = false)
        )
    }

    @Test
    fun `Get paginated activities ids in root topics section when end index greater than size`() {
        val expectedActivitiesIds = listOf(3L, 4L, 5L)
        val section = StudyPlanWidgetFeature.StudyPlanSectionInfo(
            studyPlanSection = studyPlanSectionStub(
                id = 0,
                type = StudyPlanSectionType.ROOT_TOPICS,
                activities = (0L..5L).toList(),
                nextActivityId = 3L
            ),
            isExpanded = false,
            contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
        )

        assertEquals(
            expectedActivitiesIds,
            reducer.getPaginatedActivitiesIds(section, isLearningPathDividedTrackTopicsEnabled = false)
        )
    }

    @Test
    fun `Get paginated activities ids in root topics section returns correct result page size`() {
        val expectedActivitiesIds = (5L..14L).toList()
        val section = StudyPlanWidgetFeature.StudyPlanSectionInfo(
            studyPlanSection = studyPlanSectionStub(
                id = 0,
                type = StudyPlanSectionType.ROOT_TOPICS,
                activities = (0L..100L).toList(),
                nextActivityId = 5L
            ),
            isExpanded = false,
            contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
        )

        assertEquals(
            expectedActivitiesIds,
            reducer.getPaginatedActivitiesIds(section, isLearningPathDividedTrackTopicsEnabled = false)
        )
    }

    @Test
    fun `Get paginated activities ids in root topics section when activities not contains next activity id`() {
        val expectedActivitiesIds = (0L..4L).toList()
        val section = StudyPlanWidgetFeature.StudyPlanSectionInfo(
            studyPlanSection = studyPlanSectionStub(
                id = 0,
                type = StudyPlanSectionType.ROOT_TOPICS,
                activities = expectedActivitiesIds,
                nextActivityId = 10L
            ),
            isExpanded = false,
            contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
        )

        assertEquals(
            expectedActivitiesIds,
            reducer.getPaginatedActivitiesIds(section, isLearningPathDividedTrackTopicsEnabled = false)
        )
    }

    @Test
    fun `Get not paginated activities ids in root topics section when DividedTrackTopics feature enabled`() {
        val expectedActivitiesIds = (0L..50L).toList()
        val section = StudyPlanWidgetFeature.StudyPlanSectionInfo(
            studyPlanSection = studyPlanSectionStub(
                id = 0,
                type = StudyPlanSectionType.ROOT_TOPICS,
                activities = expectedActivitiesIds,
                nextActivityId = 5L
            ),
            isExpanded = false,
            contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
        )

        assertEquals(
            expectedActivitiesIds,
            reducer.getPaginatedActivitiesIds(section, isLearningPathDividedTrackTopicsEnabled = true)
        )
    }

    @Test
    fun `Activity with id section next_activity_id in ViewState will be next for root topics section`() {
        val nextActivityId = 0L
        val notNextActivityId = 1L
        val sectionId = 2L
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                sectionId to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(
                        id = sectionId,
                        activities = listOf(nextActivityId, notNextActivityId),
                        nextActivityId = nextActivityId
                    ),
                    isExpanded = true,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            ),
            activities = mapOf(
                notNextActivityId to stubLearningActivity(notNextActivityId),
                nextActivityId to stubLearningActivity(nextActivityId)
            ),
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
            StudyPlanWidgetViewState.SectionItemState.IDLE,
            viewSectionItems.first { it.id == notNextActivityId }.state
        )
    }

    @Test
    fun `First activity in ViewState will be next for not root topics section`() {
        val firstActivityId = 0L
        val secondActivityId = 1L
        val sectionId = 2L
        val state = StudyPlanWidgetFeature.State(
            studyPlanSections = mapOf(
                sectionId to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                    studyPlanSection = studyPlanSectionStub(
                        id = sectionId,
                        activities = listOf(firstActivityId, secondActivityId),
                        nextActivityId = null
                    ),
                    isExpanded = true,
                    contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
                )
            ),
            activities = mapOf(
                firstActivityId to stubLearningActivity(firstActivityId),
                secondActivityId to stubLearningActivity(secondActivityId)
            ),
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
            StudyPlanWidgetViewState.SectionItemState.IDLE,
            viewSectionItems.first { it.id == secondActivityId }.state
        )
    }

    private fun sectionViewState(
        section: StudyPlanSection,
        content: StudyPlanWidgetViewState.SectionContent,
        isCurrent: Boolean = false,
        formattedTopicsCount: String? = null,
        formattedTimeToComplete: String? = null
    ) =
        StudyPlanWidgetViewState.Section(
            id = section.id,
            title = section.title,
            subtitle = section.subtitle.takeIf { it.isNotEmpty() },
            isCurrent = isCurrent,
            content = content,
            formattedTopicsCount = formattedTopicsCount,
            formattedTimeToComplete = formattedTimeToComplete
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
            typeValue = type.value,
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
        title: String = "",
        description: String? = null,
        isIdeRequired: Boolean = false,
        topicId: Long? = null
    ) =
        LearningActivity(
            id = id,
            stateValue = state.value,
            targetId = targetId,
            typeValue = type.value,
            targetTypeValue = targetType.value,
            title = title,
            description = description,
            isIdeRequired = isIdeRequired,
            topicId = topicId
        )

    private fun studyPlanSectionItemStub(
        activityId: Long,
        title: String = "",
        subtitle: String? = null,
        state: StudyPlanWidgetViewState.SectionItemState = StudyPlanWidgetViewState.SectionItemState.IDLE,
        isIdeRequired: Boolean = false
    ) =
        StudyPlanWidgetViewState.SectionItem(
            id = activityId,
            title = title.ifBlank { activityId.toString() },
            subtitle = subtitle,
            formattedProgress = null,
            progress = 0,
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