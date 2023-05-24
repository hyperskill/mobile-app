package org.hyperskill.projects_selection

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.core.view.mapper.NumbersFormatter
import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.project_selection.list.domain.analytic.ProjectSelectionListClickedProjectHyperskillAnalyticsEvent
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.Action
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.ContentState
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.InternalAction
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.Message
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.State
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.ViewState
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListReducer
import org.hyperskill.app.project_selection.list.presentation.bestRatedProjectId
import org.hyperskill.app.project_selection.list.presentation.fastestToCompleteProjectId
import org.hyperskill.app.project_selection.list.presentation.recommendedProjects
import org.hyperskill.app.project_selection.list.view.mapper.ProjectSelectionListViewStateMapper
import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.model.ProjectKind
import org.hyperskill.app.projects.domain.model.ProjectLevel
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.projects.domain.model.ProjectTracksEntry
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.projects.domain.model.isGraduate
import org.hyperskill.app.projects.domain.model.sortByScore
import org.hyperskill.app.track.domain.model.ProjectsByLevel
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.profile.stub
import org.hyperskill.track.stub
import ru.nobird.app.core.model.safeCast

class ProjectsListTest {

    private val projectSelectionListReducer = ProjectSelectionListReducer()

    private val resourceProvider = ResourceProviderStub()

    private val viewStateMapper = ProjectSelectionListViewStateMapper(
        resourceProvider = resourceProvider,
        numbersFormatter = NumbersFormatter(resourceProvider),
        dateFormatter = SharedDateFormatter(resourceProvider)
    )

    @Test
    fun `Initialize message should trigger content loading`() {
        val trackId = 0L
        val (state, actions) = projectSelectionListReducer.reduce(
            ProjectSelectionListFeature.initialState(trackId),
            Message.Initialize
        )
        assertContains(
            actions,
            InternalAction.FetchContent(trackId, false)
        )
        assertEquals(
            ContentState.Loading,
            state.content
        )
    }

    @Test
    fun `FetchContent success message should update content`() {
        val trackId = 0L
        val track = Track.stub(trackId)
        val currentProjectId = 3L

        /**
         * Order is important here because of sorting.
         * @see sortByScore
         * @see ProjectSelectionListFeature.ContentFetchResult.Success
         * @see ProjectSelectionListReducer
         * @see ProjectSelectionListFeature.ContentState.Content.sortedProjectsIds
         */
        val projects = listOf(5L, 4L, 3L, 2L, 1L, 0L)

        val (state, actions) = projectSelectionListReducer.reduce(
            State(trackId, ContentState.Loading),
            ProjectSelectionListFeature.ContentFetchResult.Success(
                track = track,
                projects = projects.map(ProjectWithProgress.Companion::stub),
                currentProjectId = currentProjectId,
                profile = Profile.stub()
            )
        )
        assertTrue(actions.isEmpty())

        val expectedContent = ContentState.Content(
            track = track,
            projects = projects.associateWith(ProjectWithProgress.Companion::stub),
            currentProjectId = currentProjectId,
            sortedProjectsIds = projects
        )
        assertEquals(
            expectedContent,
            state.content
        )
    }

    @Test
    fun `RetryContentLoading message should trigger force content loading`() {
        val trackId = 0L
        val (state, actions) = projectSelectionListReducer.reduce(
            State(
                trackId,
                ContentState.Error
            ),
            Message.RetryContentLoading
        )
        assertContains(
            actions,
            InternalAction.FetchContent(trackId, forceLoadFromNetwork = true)
        )
        assertEquals(
            ContentState.Loading,
            state.content
        )
    }

    @Test
    fun `RetryContentLoading should not be handled in non Error state`() {
        listOf(
            ContentState.Loading,
            ContentState.Content(
                track = Track.stub(0L),
                projects = emptyMap(),
                currentProjectId = null,
                sortedProjectsIds = emptyList()
            ),
            ContentState.Idle
        ).forEach { contentState ->
            val (state, actions) = projectSelectionListReducer.reduce(
                State(
                    trackId = 0L,
                    content = contentState
                ),
                Message.RetryContentLoading
            )
            assertTrue(actions.isEmpty())
            assertEquals(contentState, state.content)
        }
    }

    @Test
    fun `ProjectClicked message on project navigates to the project details screen`() {
        val trackId = 0L
        val projectId = 1L
        val projectWithProgress = ProjectWithProgress.stub(projectId)

        val (_, actions) = projectSelectionListReducer.reduce(
            State(
                trackId,
                ContentState.Content(
                    track = Track.stub(trackId),
                    projects = mapOf(projectId to projectWithProgress),
                    currentProjectId = projectId,
                    sortedProjectsIds = listOf(projectId)
                )
            ),
            Message.ProjectClicked(projectId)
        )

        assertTrue {
            actions.any {
                it is Action.ViewAction.NavigateTo.ProjectDetails &&
                    it.trackId == trackId &&
                    it.projectId == projectId && it.isProjectSelected
            }
        }
        assertTrue {
            actions.any {
                it is InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is ProjectSelectionListClickedProjectHyperskillAnalyticsEvent &&
                    it.analyticEvent.projectId == projectId &&
                    it.analyticEvent.trackId == trackId
            }
        }
    }

