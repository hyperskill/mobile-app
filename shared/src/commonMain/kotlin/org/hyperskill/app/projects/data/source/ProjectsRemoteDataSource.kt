package org.hyperskill.app.projects.data.source

import org.hyperskill.app.projects.domain.model.Project

interface ProjectsRemoteDataSource {
    suspend fun getProjects(projectsIds: List<Long>): Result<List<Project>>
}