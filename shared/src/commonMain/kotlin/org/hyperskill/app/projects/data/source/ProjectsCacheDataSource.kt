package org.hyperskill.app.projects.data.source

import org.hyperskill.app.core.domain.repository_cache.RepositoryCache
import org.hyperskill.app.projects.domain.model.Project

interface ProjectsCacheDataSource : RepositoryCache<Long, Project>