    @Test
    fun `Recommended projects should not contain selected project`() {
        val selectedProjectId = 3L
        val selectedProject = Project.stub(selectedProjectId)
        val projects = listOf(0L, 1L, 2L, 3L, 4L, 5L)
        val content = ContentState.Content(
            track = Track.stub(0L),
            projects = projects.associateWith { id ->
                ProjectWithProgress(
                    progress = ProjectProgress.stub(projectId = id),
                    project = Project.stub(id)
                )
            },
            currentProjectId = selectedProjectId,
            sortedProjectsIds = projects
        )
        assertTrue {
            content.recommendedProjects.none {
                it.project == selectedProject
            }
        }
    }

    @Test
    fun `Recommended projects should be first 6 projects from track`() {
        val recommendedProjectsIds = (0..5).map { it.toLong() }
        val projectsIds = recommendedProjectsIds + (6..20).map { it.toLong() }
        val track = Track.stub(id = 0L, projects = recommendedProjectsIds)
        val content = ContentState.Content(
            track = track,
            projects = projectsIds.associateWith { id ->
                ProjectWithProgress(
                    progress = ProjectProgress.stub(projectId = id),
                    project = Project.stub(id)
                )
            },
            currentProjectId = null,
            sortedProjectsIds = projectsIds
        )
        assertEquals(
            recommendedProjectsIds,
            content.recommendedProjects.map { it.project.id }
        )
    }

    @Test
    fun `Best rated project should be a project with highest averageRating`() {
        val projects = (0..4).map { value ->
            ProjectWithProgress(
                project = Project.stub(id = value.toLong()),
                progress = ProjectProgress.stub(
                    projectId = value.toLong(),
                    usefulness = value.toFloat(),
                    funMeasure = value.toFloat(),
                    clarity = value.toFloat()
                )
            )
        }.associateBy { it.project.id }
        val content = ContentState.Content(
            track = Track.stub(0L),
            projects = projects,
            currentProjectId = null,
            sortedProjectsIds = projects.keys.toList()
        )
        assertEquals(
            projects[4]?.project?.id,
            content.bestRatedProjectId
        )
    }

    @Test
    fun `Best rated project should be null if there are no projects`() {
        val content = ContentState.Content(
            track = Track.stub(0L),
            projects = emptyMap(),
            currentProjectId = null,
            sortedProjectsIds = emptyList()
        )
        assertNull(content.bestRatedProjectId)
    }

    @Test
    fun `Best rated project should be null if all projects have zero averageRating`() {
        val projects = List(5) { id ->
            ProjectWithProgress(
                project = Project.stub(id = id.toLong()),
                progress = ProjectProgress.stub(
                    projectId = id.toLong(),
                    usefulness = 0f,
                    funMeasure = 0f,
                    clarity = 0f
                )
            )
        }.associateBy { it.project.id }
        val content = ContentState.Content(
            track = Track.stub(0L),
            projects = projects,
            currentProjectId = null,
            sortedProjectsIds = projects.keys.toList()
        )
        assertNull(content.bestRatedProjectId)
    }

    @Test
    fun `Project should be graduated if has Graduated kind for current track`() {
        val currentTrackId = 0L
        val otherTrackId = 1L
        ProjectKind.values().forEach { kind ->
            val project = Project.stub(
                id = 0L,
                tracks = mapOf(
                    currentTrackId.toString() to ProjectTracksEntry(
                        level = ProjectLevel.EASY,
                        kind = kind
                    ),
                    otherTrackId.toString() to ProjectTracksEntry(
                        level = ProjectLevel.EASY,
                        kind = kind
                    )
                )
            )
            if (kind == ProjectKind.GRADUATE) {
                assertTrue(project.isGraduate(currentTrackId))
            }
        }
    }

    // Test fastestToCompleteProjectId
    @Test
    fun `Fastest to complete project should be a project with lowest averageTimeToComplete`() {
        val projects = (1..10).map { value ->
            ProjectWithProgress(
                project = Project.stub(id = value.toLong()),
                progress = ProjectProgress.stub(
                    projectId = value.toLong(),
                    secondsToComplete = value.toFloat()
                )
            )
        }.shuffled().associateBy { it.project.id }
        val content = ContentState.Content(
            track = Track.stub(0L),
            projects = projects,
            currentProjectId = null,
            sortedProjectsIds = projects.keys.toList()
        )
        val expectedProjectId =
            projects.values.first { it.progress.secondsToComplete == 1f }.project.id
        assertEquals(
            expectedProjectId,
            content.fastestToCompleteProjectId
        )
    }

