package org.hyperskill.app.stages.domain.repository

import org.hyperskill.app.stages.domain.model.Stage

interface StagesRepository {
    suspend fun getStages(stagesIds: List<Long>): Result<List<Stage>>

    suspend fun getProjectStages(projectId: Long): Result<List<Stage>>
}

suspend fun StagesRepository.getStage(stageId: Long): Result<Stage> =
    kotlin.runCatching {
        getStages(listOf(stageId)).getOrThrow().first()
    }