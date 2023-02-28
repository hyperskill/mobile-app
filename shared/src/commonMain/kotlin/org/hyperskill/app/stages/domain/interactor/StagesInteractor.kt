package org.hyperskill.app.stages.domain.interactor

import org.hyperskill.app.stages.domain.model.Stage
import org.hyperskill.app.stages.domain.repository.StagesRepository

class StagesInteractor(
    private val stagesRepository: StagesRepository
) {
    suspend fun getStages(stagesIds: List<Long>): Result<List<Stage>> =
        stagesRepository.getStages(stagesIds)

    suspend fun getStage(stageId: Long): Result<Stage> =
        stagesRepository.getStage(stageId)

    suspend fun getProjectStages(projectId: Long): Result<List<Stage>> =
        stagesRepository.getProjectStages(projectId)
}