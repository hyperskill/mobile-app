package org.hyperskill.app.projects.injection

import org.hyperskill.app.projects.domain.repository.ProjectsRepository

interface ProjectsDataComponent {
    val projectsRepository: ProjectsRepository
}