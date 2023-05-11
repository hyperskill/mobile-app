package org.hyperskill.projects

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.model.ProjectKind
import org.hyperskill.app.projects.domain.model.ProjectLevel
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.projects.domain.model.ProjectTracksEntry
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.projects.domain.model.isGraduated
import org.hyperskill.app.projects.presentation.ProjectsListFeature
import org.hyperskill.app.projects.presentation.ProjectsListFeature.Action
import org.hyperskill.app.projects.presentation.ProjectsListFeature.ContentState
import org.hyperskill.app.projects.presentation.ProjectsListFeature.InternalAction
import org.hyperskill.app.projects.presentation.ProjectsListFeature.Message
import org.hyperskill.app.projects.presentation.ProjectsListFeature.State
import org.hyperskill.app.projects.presentation.ProjectsListFeature.ViewState
import org.hyperskill.app.projects.presentation.ProjectsListReducer
import org.hyperskill.app.projects.presentation.bestRatedProjectId
import org.hyperskill.app.projects.presentation.fastestToCompleteProjectId
import org.hyperskill.app.projects.presentation.recommendedProjects
import org.hyperskill.app.projects.view.mapper.ProjectsListViewStateMapper
import org.hyperskill.app.track.domain.model.ProjectsByLevel
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.track.stub
import ru.nobird.app.core.model.safeCast

class ProjectsListTest {

    private val projectsListReducer = ProjectsListReducer()

    private val resourceProvider = ResourceProviderStub()

    private val viewStateMapper = ProjectsListViewStateMapper(resourceProvider)

    @Test
    fun `Initialize message should trigger content loading`() {
        val trackId = 0L
        val (state, actions) = projectsListReducer.reduce(
            ProjectsListFeature.initialState(trackId),
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
        val projects = listOf(0L, 1L, 2L, 3L, 4L, 5L)
        val selectedProjectId = 3L

        val (state, actions) = projectsListReducer.reduce(
            State(trackId, ContentState.Loading),
            ProjectsListFeature.ContentFetchResult.Success(
                track = track,
                projects = projects.map(ProjectWithProgress.Companion::stub),
                selectedProjectId = selectedProjectId
            )
        )
        assertTrue(actions.isEmpty())

        val expectedContent = ContentState.Content(
            track = track,
            projects = projects.associateWith(ProjectWithProgress.Companion::stub),
            selectedProjectId = selectedProjectId
        )
        assertEquals(
            expectedContent,
            state.content
        )
    }

    @Test
    fun `PullToRefresh message should trigger force content loading`() {
        val trackId = 0L
        val (state, actions) = projectsListReducer.reduce(
            State(
                trackId,
                ContentState.Content(
                    track = Track.stub(trackId),
                    projects = emptyMap(),
                    selectedProjectId = null,
                    isRefreshing = false
                )
            ),
            Message.PullToRefresh
        )
        assertContains(
            actions,
            InternalAction.FetchContent(trackId, forceLoadFromNetwork = true)
        )
        assertTrue {
            val content = state.content
            content is ContentState.Content && content.isRefreshing
        }
    }

    @Test
    fun `PullToRefresh should not be handled in non Content state`() {
        listOf(
            ContentState.Loading,
            ContentState.Error,
            ContentState.Idle
        ).forEach { contentState ->
            val (state, actions) = projectsListReducer.reduce(
                State(
                    trackId = 0L,
                    content = contentState
                ),
                Message.PullToRefresh
            )
            assertTrue(actions.isEmpty())
            assertEquals(contentState, state.content)
        }
    }

    @Test
    fun `RetryContentLoading message should trigger force content loading`() {
        val trackId = 0L
        val (state, actions) = projectsListReducer.reduce(
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
                selectedProjectId = null
            ),
            ContentState.Idle
        ).forEach { contentState ->
            val (state, actions) = projectsListReducer.reduce(
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
    fun `ProjectClicked message should trigger project selection`() {
        val trackId = 0L
        val projectId = 1L
        val (state, actions) = projectsListReducer.reduce(
            State(
                trackId,
                ContentState.Content(
                    track = Track.stub(trackId),
                    projects = mapOf(projectId to ProjectWithProgress.stub(projectId)),
                    selectedProjectId = null
                )
            ),
            Message.ProjectClicked(projectId)
        )
        assertContains(
            actions,
            InternalAction.SelectProject(trackId, projectId)
        )
        assertTrue {
            val content = state.content
            content is ContentState.Content && content.isProjectSelectionLoadingShowed
        }
    }

    @Test
    fun `Successful project selection should trigger success status and navigation to the Home screen`() {
        val trackId = 0L
        val projectId = 1L
        val initialContentState = ContentState.Content(
            track = Track.stub(trackId),
            projects = mapOf(projectId to ProjectWithProgress.stub(projectId)),
            selectedProjectId = null,
            isProjectSelectionLoadingShowed = true
        )
        val (state, actions) = projectsListReducer.reduce(
            State(trackId, initialContentState),
            ProjectsListFeature.ProjectSelectionResult.Success
        )
        assertContains(
            actions,
            Action.ViewAction.ShowProjectSelectionStatus.Success
        )
        assertContains(
            actions,
            Action.ViewAction.NavigateTo.HomeScreen
        )
        assertEquals(
            initialContentState.copy(isProjectSelectionLoadingShowed = false),
            state.content
        )
    }

    @Test
    fun `Failed project selection should trigger error status and hide loading`() {
        val trackId = 0L
        val projectId = 1L
        val initialContentState = ContentState.Content(
            track = Track.stub(trackId),
            projects = mapOf(projectId to ProjectWithProgress.stub(projectId)),
            selectedProjectId = null,
            isProjectSelectionLoadingShowed = true
        )
        val (state, actions) = projectsListReducer.reduce(
            State(trackId, initialContentState),
            ProjectsListFeature.ProjectSelectionResult.Error
        )
        assertContains(
            actions,
            Action.ViewAction.ShowProjectSelectionStatus.Error
        )
        assertEquals(
            initialContentState.copy(isProjectSelectionLoadingShowed = false),
            state.content
        )
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
            selectedProjectId = selectedProjectId
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
        val projectsIds = (6..20).map { it.toLong() } + recommendedProjectsIds
        val track = Track.stub(id = 0L, projects = recommendedProjectsIds)
        val content = ContentState.Content(
            track = track,
            projects = projectsIds.associateWith { id ->
                ProjectWithProgress(
                    progress = ProjectProgress.stub(projectId = id),
                    project = Project.stub(id)
                )
            },
            selectedProjectId = null
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
            selectedProjectId = null
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
            selectedProjectId = null
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
            selectedProjectId = null
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
                assertTrue(project.isGraduated(currentTrackId))
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
                    secondsToComplete = value.toDouble()
                )
            )
        }.shuffled().associateBy { it.project.id }
        val content = ContentState.Content(
            track = Track.stub(0L),
            projects = projects,
            selectedProjectId = null
        )
        val expectedProjectId =
            projects.values.first { it.progress.secondsToComplete == 1.0 }.project.id
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
            selectedProjectId = null
        )
        assertNull(content.fastestToCompleteProjectId)
    }

