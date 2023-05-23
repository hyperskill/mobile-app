package org.hyperskill.app.study_plan.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.network.remote.parameterIds
import org.hyperskill.app.study_plan.data.source.StudyPlanSectionsRemoteDataSource
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.remote.model.StudyPlanSectionsResponse

class StudyPlanSectionsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : StudyPlanSectionsRemoteDataSource {
    override suspend fun getStudyPlanSections(sectionsIds: List<Long>): Result<List<StudyPlanSection>> =
        kotlin.runCatching {
            httpClient
                .get("api/study-plan-sections") {
                    contentType(ContentType.Application.Json)
                    parameterIds(sectionsIds)
                }.body<StudyPlanSectionsResponse>().studyPlanSections
        }
}