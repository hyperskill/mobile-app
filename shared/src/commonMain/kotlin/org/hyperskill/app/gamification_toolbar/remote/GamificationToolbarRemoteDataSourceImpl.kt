package org.hyperskill.app.gamification_toolbar.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.gamification_toolbar.data.source.GamificationToolbarRemoteDataSource
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarData
import org.hyperskill.app.gamification_toolbar.remote.model.CurrentGamificationToolbarResponse

class GamificationToolbarRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : GamificationToolbarRemoteDataSource {
    override suspend fun getGamificationToolbarData(): Result<GamificationToolbarData> =
        kotlin.runCatching {
            httpClient
                .get("/api/gamification-toolbars/current") {
                    contentType(ContentType.Application.Json)
                }.body<CurrentGamificationToolbarResponse>().gamificationToolbars.first()
        }
}