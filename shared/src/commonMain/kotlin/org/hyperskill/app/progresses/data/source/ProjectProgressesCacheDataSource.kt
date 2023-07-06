package org.hyperskill.app.progresses.data.source

import org.hyperskill.app.core.domain.repository_cache.RepositoryCache
import org.hyperskill.app.projects.domain.model.ProjectProgress

interface ProjectProgressesCacheDataSource : RepositoryCache<Long, ProjectProgress>