    @Test
    fun `Fastest to complete project should be null if there are no projects`() {
        val content = ContentState.Content(
            track = Track.stub(0L),
            projects = emptyMap(),
            currentProjectId = null,
            sortedProjectsIds = emptyList()
        )
        assertNull(content.fastestToCompleteProjectId)
    }

    @Test
    fun `Projects should be grouped by level`() {
        val easyProjects = listOf(1L, 0L, 2L)
        val mediumProjects = listOf(4L, 3L, 5L)
        val hardProjects = listOf(8L, 7L, 6L)
        val nightmareProjects = listOf(9L, 11L, 10L)
        val projectsByLevel = ProjectsByLevel(
            easy = easyProjects,
            medium = mediumProjects,
            hard = hardProjects,
            nightmare = nightmareProjects
        )
        val allProjects = (easyProjects + mediumProjects + hardProjects + nightmareProjects)
            .associateWith { id ->
                ProjectWithProgress(
                    project = Project.stub(id = id),
                    progress = ProjectProgress.stub(projectId = id)
                )
            }

        val sortedProjectsIds = (0L..12L).toList()

        val content = ContentState.Content(
            track = Track.stub(
                0L,
                projectsByLevel = projectsByLevel,
                projects = allProjects.keys.toList()
            ),
            projects = allProjects,
            currentProjectId = null,
            sortedProjectsIds = sortedProjectsIds
        )
        val viewState = viewStateMapper.map(content)

        ProjectLevel.values().forEachIndexed { index, level ->
            val expectedLevelProjectIds = sortedProjectsIds.windowed(3, 3)[index]
            assertEquals(
                expectedLevelProjectIds,
                viewState.safeCast<ViewState.Content>()
                    ?.projectsByLevel?.get(level)?.map { it.id } ?: emptyList()
            )
        }
    }

    @Test
    fun `Project should be sorted by base or default score`() {
        val initial = listOf(
            createProjectWithProgress(projectId = 3L, baseScore = 2f, defaultScore = 0f),
            createProjectWithProgress(projectId = 0L, defaultScore = 1f, baseScore = 0f),
            createProjectWithProgress(projectId = 2L, baseScore = 1f, defaultScore = 0f),
            createProjectWithProgress(projectId = 1L, defaultScore = 2f, baseScore = 0f)
        )

        val sortedProjects = initial.sortByScore(false)

        val actualOrder = sortedProjects.map { it.project.id }
        val expectedOrder = listOf(3L, 2L, 1L, 0L)

        assertEquals(expectedOrder, actualOrder)
    }

    @Test
    fun `Project should be sorted by feature score`() {
        val initial = listOf(
            createProjectWithProgress(projectId = 2L, featureScore = 2f),
            createProjectWithProgress(projectId = 0L, featureScore = 0f),
            createProjectWithProgress(projectId = 1L, featureScore = 1f),
            createProjectWithProgress(projectId = 3L, featureScore = 3f)
        )

        val sortedProjects = initial.sortByScore(true)
        val actualOrder = sortedProjects.map { it.project.id }

        val expectedOrder = listOf(3L, 2L, 1L, 0L)

        assertEquals(expectedOrder, actualOrder)
    }

    @Test
    fun `Project should be sorted by feature base or default score`() {
        val initial = listOf(
            createProjectWithProgress(projectId = 4L, featureScore = 2f, baseScore = 4f),
            createProjectWithProgress(projectId = 2L, baseScore = 2f, defaultScore = 3f),
            createProjectWithProgress(projectId = 1L, baseScore = 2f, defaultScore = 2f),
            createProjectWithProgress(projectId = 0L, baseScore = 1f, defaultScore = 6f),
            createProjectWithProgress(projectId = 3L, featureScore = 1f, baseScore = 5f),
            createProjectWithProgress(projectId = 5L, featureScore = 3f, defaultScore = 1f)
        )

        val sortedProjects = initial.sortByScore(true)

        val actualOrder = sortedProjects.map { it.project.id }
        val expectedOrder = (5L downTo 0L).toList()

        assertEquals(expectedOrder, actualOrder)
    }

    private fun createProjectWithProgress(
        projectId: Long,
        featureScore: Float = 0f,
        baseScore: Float = 0f,
        defaultScore: Float = 0f
    ) =
        ProjectWithProgress(
            project = Project.stub(id = projectId, defaultScore = defaultScore),
            progress = ProjectProgress.stub(
                projectId = projectId,
                baseScore = baseScore,
                featureScore = featureScore
            )
        )
}