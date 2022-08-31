package org.hyperskill.app.analytic.data.source

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

interface AnalyticHyperskillRemoteDataSource {
    suspend fun flushEvents(events: List<AnalyticEvent>, isAuthorized: Boolean): Result<Unit>
}