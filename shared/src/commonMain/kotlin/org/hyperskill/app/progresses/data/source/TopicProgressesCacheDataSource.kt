package org.hyperskill.app.progresses.data.source

import org.hyperskill.app.core.domain.repository_cache.RepositoryCache
import org.hyperskill.app.topics.domain.model.TopicProgress

interface TopicProgressesCacheDataSource : RepositoryCache<Long, TopicProgress>