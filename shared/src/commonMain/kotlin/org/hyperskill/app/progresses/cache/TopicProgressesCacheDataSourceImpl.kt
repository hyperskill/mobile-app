package org.hyperskill.app.progresses.cache

import org.hyperskill.app.core.data.repository_cache.InMemoryRepositoryCache
import org.hyperskill.app.core.domain.repository_cache.RepositoryCache
import org.hyperskill.app.progresses.data.source.TopicProgressesCacheDataSource
import org.hyperskill.app.topics.domain.model.TopicProgress

internal class TopicProgressesCacheDataSourceImpl(
    cache: InMemoryRepositoryCache<Long, TopicProgress>
) : TopicProgressesCacheDataSource, RepositoryCache<Long, TopicProgress> by cache