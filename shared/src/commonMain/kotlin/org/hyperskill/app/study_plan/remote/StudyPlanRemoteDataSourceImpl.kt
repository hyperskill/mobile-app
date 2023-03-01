package org.hyperskill.app.study_plan.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.study_plan.data.source.StudyPlanRemoteDataSource
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.study_plan.remote.model.StudyPlanResponse

class StudyPlanRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : StudyPlanRemoteDataSource {
    override suspend fun getStudyPlans(): Result<List<StudyPlan>> =
        kotlin.runCatching {
            httpClient
                .get("/api/study-plans") {
                    contentType(ContentType.Application.Json)
                }.body<StudyPlanResponse>().studyPlans
        }

    override suspend fun getCurrentStudyPlan(): Result<StudyPlan> =
        kotlin.runCatching {
            httpClient
                .get("/api/study-plans/current") {
                    contentType(ContentType.Application.Json)
                }.body<StudyPlanResponse>().studyPlans.first()
        }
}