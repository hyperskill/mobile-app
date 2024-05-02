package org.hyperskill.app.analytic.domain.model

interface AnalyticEngine {
    val targetSource: AnalyticSource

    suspend fun reportEvent(
        event: AnalyticEvent,
        userProperties: AnalyticEventUserProperties,
        force: Boolean = false
    )
    suspend fun flushEvents()
}