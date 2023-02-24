package org.hyperskill.app.learning_activities.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.learning_activities.data.source.LearningActivitiesRemoteDataSource
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesResponse

class LearningActivitiesRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : LearningActivitiesRemoteDataSource {
    override suspend fun getUncompletedTopicsLearningActivities(
        studyPlanId: Long,
        pageSize: Int,
        page: Int
    ): Result<LearningActivitiesResponse> =
        kotlin.runCatching {
            httpClient
                .get("/api/learning-activities") {
                    contentType(ContentType.Application.Json)
                    parameter("study_plan", studyPlanId)
                    parameter("page_size", pageSize)
                    parameter("page", page)
                    parameter("state", LearningActivityState.TODO.value)
                    parameter("types", LearningActivityType.LEARN_TOPIC.value)
                }.body()
        }
}