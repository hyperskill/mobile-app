package org.hyperskill.app.learning_activities.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.learning_activities.data.source.LearningActivitiesRemoteDataSource
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesRequest
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesResponse

class LearningActivitiesRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : LearningActivitiesRemoteDataSource {

    override suspend fun getLearningActivities(request: LearningActivitiesRequest): Result<List<LearningActivity>> =
        kotlin.runCatching {
            httpClient
                .get("/api/learning-activities") {
                    contentType(ContentType.Application.Json)
                    request.parameters.forEach { (key, value) ->
                        parameter(key, value)
                    }
                }
                .body<LearningActivitiesResponse>().learningActivities
        }
}