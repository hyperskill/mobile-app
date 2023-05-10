package org.hyperskill.project

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.projects.presentation.ProjectsListFeature
import org.hyperskill.app.projects.presentation.ProjectsListFeature.ContentState
import org.hyperskill.app.projects.presentation.ProjectsListReducer
import org.hyperskill.app.projects.presentation.recommendedProjects
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.track.stub

class ProjectListTest {

    private val projectsListReducer = ProjectsListReducer()

    @Test
    fun `Initialize message should trigger content loading`() {
        val trackId = 0L
        val (state, actions) = projectsListReducer.reduce(
            ProjectsListFeature.initialState(trackId),
            ProjectsListFeature.Message.Initialize
        )
        assertContains(
            actions,
            ProjectsListFeature.Action.FetchContent(trackId, false)
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
            ProjectsListFeature.State(trackId, ContentState.Loading),
            ProjectsListFeature.Message.ContentFetchResult.Success(
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
            ProjectsListFeature.State(
                trackId,
                ContentState.Content(
                    track = Track.stub(trackId),
                    projects = emptyMap(),
                    selectedProjectId = null,
                    isRefreshing = false
                )
            ),
            ProjectsListFeature.Message.PullToRefresh
        )
        assertContains(
            actions,
            ProjectsListFeature.Action.FetchContent(trackId, forceLoadFromNetwork = true)
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
                ProjectsListFeature.State(
                    trackId = 0L,
                    content = contentState
                ),
                ProjectsListFeature.Message.PullToRefresh
            )
            assertTrue(actions.isEmpty())
            assertEquals(contentState, state.content)
        }
    }

    @Test
    fun `RetryContentLoading message should trigger force content loading`() {
        val trackId = 0L
        val (state, actions) = projectsListReducer.reduce(
            ProjectsListFeature.State(
                trackId,
                ContentState.Error
            ),
            ProjectsListFeature.Message.RetryContentLoading
        )
        assertContains(
            actions,
            ProjectsListFeature.Action.FetchContent(trackId, forceLoadFromNetwork = true)
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
                ProjectsListFeature.State(
                    trackId = 0L,
                    content = contentState
                ),
                ProjectsListFeature.Message.RetryContentLoading
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
            ProjectsListFeature.State(
                trackId,
                ContentState.Content(
                    track = Track.stub(trackId),
                    projects = mapOf(projectId to ProjectWithProgress.stub(projectId)),
                    selectedProjectId = null
                )
            ),
            ProjectsListFeature.Message.ProjectClicked(projectId)
        )
        assertContains(
            actions,
            ProjectsListFeature.Action.SelectProject(trackId, projectId)
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
            ProjectsListFeature.State(trackId, initialContentState),
            ProjectsListFeature.Message.ProjectSelectionResult.Success
        )
        assertContains(
            actions,
            ProjectsListFeature.Action.ViewAction.ShowProjectSelectionStatus.Success
        )
        assertContains(
            actions,
            ProjectsListFeature.Action.ViewAction.NavigateTo.HomeScreen
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
            ProjectsListFeature.State(trackId, initialContentState),
            ProjectsListFeature.Message.ProjectSelectionResult.Error
        )
        assertContains(
            actions,
            ProjectsListFeature.Action.ViewAction.ShowProjectSelectionStatus.Error
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
}