    @Test
    fun `Projects should be grouped by level`() {
        val easyProjects = listOf(0L, 1L)
        val mediumProjects = listOf(2L, 3L)
        val hardProjects = listOf(4L, 5L)
        val nightmareProjects = listOf(6L, 7L)
        val projectsByLevel = ProjectsByLevel(
            easy = easyProjects,
            medium = mediumProjects,
            hard = hardProjects,
            nightmare = nightmareProjects
        )
        val allProjects = (easyProjects + mediumProjects + hardProjects + nightmareProjects)
            .shuffled()
            .associateWith { id ->
                ProjectWithProgress(
                    project = Project.stub(id = id),
                    progress = ProjectProgress.stub(projectId = id)
                )
            }
        val content = ContentState.Content(
            track = Track.stub(
                0L,
                projectsByLevel = projectsByLevel,
                projects = allProjects.keys.toList()
            ),
            projects = allProjects,
            selectedProjectId = null
        )
        val viewState = viewStateMapper.map(content)

        ProjectLevel.values().forEach { level ->
            val projectsIds = when (level) {
                ProjectLevel.EASY -> projectsByLevel.easy
                ProjectLevel.MEDIUM -> projectsByLevel.medium
                ProjectLevel.HARD -> projectsByLevel.hard
                ProjectLevel.NIGHTMARE -> projectsByLevel.nightmare
            }
            assertEquals(
                projectsIds,
                viewState.safeCast<ViewState.Content>()
                    ?.projectsByLevel?.get(level)?.map { it.id } ?: emptyList()
            )
        }
    }
}