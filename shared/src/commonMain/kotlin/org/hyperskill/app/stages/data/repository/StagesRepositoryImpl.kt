package org.hyperskill.app.stages.data.repository

import org.hyperskill.app.stages.data.source.StagesRemoteDataSource
import org.hyperskill.app.stages.domain.model.Stage
import org.hyperskill.app.stages.domain.repository.StagesRepository

class StagesRepositoryImpl(
    private val stagesRemoteDataSource: StagesRemoteDataSource
) : StagesRepository {
    override suspend fun getStages(stagesIds: List<Long>): Result<List<Stage>> =
        stagesRemoteDataSource.getStages(stagesIds)

    override suspend fun getProjectStages(projectId: Long): Result<List<Stage>> =
        stagesRemoteDataSource.getProjectStages(projectId)
}