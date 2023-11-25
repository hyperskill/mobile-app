package org.hyperskill.app.learning_activities.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.hyperskill.app.learning_activities.data.source.LearningActivitiesRemoteDataSource
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesRequest
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesResponse
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesWithSectionsRequest
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesWithSectionsResponse
import org.hyperskill.app.learning_activities.remote.model.NextLearningActivityRequest

internal class LearningActivitiesRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : LearningActivitiesRemoteDataSource {
    override suspend fun getNextLearningActivity(request: NextLearningActivityRequest): Result<LearningActivity?> =
        kotlin.runCatching {
            httpClient
                .get("/api/learning-activities/next") {
                    contentType(ContentType.Application.Json)
                    request.parameters.forEach { (key, value) ->
                        parameter(key, value)
                    }
                }
                .body<LearningActivitiesResponse>().learningActivities.firstOrNull()
        }

    override suspend fun getLearningActivities(request: LearningActivitiesRequest): Result<List<LearningActivity>> =
        kotlin.runCatching {
            if (request.chunkedParameters.size == 1) {
                getLearningActivities(request.chunkedParameters.first())
            } else {
                val deferreds = mutableListOf<Deferred<List<LearningActivity>>>()

                for (parameters in request.chunkedParameters) {
                    val deferred = httpClient.async {
                        getLearningActivities(parameters)
                    }
                    deferreds.add(deferred)
                }

                val responses = deferreds.awaitAll()

                return Result.success(responses.flatten())
            }
        }

    private suspend fun getLearningActivities(parameters: Map<String, Any>): List<LearningActivity> =
        httpClient
            .get("/api/learning-activities") {
                contentType(ContentType.Application.Json)
                parameters.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
            .body<LearningActivitiesResponse>().learningActivities

    override suspend fun getLearningActivitiesWithSections(
        request: LearningActivitiesWithSectionsRequest
    ): Result<LearningActivitiesWithSectionsResponse> =
        kotlin.runCatching {
            httpClient
                .get("/api/learning-activities/with-sections") {
                    contentType(ContentType.Application.Json)
                    request.parameters.forEach { (key, value) ->
                        parameter(key, value)
                    }
                }
                .body()
        }
}