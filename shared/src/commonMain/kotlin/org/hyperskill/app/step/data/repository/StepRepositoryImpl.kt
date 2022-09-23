package org.hyperskill.app.step.data.repository

import org.hyperskill.app.step.data.source.StepRemoteDataSource
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.repository.StepRepository

class StepRepositoryImpl(
    private val stepRemoteDataSource: StepRemoteDataSource
) : StepRepository {
    override suspend fun getSteps(stepIds: List<Long>): Result<List<Step>> =
        stepRemoteDataSource.getSteps(stepIds)
}