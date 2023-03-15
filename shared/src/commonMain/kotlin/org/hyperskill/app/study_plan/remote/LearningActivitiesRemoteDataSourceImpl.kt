package org.hyperskill.app.study_plan.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.study_plan.data.source.LearningActivitiesRemoteDataSource
import org.hyperskill.app.study_plan.domain.model.LearningActivity
import org.hyperskill.app.study_plan.remote.model.LearningActivitiesResponse

class LearningActivitiesRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : LearningActivitiesRemoteDataSource {
    override suspend fun getLearningActivities(activitiesIds: List<Long>): Result<List<LearningActivity>> =
        kotlin.runCatching {
            httpClient
                .get("api/learning-activities") {
                    contentType(ContentType.Application.Json)
                    parameter("ids", activitiesIds.joinToString(separator = ","))
                }.body<LearningActivitiesResponse>().learningActivities
        }
}