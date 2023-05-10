package org.hyperskill.app.projects.domain.model

data class ProjectWithProgress(
    val project: Project,
    val progress: ProjectProgress
) {
    companion object
}