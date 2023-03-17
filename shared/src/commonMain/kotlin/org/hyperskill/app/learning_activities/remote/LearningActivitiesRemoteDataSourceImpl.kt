package org.hyperskill.app.learning_activities.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.learning_activities.data.source.LearningActivitiesRemoteDataSource
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
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
            requestLeaningActivities(
                studyPlanId = studyPlanId,
                pageSize = pageSize,
                page = page,
                types = setOf(LearningActivityType.LEARN_TOPIC)
            ).body()
        }

    override suspend fun getLearningActivities(
        activitiesIds: List<Long>,
        types: Set<LearningActivityType>
    ): Result<List<LearningActivity>> =
        kotlin.runCatching {
            requestLeaningActivities(activitiesIds = activitiesIds, types = types)
                .body<LearningActivitiesResponse>().learningActivities
        }

    private suspend fun requestLeaningActivities(
        studyPlanId: Long? = null,
        pageSize: Int? = null,
        page: Int? = null,
        activitiesIds: List<Long> = emptyList(),
        state: LearningActivityState = LearningActivityState.TODO,
        types: Set<LearningActivityType>
    ): HttpResponse =
        httpClient
            .get("/api/learning-activities") {
                contentType(ContentType.Application.Json)
                studyPlanId?.let {
                    parameter("study_plan", studyPlanId)
                }
                pageSize?.let {
                    parameter("page_size", pageSize)
                }
                page?.let {
                    parameter("page", page)
                }
                parameter("state", state.value)
                parameter(
                    "types",
                    types.joinToString(",") { it.value.toString() }
                )
                parameter("ids", activitiesIds.joinToString(separator = ","))
            }
}