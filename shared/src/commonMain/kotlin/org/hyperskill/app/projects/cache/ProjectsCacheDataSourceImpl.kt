package org.hyperskill.app.projects.cache

import org.hyperskill.app.core.data.repository_cache.InMemoryRepositoryCache
import org.hyperskill.app.core.domain.repository_cache.RepositoryCache
import org.hyperskill.app.projects.data.source.ProjectsCacheDataSource
import org.hyperskill.app.projects.domain.model.Project

class ProjectsCacheDataSourceImpl(
    cache: InMemoryRepositoryCache<Long, Project>
) : ProjectsCacheDataSource, RepositoryCache<Long, Project> by cache