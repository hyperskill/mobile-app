package org.hyperskill.app.stages.data.source

import org.hyperskill.app.stages.domain.model.Stage

interface StagesRemoteDataSource {
    suspend fun getStages(stagesIds: List<Long>): Result<List<Stage>>

    suspend fun getProjectStages(projectId: Long): Result<List<Stage>>
}