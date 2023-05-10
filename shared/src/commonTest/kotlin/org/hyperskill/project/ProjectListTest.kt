package org.hyperskill.project

import kotlin.test.Test
import kotlin.test.assertTrue
import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.projects.presentation.ProjectsListFeature
import org.hyperskill.app.projects.presentation.recommendedProjects
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.track.stub

class ProjectListTest {

    @Test
    fun `Recommended projects should not contain selected project`() {
        val selectedProjectId = 3L
        val selectedProject = Project.stub(selectedProjectId)
        val projects = listOf(0L, 1L, 2L, 3L, 4L, 5L)
        val content = ProjectsListFeature.ContentState.Content(
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