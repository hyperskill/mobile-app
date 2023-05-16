package org.hyperskill.projects_selection

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.project_selection.domain.analytic.ProjectSelectionListSelectConfirmationResultHyperskillAnalyticEvent
import org.hyperskill.app.project_selection.domain.analytic.ProjectsSelectionListClickedProjectHyperskillAnalyticsEvent
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.Action
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.ContentState
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.InternalAction
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.Message
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.State
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.ViewState
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListReducer
import org.hyperskill.app.project_selection.presentation.bestRatedProjectId
import org.hyperskill.app.project_selection.presentation.fastestToCompleteProjectId
import org.hyperskill.app.project_selection.presentation.recommendedProjects
import org.hyperskill.app.project_selection.view.mapper.ProjectSelectionListViewStateMapper
import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.model.ProjectKind
import org.hyperskill.app.projects.domain.model.ProjectLevel
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.projects.domain.model.ProjectTracksEntry
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.projects.domain.model.isGraduate
import org.hyperskill.app.track.domain.model.ProjectsByLevel
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.track.stub
import ru.nobird.app.core.model.safeCast

class ProjectsListTest {

    private val projectSelectionListReducer = ProjectSelectionListReducer()

    private val resourceProvider = ResourceProviderStub()

    private val viewStateMapper = ProjectSelectionListViewStateMapper(resourceProvider)

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
        val projects = listOf(0L, 1L, 2L, 3L, 4L, 5L)
        val currentProjectId = 3L

        val (state, actions) = projectSelectionListReducer.reduce(
            State(trackId, ContentState.Loading),
            ProjectSelectionListFeature.ContentFetchResult.Success(
                track = track,
                projects = projects.map(ProjectWithProgress.Companion::stub),
                currentProjectId = currentProjectId
            )
        )
        assertTrue(actions.isEmpty())

        val expectedContent = ContentState.Content(
            track = track,
            projects = projects.associateWith(ProjectWithProgress.Companion::stub),
            currentProjectId = currentProjectId
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
                currentProjectId = null
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
    fun `ProjectClicked message should request user permission to add the project to their profile`() {
        val trackId = 0L
        val projectId = 1L
        val projectWithProgress = ProjectWithProgress.stub(projectId)
        val (_, actions) = projectSelectionListReducer.reduce(
            State(
                trackId,
                ContentState.Content(
                    track = Track.stub(trackId),
                    projects = mapOf(projectId to projectWithProgress),
                    currentProjectId = null
                )
            ),
            Message.ProjectClicked(projectId)
        )
        assertContains(
            actions,
            Action.ViewAction.ShowProjectSelectionConfirmationModal(projectWithProgress.project)
        )
        assertTrue {
            actions.any {
                it is InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is ProjectsSelectionListClickedProjectHyperskillAnalyticsEvent &&
                    it.analyticEvent.projectId == projectId &&
                    it.analyticEvent.trackId == trackId
            }
        }
    }

    @Test
    fun `ProjectClicked message on current project not request user permission to add the project to their profile`() {
        val trackId = 0L
        val projectId = 1L
        val projectWithProgress = ProjectWithProgress.stub(projectId)

        val (_, actions) = projectSelectionListReducer.reduce(
            State(
                trackId,
                ContentState.Content(
                    track = Track.stub(trackId),
                    projects = mapOf(projectId to projectWithProgress),
                    currentProjectId = projectId
                )
            ),
            Message.ProjectClicked(projectId)
        )

        assertEquals(1, actions.size)
        assertTrue {
            actions.any {
                it is InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is ProjectsSelectionListClickedProjectHyperskillAnalyticsEvent &&
                    it.analyticEvent.projectId == projectId &&
                    it.analyticEvent.trackId == trackId
            }
        }
    }

    @Test
    fun `Confirming selection permission should trigger project selection`() {
        val trackId = 0L
        val projectId = 1L
        val projectWithProgress = ProjectWithProgress.stub(projectId)
        val (_, actions) = projectSelectionListReducer.reduce(
            State(
                trackId,
                ContentState.Content(
                    track = Track.stub(trackId),
                    projects = mapOf(projectId to projectWithProgress),
                    currentProjectId = null
                )
            ),
            Message.ProjectSelectionConfirmationResult(projectId, true)
        )
        assertContains(
            actions,
            InternalAction.SelectProject(trackId, projectId)
        )
        assertTrue {
            actions.any {
                it is InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is ProjectSelectionListSelectConfirmationResultHyperskillAnalyticEvent &&
                    it.analyticEvent.trackId == trackId
            }
        }
    }

    @Test
    fun `Project selection should not be triggered if user reject project selection permission`() {
        val trackId = 0L
        val projectId = 1L
        val projectWithProgress = ProjectWithProgress.stub(projectId)
        val (_, actions) = projectSelectionListReducer.reduce(
            State(
                trackId,
                ContentState.Content(
                    track = Track.stub(trackId),
                    projects = mapOf(projectId to projectWithProgress),
                    currentProjectId = null
                )
            ),
            Message.ProjectSelectionConfirmationResult(projectId, false)
        )
        assertFalse {
            actions.any {
                it is InternalAction.SelectProject
            }
        }
    }

    @Test
    fun `Successful project selection should trigger success status and navigation to the StudyPlan screen`() {
        val trackId = 0L
        val projectId = 1L
        val initialContentState = ContentState.Content(
            track = Track.stub(trackId),
            projects = mapOf(projectId to ProjectWithProgress.stub(projectId)),
            currentProjectId = null,
            isProjectSelectionLoadingShowed = true
        )
        val (state, actions) = projectSelectionListReducer.reduce(
            State(trackId, initialContentState),
            ProjectSelectionListFeature.ProjectSelectionResult.Success
        )
        assertContains(
            actions,
            Action.ViewAction.ShowProjectSelectionStatus.Success
        )
        assertContains(
            actions,
            Action.ViewAction.NavigateTo.StudyPlan
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
            currentProjectId = null,
            isProjectSelectionLoadingShowed = true
        )
        val (state, actions) = projectSelectionListReducer.reduce(
            State(trackId, initialContentState),
            ProjectSelectionListFeature.ProjectSelectionResult.Error
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
            currentProjectId = selectedProjectId
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
            currentProjectId = null
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
            currentProjectId = null
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
            currentProjectId = null
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
            currentProjectId = null
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
            currentProjectId = null
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
            currentProjectId = null
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
            currentProjectId = null
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