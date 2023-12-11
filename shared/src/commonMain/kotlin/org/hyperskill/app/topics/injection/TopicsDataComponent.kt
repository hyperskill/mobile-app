package org.hyperskill.app.topics.injection

import org.hyperskill.app.topics.domain.repository.TopicsRepository

interface TopicsDataComponent {
    val topicsRepository: TopicsRepository
}