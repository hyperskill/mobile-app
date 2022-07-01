package org.hyperskill.app.project.data.source

import org.hyperskill.app.project.domain.model.Project

interface ProjectRemoteDataSource {
    suspend fun getProjects(projectIds: List<Long>): Result<List<Project>>
}