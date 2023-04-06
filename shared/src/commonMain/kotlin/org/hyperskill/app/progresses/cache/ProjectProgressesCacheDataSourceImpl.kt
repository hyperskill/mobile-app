package org.hyperskill.app.progresses.cache

import org.hyperskill.app.core.data.repository_cache.InMemoryRepositoryCache
import org.hyperskill.app.core.domain.repository_cache.RepositoryCache
import org.hyperskill.app.progresses.data.source.ProjectProgressesCacheDataSource
import org.hyperskill.app.projects.domain.model.ProjectProgress

class ProjectProgressesCacheDataSourceImpl(
    cache: InMemoryRepositoryCache<Long, ProjectProgress>
) : ProjectProgressesCacheDataSource, RepositoryCache<Long, ProjectProgress> by cache